package com.fredrik.bookit.support;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;
import com.google.common.collect.Lists;

public class OpenAPIToJsonSupport {

	private OpenAPIToJsonSupport() {
	}

	public static void main(String[] args) {

		System.out.println("Exeuting: OpenAPIToJsonSupport");

//		System.out.println("sourceRoot: " +  System.getProperty("sourceRoot"));
//		System.out.println("exec, work dir: " +  System.getProperty("project.basedir"));
//		System.out.println("Bld dir: " +  System.getProperty("project.build.directory"));
		System.out.println("Base dir: " +  System.getProperty("project.basedir"));		
		
		File file = new File(System.getProperty("project.basedir"));
//		System.out.println("Path: " + file.getAbsolutePath());

		addJsonIdentityInfoToFile(new File(System.getProperty("project.basedir"), 
				"./target/generated-sources/openapi/src/main/java/com/fredrik/bookit/web/rest/model/ItemDTO.java"), "id");
		addJsonIdentityInfoToFile(new File(System.getProperty("project.basedir"), 
				"./target/generated-sources/openapi/src/main/java/com/fredrik/bookit/web/rest/model/ProjectDTO.java"), "id");
		
	}

	private static void addJsonIdentityInfoToFile(File file, String identifier) {
	
		boolean exists = file.exists();
		
		System.out.println(file.getName()+ ", exists: " + exists + ", " + file.getAbsoluteFile());
		
		try {
			List<String> toLines = Lists.newArrayList();
			List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
			
			boolean foundImport = false;
			for (String line : lines) {

				// Add imports
				if (!foundImport && line.contains("import")) {
					toLines.add("import com.fasterxml.jackson.annotation.JsonIdentityInfo;");
					toLines.add("import com.fasterxml.jackson.annotation.ObjectIdGenerators;");
					toLines.add("import com.voodoodyne.jackson.jsog.JSOGGenerator;");
					foundImport = true;
				}				
				// Add JsonIdentityInfo
				if (line.contains("public class")) {
//					toLines.add("@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property=\"" + identifier + "\")");
					toLines.add("@JsonIdentityInfo(generator=JSOGGenerator.class)");
				}
				toLines.add(line);
			}
			Files.write(file.toPath(), toLines, StandardCharsets.UTF_8);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void testing() {

		ObjectMapper mapper = new ObjectMapper(new YAMLFactory().enable(Feature.MINIMIZE_QUOTES));
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		
//		NetworkPolicy networkPolicy = new NetworkPolicy("edb-dev", "pmd-backend-test", "TCP", 8888);
//		
//		try {
//			mapper.writeValue(new File("test.yaml"), networkPolicy);
//		
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}
}
