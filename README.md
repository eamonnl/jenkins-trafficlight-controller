Jenkins Build Status Trafficlight Controller
===============================

Controlls a Cleware USB traffic light based on build status of a set of monitored jobs your Jenkins Continuous Integration Server.

## Hardware

You can buy the USB traffic light in this shop
 - http://www.cleware-shop.de
 
This application can run from a Raspberry PI

## Software

To control the USB device from your command line, you need the software from Cleware:
 - http://www.cleware.net/download.html

For Linux use this link:
 - http://www.vanheusden.com/clewarecontrol/
 
 
## Compiling
 
This maven module packages an executable jar. Compile it with `mvn package`.
 
 
## Executing
 
The jar is executed using `java -jar jenkins-trafficlight.jar`. I run the application on an Raspberry PI and demonise it using `nohup`. For example:
 
    $nohup java -jar jenkins-trafficlight.jar 1>/dev/null 2>&1 &

 
## Configuration
 
The applciation expects to find a file named `jenkins.properties` in the same folder as the executable jar. For example:
 
    ./ - where you installed the program
     |__ jenkins-trafficlight.jar
     |__ jenkins.properties
 
This file should contain the following properties.
 
    jenkins.url=https://jenkins.build-cluster.company.com/
    jenkins.username=eamonnlinehan
    jenkins.apitoken=dc97c72d0d6ac5f81
    jenkins.jobs=Nightly Build,Integration Tests

