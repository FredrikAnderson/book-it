# Alpine Linux with OpenJDK JRE
FROM mavenqa.got.volvo.net:18443/openjdk:8-jre-alpine

# copy WAR into image
COPY java-print-props.jar /java-print-props.jar

# run application with this command line 
CMD ["/usr/bin/java", "-cp", "/java-print-props.jar", "com.volvo.fredrik.JavaPrintProperties"]