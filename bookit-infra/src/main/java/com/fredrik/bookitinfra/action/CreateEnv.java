package com.fredrik.bookitinfra.action;

import com.fredrik.bookitinfra.Openshift;
import com.fredrik.bookitinfra.cmd.CommonUtil;

public final class CreateEnv {
    
    
    public CreateEnv(String env, String version) {
    	createEnvironment(env, version);
    }

	public static void main(String args[]) {
		
        String version = null;
        if (args[1] != null && !args[1].trim().isEmpty()) {
            version = args[1];
        } else {
            version = System.getenv("VERSION");
        }

        createEnvironment(args[0], version);
	}
	
	private static void createEnvironment(String env, String version) {

		Openshift oc = new Openshift(env);

        if (version == null) {
            System.out.println("You need to give initial version that you want. Please specify with -Dswversion argument.");
            return;
        }
        
        oc.setVersion(version);
        oc.login();

        System.out.println(oc.toString());
        System.out.println(oc.getDocker().toString());
        System.out.println("Creating appInstance: " + oc.getAppInstance() + " using version: " + version);

        oc.createApplicationImageStreamIfNotExisting(oc.getAppName());

        oc.importImageFromDockerRepoToAppImageStream(20);

        oc.tagAppInstance(8);
                
		String newApp = "oc new-app " + oc.getAppName() + ":" + oc.getEnv() + " --name="+ oc.getAppInstance();
		CommonUtil.executeCmd(newApp);

		oc.pauseRollout();

		oc.applyConfiguration();

		// 
		// Create secrets only needed for env using Postgres DB and/or IBM MQ connectivity
		//
//        if (oc.getName().equals("test") || oc.getName().equals("capacity") || oc.getName().equals("qa") || oc.getName().equals("prod")) {
//            createSecret("db", oc.getName());
//            createSecret("jms", oc.getName());
//        }		
		
//		# oc volume dc/$OC_NAME --add -t pvc --name=$OC_NAME-files --mount-path=/files --claim-size=20G --claim-name=$OC_NAME-files --overwrite
//		String volumeCmd = "oc volume dc/"+ oc.getName() +" --add -t pvc --name="+ oc.getName() +"-files --mount-path=/home/jboss/files --claim-size=30G "+
//			"--claim-name="+ oc.getName() +"-files --claim-mode=rwm --overwrite";
//		CommonUtil.executeCmd(volumeCmd);

		/* 
		 * Defining services and routes 
		 */
        oc.exposeDefaultRoute();

        
		// Only have shorter DNS:es for dev, test and qa.
		// 

        // http:// URLs
        // 
//        if (oc.getName().equals("dev") || oc.getName().equals("test") || oc.getName().equals("capacity") || oc.getName().equals("qa")) {
//		    
//            String exposeDnsCmd = "oc expose service "+ oc.getName() +" --name=\"exposed-" + oc.getName() +"-to-dns\" --port=8080 "+
//		            "--hostname=\"testmanager-"+ oc.getName() +".got.volvo.net\"";
//		    CommonUtil.executeCmd(exposeDnsCmd);
//		}

        // https:// supported URLs
        // 
//        if (oc.getName().equals("dev") || oc.getName().equals("test") || oc.getName().equals("capacity")) {
//            
//            String exposeDnsCmd = "oc create route edge https-route-" + oc.getName() + " --service=" + oc.getName() + " " +
//                    "--hostname=\"testmanager-"+ oc.getName() +".got.volvo.net\" --path=/ --port=8080 --insecure-policy=Allow " +
//                    "--cert=C:\\JavaDev\\tools\\cert\\nonprod.crt --key=C:\\JavaDev\\tools\\cert\\nonprod.key";
//            CommonUtil.executeCmd(exposeDnsCmd);
//        }
        // QA
//        if (oc.getName().equals("qa")) {
//            
//            String exposeDnsCmd = "oc create route edge https-route-" + oc.getName() + " --service=" + oc.getName() + " " +
//                    "--hostname=\"testmanager-"+ oc.getName() +".got.volvo.net\" --path=/ --port=8080 --insecure-policy=None " +
//                    "--cert=C:\\JavaDev\\tools\\cert\\nonprod.crt --key=C:\\JavaDev\\tools\\cert\\nonprod.key";
//            CommonUtil.executeCmd(exposeDnsCmd);
//        }
        // Prod
//        if (oc.getName().equals("prod")) {
//            String exposeHttpsCmd = "oc create route edge https-route-" + oc.getName() + " --service=" + oc.getName() + " " + 
//                    "--hostname=\"testmanager.got.volvo.net\" --path=/ --port=8080 --insecure-policy=None " +
//                    "--cert=C:\\JavaDev\\tools\\cert\\prod.crt --key=C:\\JavaDev\\tools\\cert\\prod.key";
//            CommonUtil.executeCmd(exposeHttpsCmd);
//        }

        
        oc.resumeRollout();        
	}

    private static void createSecret(String kind, String stage) {
        /**
         * 
         * * oc secret new-basicauth capacity-db-secret --username=u_gtmgrq02_00 --password=4MmzOosd
         * 
         * oc set volume dc/dev --add -t secret --secret-name=capacity-db-secret --mount-path=/home/jboss/secrets/db
         */
        
        String createSecretCmd = "oc secret new-basicauth "+ stage + "-" + kind + "-secret --username="+ getUsernameFor(kind, stage) +" --password=" + getPasswordFor(kind, stage);
        CommonUtil.executeCmd(createSecretCmd);

        String labelCmd = "oc label secret " + stage + "-" + kind + "-secret app=" + stage;
        CommonUtil.executeCmd(labelCmd);

        String volumeCmd = "oc volume dc/"+ stage + " --add -t secret --secret-name=" + stage + "-" + kind + "-secret --mount-path=/home/jboss/secrets/"+ kind+ "";
        CommonUtil.executeCmd(volumeCmd);
        
    }

    private static String getPasswordFor(String kind, String stage) {
        // Example TEST_DB_PASSWORD
        String var = "" + stage.toUpperCase() + "_" + kind.toUpperCase() + "_PASSWORD";
        System.out.println("Reading value from env variable: " + var);
        return System.getenv(var);        
    }

    private static String getUsernameFor(String kind, String stage) {
        // Example TEST_DB_PASSWORD
        String var = "" + stage.toUpperCase() + "_" + kind.toUpperCase() + "_USERNAME";
        System.out.println("Reading value from env variable: " + var);
        return System.getenv(var);        
    }	
}
