package com.fredrik.bookitinfra.action;

import java.io.File;
import java.time.LocalDateTime;

import com.fredrik.bookitinfra.AppConfig;
import com.fredrik.bookitinfra.Docker;
import com.fredrik.bookitinfra.cmd.CommonUtil;
import com.fredrik.bookitinfra.cmd.GitUtil;

public final class BuildDockerImage {

	// private static Logger log = LoggerFactory.getLogger(DBUtils.class);

	Docker docker = new Docker();

	public BuildDockerImage() {
		System.out.println(docker.toString());
	}

	public static void main(String args[]) {
		String version = null;
		if (args[0] != null && !args[0].trim().isEmpty()) {
			version = args[0];
		} else {
			version = System.getenv("VERSION");
		}
		if (version == null) {
			if (AppConfig.getInstance().getApplicationVersion() != null) {
				version = AppConfig.getInstance().getApplicationVersion();
			} else {
				version = "latest";
			}
		}

		new BuildDockerImage().buildImage(version);
	}

	public void buildImage(String version) {

		System.out.println("Building docker image version: " + version);

		File dockerFileDir = CommonUtil.resolveDirWithFile("Dockerfile");

		AppConfig.getInstance().getApplicationVersion();

		CommonUtil.executeCmd("docker info");

		if (!CommonUtil.runningWindows()) {
			
			CommonUtil.executeCmd(dockerFileDir, "chmod 777 target/*.jar");
			CommonUtil.executeCmd(dockerFileDir, "chmod 777 target/pmd-backend.jar");
			CommonUtil.executeCmd(dockerFileDir, "ls -la target");
		} else {
			dockerFileDir = dockerFileDir;
		}


		String build = "docker build --tag=" + docker.getTag() + " .";
		CommonUtil.executeCmd(dockerFileDir, build);

		String tag1 = "docker tag " + docker.getTag() + " " + docker.getRepoName();
		CommonUtil.executeCmd(tag1);

		String tag2 = "docker tag " + docker.getTag() + " " + docker.getRepoName() + ":" + version;
		CommonUtil.executeCmd(tag2);

		System.out.println("Ready building version: " + version);
	}

	public void pushImage(String version) {
		
		String login = "docker login --username=" + docker.getRepoUsername() + " --password=" + docker.getRepoPassword()
				+ " " + docker.getRepo();
		String loginOut = "docker login --username=" + docker.getRepoUsername() + " --password=******* "
				+ docker.getRepo();
		CommonUtil.executeCmd(login, loginOut);

		String push1 = "docker push " + docker.getRepoName();
		CommonUtil.executeCmd(push1);

		String push2 = "docker push " + docker.getRepoName() + ":" + version;
		CommonUtil.executeCmd(push2);

		System.out.println("Pushed image version: " + version);
		notifyAboutBuild(version);
	}
	
	private static void notifyAboutBuild(String version) {
		
		String emailTo = System.getProperty("email.to");
		if (emailTo == null || emailTo.isEmpty()) {
			System.out.println("email.property to was empty, not sending email notification.");
			return;
		}

		String gitLog = GitUtil.getGitLog("remotes/origin/develop", 2, 40);

		StringBuilder buf = new StringBuilder();
		buf.append("<b>New version built:</b>");
		buf.append(version);
		buf.append("<br>");
		buf.append("<b>Time:</b>");
		buf.append(LocalDateTime.now());
		buf.append("<br><br>");
		buf.append("<b>History from Git since last build:</b>");
		buf.append("<br>");
		buf.append(gitLog);
		buf.append("<br>");
		buf.append("Sent by build code.");
		buf.append("<br>");

//     System.out.println("Message to be emailed: " + buf.toString());    

//     Properties prop = new Properties();
//     prop.put("mail.smtp.auth", false);
//     prop.put("mail.smtp.starttls.enable", "false");
//     prop.put("mail.smtp.host", "mailgot.it.volvo.net");
//     prop.put("mail.smtp.port", "25");
//     
//     Session session = Session.getInstance(prop);
//             
//     try {
//         Message message = new MimeMessage(session);
//         message.setFrom(new InternetAddress("testmanager@volvo.com"));
//         message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
//         message.setSubject("TestManager - Build notification: version: " + version);
//
//         String msg = buf.toString();
//            
//         MimeBodyPart mimeBodyPart = new MimeBodyPart();
//         mimeBodyPart.setContent(msg, "text/html");
//
//         Multipart multipart = new MimeMultipart();
//         multipart.addBodyPart(mimeBodyPart);
//         message.setContent(multipart);
//
//         Transport.send(message);            
//     } catch (Exception e) {
//         e.printStackTrace();
//     }       
	}

}
