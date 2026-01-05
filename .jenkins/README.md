# Jenkins Pipeline Configuration

## Overview

This repository contains multiple Jenkinsfile configurations for the Retail Management System Spring Boot application. Choose the one that best fits your Jenkins setup.

## Available Jenkinsfiles

### 1. `Jenkinsfile` (Recommended for Pre-configured Tools)
- **Use Case**: When you have JDK-17 and Maven-3 pre-configured in Jenkins Global Tool Configuration
- **Features**: 
  - Uses Jenkins tool management
  - Automatic tool resolution
  - Best for production environments

### 2. `Jenkinsfile.standalone` (For System-installed Tools)
- **Use Case**: When Java 17 and Maven 3 are installed on the Jenkins agent
- **Features**:
  - Uses system-installed tools
  - Requires manual path configuration
  - Good for simple setups

### 3. `Jenkinsfile.docker` (Docker-based - Most Portable)
- **Use Case**: When using Docker agents in Jenkins
- **Features**:
  - Uses official Maven Docker image with Java 17
  - No tool configuration needed
  - Isolated build environment
  - Best for CI/CD environments

## Pipeline Stages

All Jenkinsfiles include the following stages:

1. **Checkout**: Checks out the source code from the repository
2. **Build**: Compiles the application using Maven (Java 17)
3. **Test**: Runs unit tests using JUnit 5 and Mockito
4. **Package**: Creates the JAR package

## Prerequisites

### For `Jenkinsfile` (Pre-configured Tools)

Configure the following tools in Jenkins:

1. **JDK-17**: 
   - Go to Jenkins → Manage Jenkins → Global Tool Configuration
   - Add JDK installation with name "JDK-17"
   - Specify Java 17 installation path or use automatic installer

2. **Maven-3**:
   - Go to Jenkins → Manage Jenkins → Global Tool Configuration
   - Add Maven installation with name "Maven-3"
   - Specify Maven 3 installation path or use automatic installer

### For `Jenkinsfile.standalone`

- Java 17 must be installed on the Jenkins agent
- Maven 3 must be installed on the Jenkins agent
- Update the `JAVA_HOME` path in the file if needed

### For `Jenkinsfile.docker`

- Docker must be installed and running on the Jenkins agent
- Jenkins must have Docker plugin installed
- No additional tool configuration needed

## Usage

1. Create a new Pipeline job in Jenkins
2. Select "Pipeline script from SCM"
3. Choose your SCM (Git, SVN, etc.)
4. Specify the repository URL
5. Set the script path to one of:
   - `Jenkinsfile` (for pre-configured tools)
   - `Jenkinsfile.standalone` (for system tools)
   - `Jenkinsfile.docker` (for Docker)
6. Save and run the pipeline

## Environment Variables

The pipelines set the following environment variables:
- `JAVA_HOME`: Points to JDK-17
- `MAVEN_HOME`: Points to Maven-3 (where applicable)
- `PATH`: Updated to include Maven and Java binaries
- `MAVEN_OPTS`: Maven JVM options (-Xmx1024m)

## Artifacts

The pipelines archive the following artifacts:
- JAR files from `target/` directory
- Test reports from `target/surefire-reports/`

## Test Reports

Test results are published as JUnit XML reports and can be viewed in:
- Jenkins test results section
- Build history with test trend graphs

## Build Information

Each pipeline displays:
- Build number
- Git commit hash
- Build status
- Execution timestamps

