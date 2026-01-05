pipeline {
    agent any
    
    tools {
        maven 'Maven-3'
        jdk 'JDK-17'
    }
    
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 30, unit: 'MINUTES')
        timestamps()
    }
    
    environment {
        JAVA_HOME = tool 'JDK-17'
        MAVEN_HOME = tool 'Maven-3'
        PATH = "${MAVEN_HOME}/bin:${JAVA_HOME}/bin:${env.PATH}"
        MAVEN_OPTS = '-Xmx1024m -XX:MaxPermSize=512m'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code from repository...'
                script {
                    // Explicitly checkout main branch
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: '*/main']],
                        extensions: [],
                        userRemoteConfigs: scm.userRemoteConfigs
                    ])
                    def gitCommit = sh(
                        script: 'git rev-parse --short HEAD',
                        returnStdout: true
                    ).trim()
                    env.GIT_COMMIT_SHORT = gitCommit
                    echo "Checked out commit: ${env.GIT_COMMIT_SHORT} on branch main"
                }
            }
            post {
                success {
                    echo 'Checkout stage completed successfully'
                }
                failure {
                    error 'Checkout stage failed'
                }
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building the application with Maven...'
                script {
                    sh '''
                        echo "Java Version:"
                        java -version
                        echo "Maven Version:"
                        mvn -version
                        echo "Building project..."
                        mvn clean compile -DskipTests
                    '''
                }
            }
            post {
                success {
                    echo 'Build stage completed successfully'
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, allowEmptyArchive: true
                }
                failure {
                    echo 'Build stage failed'
                    error 'Build failed'
                }
            }
        }
        
        stage('Test') {
            steps {
                echo 'Running unit tests...'
                script {
                    sh '''
                        echo "Running Maven tests..."
                        mvn test
                    '''
                }
            }
            post {
                always {
                    echo 'Publishing test results...'
                    junit 'target/surefire-reports/*.xml'
                    publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
                }
                success {
                    echo 'All tests passed successfully'
                }
                failure {
                    echo 'Some tests failed'
                }
                unstable {
                    echo 'Tests completed with warnings'
                }
            }
        }
        
        stage('Package') {
            steps {
                echo 'Packaging the application...'
                script {
                    sh '''
                        echo "Creating JAR package..."
                        mvn package -DskipTests
                    '''
                }
            }
            post {
                success {
                    echo 'Package created successfully'
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
                failure {
                    echo 'Package creation failed'
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline execution completed'
            cleanWs()
        }
        success {
            echo 'Pipeline succeeded!'
            script {
                def buildInfo = """
                    Build Information:
                    - Build Number: ${env.BUILD_NUMBER}
                    - Git Commit: ${env.GIT_COMMIT_SHORT ?: 'N/A'}
                    - Build Status: SUCCESS
                """
                echo buildInfo
            }
        }
        failure {
            echo 'Pipeline failed!'
            script {
                def buildInfo = """
                    Build Information:
                    - Build Number: ${env.BUILD_NUMBER}
                    - Git Commit: ${env.GIT_COMMIT_SHORT ?: 'N/A'}
                    - Build Status: FAILURE
                """
                echo buildInfo
            }
        }
        unstable {
            echo 'Pipeline is unstable!'
        }
    }
}

