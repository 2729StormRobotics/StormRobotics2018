# StormRobotics2018

2018 development code for FIRST Power Up

[![Build Status](https://travis-ci.org/2729StormRobotics/StormRobotics2018.svg?branch=master)](https://travis-ci.org/2729StormRobotics/StormRobotics2018)
[![Coverity Scan](https://scan.coverity.com/projects/15040/badge.svg?flat=1)](https://scan.coverity.com/projects/2729stormrobotics-stormrobotics2018)

## Cloning

This repository only contains the source code and relevant java packages.  Everything else must be built using gradle according to the following commands:

- ```gradlew idea``` will generate IDE files for IntelliJ IDEA (java)  
- ```gradlew eclipse``` will generate IDE files for Eclipse (java)
- It is possible you will need to run ```sudo chmod +x gradlew``` if permission is denied

## Building and Deploying

- ```gradlew build``` will build your Robot Code  
- ```gradlew deploy``` will build and deploy your code.
