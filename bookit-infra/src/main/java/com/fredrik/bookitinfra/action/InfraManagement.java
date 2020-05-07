package com.fredrik.bookitinfra.action;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;
import com.fredrik.bookitinfra.AppConfig;
import com.fredrik.bookitinfra.model.NetworkPolicy;

public final class InfraManagement {

	private InfraManagement() {
	}

	public static void main(String[] args) {

//		List<String> asList = Arrays.asList(args);
//		for (String string : asList) {
//			System.out.println("Arg: " + string);
//		}

		// Resolve input parameters
		String env = resolveEnvironment();
		String version = resolveVersion();
		String passwd = resolvePassword();

		// Print config
		System.out.println("Env: " + env);
		System.out.println("Version: " + version);

		resolveActions();

		// Test stuff 
		if (!Objects.isNull(System.getProperty("testing"))) {
			testing();
		}

		if (!Objects.isNull(System.getProperty("versions"))) {
			AppInstanceVersions.getAppInstanceVersions();
		}

		// Deploy artifact stuff early
		if (!Objects.isNull(System.getProperty("deploy-to-infra"))) {
			DeployArtifact.deployToInfra(env, passwd);
		}

		
		// Docker stuff first
		if (!Objects.isNull(System.getProperty("build-docker-image"))) {
			new BuildDockerImage().buildImage(version);
		}
		if (!Objects.isNull(System.getProperty("push-docker-image"))) {
			new BuildDockerImage().pushImage(version);
		}

		// Openshift
		if (!Objects.isNull(System.getProperty("init-openshift-project"))) {
			new InitiateOpenshiftProject(env);
		}
		if (!Objects.isNull(System.getProperty("create-openshift-env"))) {
			new CreateEnv(env, version);
		}
		if (!Objects.isNull(System.getProperty("deploy-to-openshift-env"))) {
			new DeployToOpenshift(env, version, passwd);
		}
		if (!Objects.isNull(System.getProperty("delete-openshift-env"))) {
			new DeleteEnv(env, version);
		}

	}

	private static void testing() {

		ObjectMapper mapper = new ObjectMapper(new YAMLFactory().enable(Feature.MINIMIZE_QUOTES));
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		
		NetworkPolicy networkPolicy = new NetworkPolicy("edb-dev", "pmd-backend-test", "TCP", 8888);
		
		try {
			mapper.writeValue(new File("test.yaml"), networkPolicy);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static void resolveActions() {
		
		if (Objects.nonNull(System.getProperty("cicd"))) {
			System.setProperty("build-docker-image", "");
			System.setProperty("push-docker-image", "");
			System.setProperty("deploy-to-openshift-env", "");
		}
		if (Objects.nonNull(System.getProperty("docker"))) {
			System.setProperty("build-docker-image", "");
			System.setProperty("push-docker-image", "");
		}
		if (Objects.nonNull(System.getProperty("push-image"))) {
			System.setProperty("push-docker-image", "");
		}

		if (Objects.nonNull(System.getProperty("build-docker-image"))) {
		
		}

		if (Objects.nonNull(System.getProperty("create-env"))) {
			System.setProperty("create-openshift-env", "");
		}

		if (Objects.nonNull(System.getProperty("deploy"))) {
			System.setProperty("deploy-to-openshift-env", "");
		}
		if (!Objects.isNull(System.getProperty("deploy-to-openshift"))) {
			System.setProperty("deploy-to-openshift-env", "");
		}
		
		if (!Objects.isNull(System.getProperty("delete-env"))) {
			System.setProperty("delete-openshift-env", "");
		}

	}

	private static String resolveEnvironment() {
		String propEnv = System.getProperty("env");		
		if (propEnv != null) {
			return propEnv;
		}		
		String appVersion = AppConfig.getInstance().getFirstInfrastructureEnv();
		return appVersion;
	}

	private static String resolvePassword() {
		String envPasswd = System.getenv("PASSWD");
		if (envPasswd != null) {
			return envPasswd;
		}
		String propPasswd = System.getProperty("passwd");		
		if (propPasswd != null) {
			return propPasswd;
		}		
		return null;
	}

	private static String resolveVersion() {
		String envVersion = System.getenv("VERSION");
		if (envVersion != null) {
			return envVersion;
		}
		String propVersion = System.getProperty("swversion");		
		if (propVersion != null) {
			return propVersion;
		}		
		String appVersion = AppConfig.getInstance().getApplicationVersion();
		if (appVersion != null) {
			return appVersion;
		}
		return "latest";
	}
}
