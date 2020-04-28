package com.fredrik.bookitinfra.action;


import java.util.Base64;

import com.fredrik.bookitinfra.Openshift;
import com.fredrik.bookitinfra.cmd.CommonUtil;

public final class DeleteEnv {
    
    public DeleteEnv(String env, String passwd) {
		deleteOpenshiftEnv(env, passwd);
    }

	public static void main(String[] args) {
		String env = args[0];
		
        String passwd = null;     
        if (args[1] != null && !args[1].trim().isEmpty()) {
            passwd = args[1];
        } else {
            passwd = System.getenv("PASSWD");
        }

		deleteOpenshiftEnv(env, passwd);
	}
	
	private static void deleteOpenshiftEnv(String env, String passwd) {
		
		Openshift oc = new Openshift(env);
		
        if (passwd.equals("prod")) {
            String encodeToString = Base64.getEncoder().encodeToString(passwd.getBytes());
     
            String encodedWantedPasswd = "dG00ZXZlcg==";            
            if (!encodeToString.equals(encodedWantedPasswd)) {
                System.out.println("You try to delete prod and need correct password to do so, please try again.");
                return;
            } else {
                System.out.println("Correct password to delete prod, fine. Moving on.");
            }
        }
		
		System.out.println(oc.toString());
		System.out.println(oc.getDocker().toString());	
		System.out.println("Should delete env: " + env);
		oc.login();
	
/**	
#
# Remove older app definition
#
oc delete all -l app=$OC_NAME -n $OC_PROJECT
*/
		String deleteCmd = "oc delete all -l app=" + oc.getAppInstance() + " -n " + oc.getProject();
		CommonUtil.executeCmd(deleteCmd);

		String deleteSecretCmd = "oc delete secret -l app=" + oc.getAppInstance() + " -n " + oc.getProject();
        CommonUtil.executeCmd(deleteSecretCmd);
}	
}
