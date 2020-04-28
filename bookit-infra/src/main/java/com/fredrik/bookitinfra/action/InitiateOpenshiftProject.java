package com.fredrik.bookitinfra.action;


import com.fredrik.bookitinfra.Openshift;
import com.fredrik.bookitinfra.cmd.CommonUtil;

public final class InitiateOpenshiftProject {
    
	public InitiateOpenshiftProject(String env) {
		initOpenshiftProject(env);
	}

	public static void main(String[] args) {
		initOpenshiftProject(args[0]);
	}

	private static void initOpenshiftProject(String env) {

		Openshift oc = new Openshift(env);

		System.out.println(oc.toString());
		System.out.println(oc.getDocker().toString());	
		System.out.println("Should initiate project: " + oc.getProject() + " for env: " + env);
		
		oc.login();
	
		String createDockerPullSecretCmd = "oc secrets new-dockercfg nexus-docker --docker-server=mavenqa.got.volvo.net:18444 "+
		        "--docker-username=docker --docker-password=tcuser --docker-email=docker@volvo.com";
		CommonUtil.executeCmd(createDockerPullSecretCmd);

		String linkSecretCmd = "oc secrets link default nexus-docker --for=pull";
		CommonUtil.executeCmd(linkSecretCmd);
	}	
}
