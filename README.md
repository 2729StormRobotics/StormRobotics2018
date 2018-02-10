# StormRobotics2018

2018 development code for FIRST Power Up

[![Build Status](https://travis-ci.org/2729StormRobotics/StormRobotics2018.svg?branch=master)](https://travis-ci.org/2729StormRobotics/StormRobotics2018)

[![Coverity Scan](https://scan.coverity.com/projects/15040/badge.svg?flat=1)](https://scan.coverity.com/projects/2729stormrobotics-stormrobotics2018)

[![SonarCloud Status](https://sonarcloud.io/api/project_badges/measure?project=StormRobotics2018&metric=alert_status)](https://sonarcloud.io/dashboard?id=StormRobotics2018)
[![SonarCloud Maintainability](https://sonarcloud.io/api/project_badges/measure?project=StormRobotics2018&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=StormRobotics2018)
[![SonarCloud Security](https://sonarcloud.io/api/project_badges/measure?project=StormRobotics2018&metric=security_rating)](https://sonarcloud.io/dashboard?id=StormRobotics2018)
[![SonarCloud Reliability](https://sonarcloud.io/api/project_badges/measure?project=StormRobotics2018&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=StormRobotics2018)
[![SonarCloud Bugs](https://sonarcloud.io/api/project_badges/measure?project=StormRobotics2018&metric=bugs)](https://sonarcloud.io/dashboard?id=StormRobotics2018)
[![SonarCloud Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=StormRobotics2018&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=StormRobotics2018)
[![SonarCloud Code Smells](https://sonarcloud.io/api/project_badges/measure?project=StormRobotics2018&metric=code_smells)](https://sonarcloud.io/dashboard?id=StormRobotics2018)

## Cloning

This repository only contains the source code and relevant java packages.  Everything else must be built using gradle according to the following commands:

- ```gradlew idea``` will generate IDE files for IntelliJ IDEA (java)  
- ```gradlew eclipse``` will generate IDE files for Eclipse (java)
- It is possible you will need to run ```sudo chmod +x gradlew``` if permission is denied

## Building and Deploying

- ```gradlew build``` will build your robot Code  
- ```gradlew deploy``` will build and deploy your code.
