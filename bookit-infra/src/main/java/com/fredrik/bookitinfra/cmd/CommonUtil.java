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
package com.fredrik.bookitinfra.cmd;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;
import com.fredrik.bookitinfra.AppConfig;

/**
 * Utility class for Dates.
 */
//CHECKSTYLE:OFF: checkstyle:magicnumber
public class CommonUtil {    
    
    // private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);
        

	public static void main(String args[]) {
		String appVersion = AppConfig.getInstance().getApplicationVersion();		
		System.out.println("App version is: " + appVersion);
	}
	
    public CommonUtil() {    
    }

    public static Integer executeCmd(String cmd) {
        return executeCmd(null, cmd, null);
    }

    public static Integer executeCmd(String cmd, String output) {
        return executeCmd(null, cmd, output);
    }

    public static Integer executeCmd(File dir, String cmd) {
        return executeCmd(dir, cmd, null);
    }

	public static Integer executeCmd(File dir, String cmd, String output) {
		Integer exitCode = null;
	    if (output == null) {
	        System.out.println("Executing: " + cmd);
	    } else {
            System.out.println("Executing: " + output);
	    }


	    boolean executeCmd = true;	    
	    if (executeCmd) {
//            File adir = new File(".");
	    	if (dir != null) {
	    		System.out.println("Current dir: " + dir.getAbsolutePath());
	    	}
	    	
    		List<String> list = new ArrayList<String>(Arrays.asList(cmd.split(" ")));		
    		try {
    			ProcessBuilder pb = new ProcessBuilder(list);
    			if (dir != null) {
    			    pb.directory(dir);
    			}			
    			pb.inheritIO();
    			
    			Process proc = pb.start();
    			exitCode = proc.waitFor();
    		
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
	    }
        
		System.out.println();
		
		return exitCode;
	}

    public static boolean runningWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    
    public static void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {
				e.printStackTrace();
		}
	}	

    private static File findFilePattern(String path, String filePattern) {    	
    	File toret = null;
    	File file = new File(path, filePattern);
    	int nrOfTries = 8;
    	
    	while (!file.exists() && nrOfTries > 0) {
//    		System.out.println("Try: " + nrOfTries + ", file: " + file.toString());
    		nrOfTries--;
    		
        	filePattern = "../" + filePattern;
    		file = new File(filePattern);    		
		}
    	if (file.exists()) {
    		toret = file;
    	}

//    	System.out.println("Found file: " + file);
    	return toret;
    }    
   
	public static File resolveDirWithFile(String file) {

		String path = CommonUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.substring(1);
		String rootPath =  path + ".." + File.separator + ".." + File.separator + ".." + File.separator;
		
		File dirForFile = CommonUtil.searchDirWithFile(rootPath, file);
//		System.out.println("FileDir: " + dirForFile);

		return dirForFile;
	}

    
	public static File searchDirWithFile(String path, String file) {
		File toret = null;
		List<Path> list = null;
		try {
			list = Files.walk(Paths.get(path))
				.filter(Files::isRegularFile)
				.filter(ff -> ff.toString().contains(file))
				.collect(Collectors.toList());
				
			toret = list.get(0).toFile().getParentFile().getCanonicalFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return toret;
	}

    public static Properties loadPropertiesForModuleWithDockerfile() {
		File dockerFileDir = resolveDirWithFile("Dockerfile");
    	System.out.println("Loading properties from dir root: " + dockerFileDir.getPath());
		
    	Properties toret = new Properties();    	
    	File file;
		file = findFilePattern(dockerFileDir.getPath(), "/target/classes/application.properties");
		toret = loadPropertiesFromFile(toret, file);

		file = findFilePattern(dockerFileDir.getPath(), "/target/classes/git.properties");
		toret = loadPropertiesFromFile(toret, file);

		file = findFilePattern(dockerFileDir.getPath(), "/target/classes/META-INF/build-info.properties");
		toret = loadPropertiesFromFile(toret, file);

//    	toret.list(System.out);

    	return toret;
    }

    public static Properties loadPropertiesFromFile(Properties props, File file) {
		try {
    
			if (file != null && file.exists()) {
				System.out.println("Loading properties from file: " + file.getName());
				FileReader fileReader;
					fileReader = new FileReader(file);
		    	props.load(fileReader);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return props;
    }

	public static void writeYamlForObject(File yamlFile, Object object) {

		ObjectMapper mapper = new ObjectMapper(new YAMLFactory().enable(Feature.MINIMIZE_QUOTES));
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			
		try {
			mapper.writeValue(yamlFile, object);
		
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
    
}
