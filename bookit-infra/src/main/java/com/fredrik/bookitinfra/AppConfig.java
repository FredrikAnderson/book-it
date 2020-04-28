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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fredrik.bookitinfra.cmd.CpuResources;
import com.fredrik.bookitinfra.cmd.DeploymentConfig;
import com.fredrik.bookitinfra.cmd.DeploymentConfigList;
import com.fredrik.bookitinfra.cmd.DeploymentStrategy;
import com.fredrik.bookitinfra.cmd.Environment;
import com.fredrik.bookitinfra.cmd.EnvironmentVariable;
import com.fredrik.bookitinfra.cmd.MemoryResources;
import com.fredrik.bookitinfra.cmd.Probe;

/**
 * Utility class for Dates.
 */
public class AppConfig {    
    

	String firstInfrastructureEnv = "";
    String applicationVersion = "";     
    
    private static AppConfig myInstance = null;

    Map<String, DeploymentConfig> configs = new HashMap<String, DeploymentConfig>();
    Map<String, DeploymentConfig> overridden_cfgs = new HashMap<String, DeploymentConfig>();
    
    private AppConfig() {        

    	/* Application configuration should be done from here */
    	
    	firstInfrastructureEnv = "test";
    	
        MemoryResources memory = new MemoryResources(500, 2000);    // request and max, MB
        CpuResources cpu = new CpuResources(1000, 2000);            // request and max, millicore
        DeploymentStrategy depStrategy = new DeploymentStrategy("Recreate");
        Probe readinessProbe = new Probe("readiness", "http://:8888/actuator/health", 20, 600, 30);
        Probe livenessProbe = new Probe("liveness", "http://:8888/actuator/health", 60, 60, 300);
//        EnvironmentVariable env1 = new EnvironmentVariable("ENABLE_ACCESS_LOG", "true");
//        EnvironmentVariable env2 = new EnvironmentVariable("JAVA_MAX_MEM_RATIO", "97");
        EnvironmentVariable env3 = new EnvironmentVariable("SPRING_PROFILES_ACTIVE", "_stage_");
        EnvironmentVariable env4 = new EnvironmentVariable("JAVA_OPTS_APPEND", 
                                                           "-Djava.awt.headless=true ");
        Environment envVars = new Environment( env3, env4 );

        
        /* app config ends here */
        
        applicationVersion = resolveVersion();
        
		// Default configuration
		// 
        configs.put("memory", memory);
        configs.put("cpu", cpu);
        configs.put("deploymentStrategy", depStrategy);
        configs.put("livenessProbe", livenessProbe);
        configs.put("readinessProbe", readinessProbe);
        configs.put("environmentVars", envVars);

		
		
		// Overridden configs
        // Dev
//        MemoryResources memoryDev = new MemoryResources(500, 2400);
//        overridden_cfgs.put("memory-dev", memoryDev);
//        CpuResources cpuDev = new CpuResources(100, 1000);
//        overridden_cfgs.put("cpu-dev", cpuDev);
//        
//        // Dev2
//        MemoryResources memoryDev2 = new MemoryResources(500, 2400);
//        overridden_cfgs.put("memory-dev2", memoryDev2);
//        CpuResources cpuDev2 = new CpuResources(100, 500);
//        overridden_cfgs.put("cpu-dev2", cpuDev2);

    }
        
    private String resolveVersion() {
    	applicationVersion = "1.0-SNAPSHOT";
//    	Properties properties = CommonUtil.loadPropertiesForModuleWithDockerfile();
//
//		String versionFormat = properties.getProperty("version.format");
//		String[] splitStrings = versionFormat.split(";");
//
//		for (String string : splitStrings) {
//			applicationVersion += properties.getProperty(string) + ".";
//		}
//		applicationVersion = applicationVersion.substring(0, applicationVersion.length() - 1);

		return applicationVersion;
	}

	public static AppConfig getInstance() {
        if (myInstance == null) {
            myInstance = new AppConfig();
        }
        return myInstance;
    }

	public String getFirstInfrastructureEnv() {
		return firstInfrastructureEnv;
	}
	
	public String getApplicationVersion() {
		return applicationVersion;
	}
	
    public List<String> getConfigCommandsForEnv(Openshift os) {
        List<String> toret = new ArrayList<>();

        for (String config : configs.keySet()) {
            List<DeploymentConfig> depCfg = getConfigForEnv(config, os.getEnv());
            for (DeploymentConfig deploymentConfig : depCfg) {                
                toret.add(deploymentConfig.getCommand(os.getAppInstance(), os.getEnv()));
            }
        }        
        return toret;
    }
    
	private List<DeploymentConfig> getConfigForEnv(String config, String stage) {	    
	    List<DeploymentConfig> toret = new ArrayList<DeploymentConfig>();
	    DeploymentConfig deploymentConfig      	= configs.get(config);
	    DeploymentConfig deploymentConfigEnv  = overridden_cfgs.get(config + "-" + stage);
	    
	    // If override config exists then take it
	    if (deploymentConfigEnv != null) {
	        deploymentConfig = deploymentConfigEnv;
	    }
	    if (deploymentConfig instanceof DeploymentConfigList) {	        
	        DeploymentConfigList deploymentConfigList = (DeploymentConfigList) deploymentConfig;
	        toret.addAll(deploymentConfigList.getConfigList(stage));
	    } else {
	        toret.add(deploymentConfig);
	    }	    
        return toret;
    }

    public String toString() {
        
        for (String key : configs.keySet()) {
            System.out.println(configs.get(key).toString());                        
        }
        
        System.out.println();
        
	    return null;
	}
	
}
