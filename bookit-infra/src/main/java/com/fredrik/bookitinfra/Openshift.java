/*
 * Copyright 2009 Volvo Information Technology AB 
 * 
 * Licensed under the Volvo IT Corporate Source License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *      http://java.volvo.com/licenses/CORPORATE-SOURCE-LICENSE 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.fredrik.bookitinfra;

import java.io.File;
import java.util.List;

import com.fredrik.bookitinfra.cmd.CommonUtil;
import com.fredrik.bookitinfra.model.NetworkPolicy;

/**
 * Utility class for Dates.
 */
//CHECKSTYLE:OFF: checkstyle:magicnumber
public class Openshift {    

    
//	private String openshiftUrl 	= "ocp-admin.got.volvo.net:8443";
	private String openshiftUrl 	= "acps-admin-alpha.srv.volvo.com:8443";
	
    private String project        	= "edb-dev";
    private String projectProd      = "edb-prod";
    
    private String applicationName  = "pmd-backend";

	private String env 				= "";
	private String appInstanceName 	= applicationName;

	private String version;
	
	private final String ocUserName	= "cs-ws-s-edb-ci";
	private String ocPassword		= "";    

	Docker docker;
	
    public Openshift(String env) {        
		docker = new Docker();
    	ocPassword = System.getenv().get("EDB_CI_PASSWD");
		
		if ("prod".equalsIgnoreCase(env)) {
			project = projectProd;
		}
		
		// appInstanceName, Resolves the name for the Openshift instance
		// 
		this.env 				= env;
		this.appInstanceName 	= applicationName + "-" + env;
    }

//    public Openshift(String appname, String stage) {        
//		docker = new Docker();
//
//		if (appname == null || appname.isEmpty()) {
//			APPLICATION_NAME 	= stage;
//			ENV 			    = stage;
//			
//		} else {
//			APPLICATION_NAME 	= appname;
//			ENV 				= stage;			
//		}		
//    }

	public Docker getDocker() {
		return docker;
	}

	public String getProject() {
		return project;
	}
	
	public String getAppName() {
		return applicationName;
	}

	public String getAppInstance() {
		return appInstanceName;
	}

	public String getOCUsername() {
		return ocUserName;
	}

	public String getOCPassword() {
		return ocPassword;
	}

	public String getEnv() {
		return env;
	}

	public void setVersion(String aVersion) {
	    if (aVersion == null) {
	    	version = "latest";
	    } else {
	    	version = aVersion;
	    }
	}

	public String getVersion() {
		return version;
	}

	
    /**
     * Deployment of image and configuration
     * 
     * oc login --username=$OC_USERNAME --password=$OC_PASSWORD ocp-admin.got.volvo.net:8443 --insecure-skip-tls-verify
     *
     * oc project $OC_PROJECT
     */
	public void login() {
		
		String loginCmd = "oc login --username="+ getOCUsername() +" --password="+ getOCPassword() + " " + openshiftUrl + " --insecure-skip-tls-verify";
		Integer cmd = CommonUtil.executeCmd(loginCmd, "oc login --username=" + getOCUsername() + " " + openshiftUrl);

		if (cmd != 0) {
			System.out.println("Unable to login, exiting: " + cmd);
			System.exit(cmd);
		}
		
		String projectCmd = "oc project " + getProject();
		CommonUtil.executeCmd(projectCmd);
	}
	
	
	//	String ocImport = "oc import-image " + oc.getAppName() + ":" + version + " --from=" + oc.getDocker().getRepoName() + ":" + version + 
	//    " --confirm --insecure=true";
	//CommonUtil.executeCmd(ocImport);
	//
	//
	//
	public void importImageFromDockerRepoToAppImageStream(int sleepTimeAfterImport) {
		String importVersion = "oc tag " + getDocker().getRepoName() + ":" + getVersion() + " "+ getAppName() + ":" + getVersion();
		CommonUtil.executeCmd(importVersion);
		CommonUtil.sleep(sleepTimeAfterImport);
	}
	
	public void tagAppInstance(int sleepTimeAfterTagging) {
		String tagEnvVersion = "oc tag " + getAppName() + ":" + getVersion() + " "+ getAppName() + ":" + getEnv();
		CommonUtil.executeCmd(tagEnvVersion);
		CommonUtil.sleep(sleepTimeAfterTagging);
	}	
	
	public void createApplicationImageStreamIfNotExisting(String appName) {

		String existsCmd = "oc describe is/"+ appName;
		Integer exitCode = CommonUtil.executeCmd(existsCmd);

		// Image stream doesn't exists
		if (!Integer.valueOf(0).equals(exitCode)) {

			String projectCmd = "oc create imagestream " + appName;
			CommonUtil.executeCmd(projectCmd);
		}		
	}

	public void createApplicationInstanceIfNotExisting(String appInstanceName) {
		createApplicationInstanceIfNotExisting(appInstanceName, "");
	}
	
	public void createApplicationInstanceIfNotExisting(String appInstanceName, String labelForApp) {
		
//		String newApp = "oc new-app " + oc.getAppName() + ":" + oc.getEnv() + " --name="+ oc.getAppInstance();
//		CommonUtil.executeCmd(newApp);

		String existsCmd = "oc describe dc/"+ appInstanceName;
		Integer exitCode = CommonUtil.executeCmd(existsCmd);

		String applyLabels = "";
		if (!labelForApp.isEmpty()) {
			applyLabels = " -l " + labelForApp;
		}

		// Image stream doesn't exists
		if (!Integer.valueOf(0).equals(exitCode)) {

			String cmd = "oc new-app " + getAppName() + ":" + getEnv() + " --name="+ appInstanceName + applyLabels;
			CommonUtil.executeCmd(cmd);
		}		
	}
	
	public void pauseRollout() {
		// Will pause rollouts or deploys
		String ocPauseRollout = "oc rollout pause dc/" + getAppInstance();
		CommonUtil.executeCmd(ocPauseRollout);
	}

	
    // Skipping this, only hanging the build in Jenkins for some time
    // 
	//	String ocRollout = "oc rollout status --watch dc/" + oc.getAppName();
	//		CommonUtil.executeCmd(ocRollout);
	//
	//
	public void resumeRollout() {
		// Resume rollouts for dc
        String ocRolloutResume = "oc rollout resume dc/" + getAppInstance();
        CommonUtil.executeCmd(ocRolloutResume);
	}
	
	public void applyConfiguration() {
	
        List<String> configCommandsForEnv = AppConfig.getInstance().getConfigCommandsForEnv(this);
        for (String cfgCmd : configCommandsForEnv) {
            CommonUtil.executeCmd(cfgCmd);
        }		
	}

	public void exposeDefaultRouteIfNotExisting() {

		String existsCmd = "oc describe route/"+ appInstanceName;
		Integer exitCode = CommonUtil.executeCmd(existsCmd);
	
		// Image stream doesn't exists
		if (!Integer.valueOf(0).equals(exitCode)) {
			exposeDefaultRoute();
		}
	}
	
	public void exposeDefaultRoute() {
		String defineRouteCmd = "oc expose svc/" + getAppInstance();
        CommonUtil.executeCmd(defineRouteCmd);            
	}

	public void applyNetworkPolicy() {

		NetworkPolicy networkPolicy = new NetworkPolicy(project, getAppInstance(), 
				docker.getExposedProtocol() , docker.getExposedPortNr());
		String fileName = getAppInstance() + "-networkpolicy.yaml";
		File yamlFile = new File(fileName);
		CommonUtil.writeYamlForObject(yamlFile, networkPolicy);
		
		String applyNwPolicyCmd = "oc apply -f " + fileName;
		CommonUtil.executeCmd(applyNwPolicyCmd);                  

		yamlFile.delete();
	}

	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Openshift project  : " + project + "\n");
		buf.append("Openshift app name : " + applicationName + "\n");
		buf.append("App instance       : " + appInstanceName + "\n");
		buf.append("Env                : " + env + "\n");
		if ("true".equalsIgnoreCase(System.getProperty("debug"))) {
			buf.append("OC Passwd           : " + ocPassword + "\n");			
		}
		
		return buf.toString();
	}
	
    /**
     * Create secret for docker registry
     * 
     * oc secrets new-dockercfg nexus-docker --docker-server=mavenqa.got.volvo.net:18444 --docker-username=docker --docker-password=tcuser --docker-email=docker@volvo.com
     *
     *      
     * oc secrets new-dockercfg nexus-docker --docker-server=maven2.it.volvo.net:18471 --docker-username=cs-ws-s-edb-ci --docker-password=7MH-Ld%cJHGeIH7 --docker-email=docker@volvo.com
     * oc secrets link default nexus-docker --for=pull
     */

    /**
     * Creating a secret for database
     * oc secret new-basicauth capacity-db-secret --username=u_gtmgrq02_00 --password=4MmzOosd
     * 
     * oc set volume dc/dev --add -t secret --secret-name=capacity-db-secret --mount-path=/home/jboss/secrets/db
     */
    
    /**
     * Adding a user VCN-id to a given Openshift role, like admin and view
     * oc adm policy add-role-to-user admin a263647
     * oc adm policy add-role-to-user view tin2984
     * 
     */

	
}
