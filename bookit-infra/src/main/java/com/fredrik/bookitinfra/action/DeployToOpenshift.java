package com.fredrik.bookitinfra.action;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;

import com.fredrik.bookitinfra.AppConfig;
import com.fredrik.bookitinfra.Openshift;

public final class DeployToOpenshift {
    
//    private static Logger LOG = LoggerFactory.getLogger(DeployToOpenshift.class);
    
    public DeployToOpenshift(String env, String version, String passwd) {
        deployToOpenshift(env, version, passwd);
    }

	public static void main(String args[]) {		
		String env = null;
		if (args[0] != null ) {
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

		deployToOpenshift(env, version, passwd);
	}
		
	private static void deployToOpenshift(String env, String version, String passwd) {

        if (env.equals("prod")) {
            String encodeToString = Base64.getEncoder().encodeToString(passwd.getBytes());
                        
            String encodedWantedPasswd = "dG00ZXZlcg==";            
            if (!encodeToString.equals(encodedWantedPasswd)) {
                System.out.println("You try to deploy to prod and need correct password to do so, please try again.");
                return;
            } else {
                System.out.println("Correct password to deploy to prod, fine. Moving on.");
            }
        }
		
        Openshift oc = new Openshift(env);
        oc.setVersion(version);
        System.out.println(oc.toString());
        System.out.println(oc.getDocker().toString());
        
        oc.login();

		System.out.println("Deploying for env: " + oc.getEnv());
		System.out.println("Deploying version: " + oc.getVersion());
		System.out.println("***************************************************\n");
        
		oc.createApplicationImageStreamIfNotExisting(oc.getAppName());
		oc.importImageFromDockerRepoToAppImageStream(20);
		oc.tagAppInstance(8);
		
		oc.createApplicationInstanceIfNotExisting(oc.getAppInstance());
		
		oc.pauseRollout();
		oc.applyConfiguration();	
		oc.resumeRollout();
		
		/* 
		 * Defining services and routes 
		 */
        oc.exposeDefaultRouteIfNotExisting();
	
        oc.applyNetworkPolicy();
        
		notifyAboutDeployment(oc.getEnv(), oc.getVersion());
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
