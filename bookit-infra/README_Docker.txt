
Setup docker locally by following:
URL to JVS Confluence
https://confluence.it.volvo.net/display/JVS/Docker+Commands+and+Dockerfile%27s
https://confluence.it.volvo.net/display/JVS/Nexus+Docker+Registry


Build process:

docker build --tag=testmanager/testmanager .

Win 7:
docker run --network="host" -e "SPRING_PROFILES_ACTIVE=test" testmanager/testmanager 

Win 10:
docker run -e "SPRING_PROFILES_ACTIVE=test" testmanager/testmanager

Access it using URL <Docker IP>:8080/tm-uiservice/api/v1/test, most commingly the IP address is 192.168.99.100.


Push images to Docker repo
=========================================
docker login mavenqa.got.volvo.net:18444

docker images

docker tag testmanager/testmanager mavenqa.got.volvo.net:18444/testmanager/testmanager:latest

docker push mavenqa.got.volvo.net:18444/testmanager/testmanager:latest



Start container in Openshift
=====================================
oc login https://ocp-admin.got.volvo.net:8443

oc project drsjava-training

oc new-app --docker-image=mavenqa.got.volvo.net:18443/testmanager/testmanager:latest --insecure-registry=true



Scratchpad

#!/bin/sh


cd 7_implementation/tm/tm-servers
chmod 777 cli-commands.txt
ls -la

chmod -R 777 target/wars
ls -la target/wars

docker build --tag=tm/testmanager .
docker tag tm/testmanager mavenqa.got.volvo.net:18444/tm/testmanager:latest

docker login --username=cs-ws-s-docker --password=N9K7b9J7 mavenqa.got.volvo.net:18444
docker push mavenqa.got.volvo.net:18444/tm/testmanager:latest

oc login --username=cs-ws-s-docker --password=N9K7b9J7 ocp-admin.got.volvo.net:8443 --insecure-skip-tls-verify

oc project drsjava-training

# Remove older app definition
oc delete all -l app=tm-test -n drsjava-training

oc new-app --docker-image=mavenqa.got.volvo.net:18444/tm/testmanager:latest --name=tm-test --env SPRING_PROFILES_ACTIVE=test --insecure-registry=true
oc expose service tm-test



#To test Jenkins slave with docker/oc
# Docker
#
#docker info
#docker images

pwd
whoami
groups

ls -la

cd 7_implementation/tm/tm-servers
ls -la
chmod 777 cli-commands.txt
ls -la
chmod -R 777 target/wars
ls -la target/wars

docker build --tag=tm/testmanager .

#docker images

#docker login --username=cs-ws-s-docker --password=N9K7b9J7 mavenqa.got.volvo.net:18443
docker login --username=cs-ws-s-docker --password=N9K7b9J7 mavenqa.got.volvo.net:18444

docker tag tm/testmanager mavenqa.got.volvo.net:18444/tm/testmanager:latest

docker push mavenqa.got.volvo.net:18444/tm/testmanager:latest


#
#oc login --username=cs-ws-s-docker --password=N9K7b9J7 ocp-admin.got.volvo.net:8443 --insecure-skip-tls-verify
# 
#oc whoami -t > ./token.txt
#
##token=$(<token.txt)
#docker login --help
#
#docker login -u cs-ws-s-docker -p $token docker-registry-default.ocp.got.volvo.net
#
#oc login https://ocp-admin.got.volvo.net:8443
oc login --username=cs-ws-s-docker --password=N9K7b9J7 ocp-admin.got.volvo.net:8443 --insecure-skip-tls-verify

oc project drsjava-training

# Remove older app definition
oc delete all -l app=tm-test -n drsjava-training
# oc delete dc tm-test -n drsjava-training
# oc delete service tm-test -n drsjava-training


oc new-app --docker-image=mavenqa.got.volvo.net:18444/tm/testmanager:latest --name=tm-test --env SPRING_PROFILES_ACTIVE=test --insecure-registry=true
oc expose service tm-test
