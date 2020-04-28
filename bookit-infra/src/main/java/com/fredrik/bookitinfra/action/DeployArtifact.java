package com.fredrik.bookitinfra.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.fredrik.bookitinfra.AppConfig;
import com.fredrik.bookitinfra.cmd.CommonUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public final class DeployArtifact {

//    private static Logger LOG = LoggerFactory.getLogger(DeployToOpenshift.class);

	private static WatchService watcher;
	private static Map<WatchKey, Path> keys = new HashMap<WatchKey, Path>();

	public DeployArtifact() {

	}

	public static void main(String args[]) {
		String env = null;
		if (args[0] != null) {
			env = args[0];
		}
		String version = null;
		if (args[1] != null && !args[1].trim().isEmpty()) {
			version = args[1];
		} else {
			version = System.getenv("VERSION");
		}
		String passwd = null;
		if (args[2] != null && !args[2].trim().isEmpty()) {
			passwd = args[2];
		} else {
			passwd = System.getenv("PASSWD");
		}
		if (version == null) {
			if (AppConfig.getInstance().getApplicationVersion() != null) {
				version = AppConfig.getInstance().getApplicationVersion();
			} else {
				version = "latest";
			}
		}

		deployToInfra(env, passwd);
	}


	public static void deployToInfra(String env, String password) {

		String userid = "pi"; // System.getProperty("userid");

		if (env.equalsIgnoreCase("localdev")) {
//			File localApacheAppDir = new File("C:\\xampp\\htdocs\\pp2");
//
//			// Copy files from phpRootDir to C:\xampp\htdocs\pp2....
//			try {
//				Path src = phpRootDir.toPath();
//				Path dest = localApacheAppDir.toPath();
//
//				// Remove destination first
//				if (localApacheAppDir.exists()) {
//					Files.walk(dest).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
//				}
////				System.out.println("Dest deleted. " + apacheAppDir.exists());				
//
////				CommonUtil.copyFolder(src, dest);
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}

		} else if (env.equals("test")) {

			JSch jsch = new JSch();
			Session session = null;
			try {
				session = jsch.getSession(userid, "tullockautomation.asuscomm.com", 2222);
				session.setPassword(password);

				java.util.Properties config = new java.util.Properties();
				config.put("StrictHostKeyChecking", "no");
				session.setConfig(config);
				session.connect();

				execute(session, "pwd");

				execute(session, "ls -la");
				
			} catch (JSchException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				session.disconnect();
			}

		} else if (env.equals("prod")) {

			JSch jsch = new JSch();
			Session session = null;
			try {
				session = jsch.getSession(userid, "tullockautomation.asuscomm.com", 2222);
				session.setPassword(password);

				java.util.Properties config = new java.util.Properties();
				config.put("StrictHostKeyChecking", "no");
				session.setConfig(config);
				session.connect();
				
				deployArtifactOverSFtp(session);
				
			} catch (JSchException e) {
				e.printStackTrace();
			} catch (SftpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				session.disconnect();
			}

		}

//		notifyAboutDeployment(oc.getEnv(), oc.getVersion());
	}

	private static void deployArtifactOverSFtp(Session session) throws SftpException, JSchException, IOException {

		File localDir = CommonUtil.resolveDirWithFile("bookit-backend");
//		String pwd = sftp.pwd();
//		System.out.println("Pwd: " + pwd);
		File localFile = new File(localDir, "target/bookit-backend-0.1-SNAPSHOT.jar");

		execute(session, "./stop_bookit.sh");

		copyFile(session, localFile);
		
		execute(session, "./start_bookit.sh", Duration.ofMillis(50 * 1000));
	}

	private static void copyFile(Session session, File localFile) throws JSchException {
		ChannelSftp sftp = null;
		try {
			sftp = (ChannelSftp) session.openChannel("sftp");		
			sftp.connect();
			
			System.out.println("Copying file: " + localFile.getName());
			sftp.put(new FileInputStream(localFile), localFile.getName(), ChannelSftp.OVERWRITE);
			
		} catch (SftpException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			sftp.disconnect();
		}
	}

	
	
	private static void copyDirTree(ChannelSftp sftp, File localFile, String destPath) {
		try {

			if (localFile.isDirectory()) {
				sftp.mkdir(localFile.getName());
//				System.out.println("Created Folder: " + localFile.getName() + " in " + destPath);
//				System.out.println("");				
//				System.out.print("#");

				destPath = destPath + "/" + localFile.getName();
				sftp.cd(destPath);

				for (File file : localFile.listFiles()) {
					copyDirTree(sftp, file, destPath);
				}
				sftp.cd(destPath.substring(0, destPath.lastIndexOf('/')));
			} else {
//				System.out.println("Copying File: " + localFile.getName() + " to " + destPath);
//				System.out.print(".");
				sftp.put(new FileInputStream(localFile), localFile.getName(), ChannelSftp.OVERWRITE);
			}
		} catch (SftpException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static void recursiveChGrp(ChannelSftp channelSftp, int gid, String path) throws SftpException {

		// List source directory structure.
		Collection<ChannelSftp.LsEntry> fileAndFolderList = channelSftp.ls(path);

		// Iterate objects in the list to get file/folder names.
		for (ChannelSftp.LsEntry item : fileAndFolderList) {
			if (!item.getAttrs().isDir()) {
				channelSftp.chgrp(gid, path + "/" + item.getFilename());
			} else if (!(".".equals(item.getFilename()) || "..".equals(item.getFilename()))) { // If it is a subdir.
				try {
					// removing sub directory.
					channelSftp.chgrp(gid, path + "/" + item.getFilename());
//	            	channelSftp.rmdir(path + "/" + item.getFilename());
				} catch (Exception e) { // If subdir is not empty and error occurs.
					// Do lsFolderRemove on this subdir to enter it and clear its contents.
					recursiveFolderDelete(channelSftp, path + "/" + item.getFilename());
				}
			}
		}
		channelSftp.chgrp(gid, path);
//	    channelSftp.rmdir(path); // delete the parent directory after empty
	}

	private static void recursiveChMod(ChannelSftp channelSftp, String number, String path) throws SftpException {
		int chmodInt = Integer.parseInt(number, 8);

		// List source directory structure.
		Collection<ChannelSftp.LsEntry> fileAndFolderList = channelSftp.ls(path);

		// Iterate objects in the list to get file/folder names.
		for (ChannelSftp.LsEntry item : fileAndFolderList) {
			if (!item.getAttrs().isDir()) {
				// Chmod on file
				channelSftp.chmod(chmodInt, path + "/" + item.getFilename());
			} else if (!(".".equals(item.getFilename()) || "..".equals(item.getFilename()))) { // If it is a subdir.
				// Do chmod on this subdir
				recursiveChMod(channelSftp, number, path + "/" + item.getFilename());
			}
		}
		channelSftp.chmod(chmodInt, path);
	}

	private static void recursiveChModOnFiles(ChannelSftp channelSftp, String number, String path, String filePattern)
			throws SftpException {
		int chmodInt = Integer.parseInt(number, 8);

		// List source directory structure.
		Collection<ChannelSftp.LsEntry> fileAndFolderList = channelSftp.ls(path);

		// Iterate objects in the list to get file/folder names.
		for (ChannelSftp.LsEntry item : fileAndFolderList) {
			if (!item.getAttrs().isDir() && item.getFilename().contains(filePattern)) {
				// Chmod on file
				channelSftp.chmod(chmodInt, path + "/" + item.getFilename());
			} else if (!(".".equals(item.getFilename()) || "..".equals(item.getFilename()))) { // If it is a subdir.
				// Do chmod on this subdir
				recursiveChModOnFiles(channelSftp, number, path + "/" + item.getFilename(), filePattern);
			}
		}
	}

	private static void recursiveFolderDelete(ChannelSftp channelSftp, String path) throws SftpException {

		// List source directory structure.
		Collection<ChannelSftp.LsEntry> fileAndFolderList = channelSftp.ls(path);

		// Iterate objects in the list to get file/folder names.
		for (ChannelSftp.LsEntry item : fileAndFolderList) {
			if (!item.getAttrs().isDir()) {
				channelSftp.rm(path + "/" + item.getFilename()); // Remove file.
			} else if (!(".".equals(item.getFilename()) || "..".equals(item.getFilename()))) { // If it is a subdir.
				try {
					// removing sub directory.
					channelSftp.rmdir(path + "/" + item.getFilename());
				} catch (Exception e) { // If subdir is not empty and error occurs.
					// Do lsFolderRemove on this subdir to enter it and clear its contents.
					recursiveFolderDelete(channelSftp, path + "/" + item.getFilename());
				}
			}
		}
		channelSftp.rmdir(path); // delete the parent directory after empty
	}

	private static void execute(Session session, String command) throws JSchException, IOException {
		execute(session, command, Duration.ofMinutes(1));
	}
	
	private static void execute(Session session, String command, Duration timeout) throws JSchException, IOException {

		ChannelExec exec = (ChannelExec) session.openChannel("exec");		
		exec.setCommand(command);

		exec.setInputStream(null);
		exec.setErrStream(System.err);

		InputStream in = exec.getInputStream();
		LocalTime startTime = LocalTime.now();
		LocalTime timeoutAt = startTime.plus(timeout);
		exec.connect();	
		
		while (!exec.isEOF() && (Duration.between(timeoutAt, LocalTime.now()).toMillis() < 0)) {
			while (in.available() > 0) {
				byte[] tmp = new byte[1024];
				int i = in.read(tmp, 0, 1024);
				if (i < 0)
					break;
				System.out.print(new String(tmp, 0, i));
			}
			try {
				Thread.sleep(1000);
			} catch (Exception ee) {
			}			
		}
//		String output = IOUtils.toString(in, Charset.defaultCharset());
//		System.out.print(output);
		
		int availableBytes = in.available();
		if (availableBytes > 0) {
			byte[] tmp = new byte[availableBytes];
			int i = in.read(tmp, 0, availableBytes);
			System.out.print(new String(tmp, 0, i));
		}
		System.out.println("exit-status: " + exec.getExitStatus());
				
//		byte[] tmp = new byte[1024];
//		while (true) {
//			while (in.available() > 0) {
//				int i = in.read(tmp, 0, 1024);
//				if (i < 0)
//					break;
//				System.out.print(new String(tmp, 0, i));
//			}
//			if (exec.isClosed()) {
//				if (in.available() > 0)
//					continue;
//				System.out.println("exit-status: " + exec.getExitStatus());
//				break;
//			}
//			try {
//				Thread.sleep(1000);
//			} catch (Exception ee) {
//			}
//		}
		exec.disconnect();
	}

	
	private static void execute(ChannelExec exec, String command) throws JSchException, IOException {
		
		exec.setCommand(command);

		exec.setErrStream(System.err);

		InputStream in = exec.getInputStream();
		exec.connect();	

		byte[] tmp = new byte[1024];
		while (true) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0)
					break;
				System.out.print(new String(tmp, 0, i));
			}
			if (exec.isClosed()) {
				if (in.available() > 0)
					continue;
				System.out.println("exit-status: " + exec.getExitStatus());
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (Exception ee) {
			}
		}
		exec.disconnect();
	}
	
	private static void executeCmd(Session session, String command) throws JSchException, IOException {
		Channel channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand(command);

//				boolean ptimestamp = true;
//
//				String remoteFile = null;
//				
//			    String command = "scp " + (ptimestamp ? "-p" :"") +" -t " + remoteFile;
//			      Channel channel=session.openChannel("exec");
//			      ((ChannelExec)channel).setCommand(command);
//
//			      // get I/O streams for remote scp
//			      OutputStream out = channel.getOutputStream();
//			      InputStream in = channel.getInputStream();
//
//			      channel.connect();

		// channel.setInputStream(System.in);
		channel.setInputStream(null);
		// channel.setOutputStream(System.out);

		// FileOutputStream fos=new FileOutputStream("/tmp/stderr");
		// ((ChannelExec)channel).setErrStream(fos);
		((ChannelExec) channel).setErrStream(System.err);

		InputStream in = channel.getInputStream();

		channel.connect();

		byte[] tmp = new byte[1024];
		while (true) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0)
					break;
				System.out.print(new String(tmp, 0, i));
			}
			if (channel.isClosed()) {
				if (in.available() > 0)
					continue;
				System.out.println("exit-status: " + channel.getExitStatus());
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (Exception ee) {
			}
		}
		channel.disconnect();
	}

	private static void notifyAboutDeployment(String env, String version) {

		String emailTo = System.getProperty("tm.email.to");
		if (emailTo == null || emailTo.isEmpty()) {
			System.out.println("tm.email.to is empty, not sending email notification.");
			return;
		}

		StringBuilder buf = new StringBuilder();
		buf.append("<b>Time:</b>");
		buf.append(LocalDateTime.now());
		buf.append("<br>");
		buf.append("<b>Environment:</b>");
		buf.append(env);
		buf.append("<br>");
		buf.append("<b>Deployed version:</b>");
		buf.append(version);
		buf.append("<br>");
		buf.append("<b>By user:</b>");
		buf.append(System.getProperty("user.name"));
		buf.append("<br>");
		buf.append("<b>Using computer:</b>");
		buf.append(getComputerName());
		buf.append("<br>");
		buf.append("<br><br>");
		buf.append("Sent by deployment code.");
		buf.append("<br>");

//	    System.out.println(buf.toString());    

		Properties prop = new Properties();
		prop.put("mail.smtp.auth", false);
		prop.put("mail.smtp.starttls.enable", "false");
		prop.put("mail.smtp.host", "mailgot.it.volvo.net");
		prop.put("mail.smtp.port", "25");

//        Session session = Session.getInstance(prop);
//	    	    
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress("testmanager@volvo.com"));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
//            message.setSubject("TestManager - Deployment notification: " + env + " version: " + version);
//
//            String msg = buf.toString();
//               
//            MimeBodyPart mimeBodyPart = new MimeBodyPart();
//            mimeBodyPart.setContent(msg, "text/html");
//
//            Multipart multipart = new MimeMultipart();
//            multipart.addBodyPart(mimeBodyPart);
//            message.setContent(multipart);
//
//            Transport.send(message);            
//        } catch (Exception e) {
//            e.printStackTrace();
//        }	    
	}

	public static void watchGitPhpFilesCopyToLocalOnChange() {

		try {
			watcher = FileSystems.getDefault().newWatchService();

//			Path path = Paths.get(System.getProperty("user.home"));
//			Path path = Paths.get(System.getProperty("user.home"));
			Path pathToWatch = CommonUtil.resolveDirWithFile("start_page_gtt.php").toPath();

			registerAllDirs(pathToWatch);
			System.out.println("Watching files at: " + pathToWatch);

//			pathToWatch.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
//					StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

			WatchKey key;
			while ((key = watcher.take()) != null) {
//				System.out.println("WatchedKey: " + key.

				for (WatchEvent<?> event : key.pollEvents()) {
					System.out.println("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
				}
				// Execute rsync
//				CommonUtil.executeCmd("rsync -ruthv --progress " + pathToWatch + " " + "/cygdrive/c/xampp/htdocs/pp2/");
				CommonUtil.executeCmd("rsync.bat -ruthv --progress " + "/cygdrive/c/Dev/EDB/SCM/pmd/pmd-php/" + " "
						+ "/cygdrive/c/Temp/pp2/");
				key.reset();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Register the given directory, and all its sub-directories, with the
	 * WatchService.
	 */
	private static void registerAllDirs(Path start) throws IOException {
		// register directory and sub-directories
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				registerPathForWatch(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	private static void registerPathForWatch(Path dir) throws IOException {
		WatchKey key = dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
				StandardWatchEventKinds.ENTRY_MODIFY);
//		if (trace) {
//			Path prev = keys.get(key);
//			if (prev == null) {
//				System.out.format("register: %s\n", dir);
//			} else {
//				if (!dir.equals(prev)) {
//					System.out.format("update: %s -> %s\n", prev, dir);
//				}
//			}
//		}
		keys.put(key, dir);
	}

	
	private static String getComputerName() {
		Map<String, String> env = System.getenv();
		if (env.containsKey("COMPUTERNAME"))
			return env.get("COMPUTERNAME");
		else if (env.containsKey("HOSTNAME"))
			return env.get("HOSTNAME");
		else if (env.containsKey("NODE_NAME"))
			return env.get("NODE_NAME");
		else
			return "Unknown Computer";
	}

	private static void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
