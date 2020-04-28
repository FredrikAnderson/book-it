package com.fredrik.bookitinfra.cmd;

import java.util.List;

public interface DeploymentConfigList {

    List<DeploymentConfig> getConfigList(String env);
    
}
