package com.fredrik.bookitinfra.cmd;

public interface DeploymentConfig {

    String getCommand(String appInstanceName, String env);
    
}
