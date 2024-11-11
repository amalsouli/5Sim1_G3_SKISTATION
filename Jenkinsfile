pipeline {
    agent any

    environment {
        SONAR_TOKEN = credentials('jenkins-sonar') // Assumes you've added a secret token in Jenkins credentials store
    }

    stages {
        stage('GIT Checkout') {
            steps {
                echo "Getting Project from Git"
                checkout([
                    $class: 'GitSCM', 
                    branches: [[name: '*/mohamedachi_5SIM1_G3']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/amalsouli/5Sim1_G3_SKISTATION.git', 
                        credentialsId: '12' // Ensure this credential exists and is valid
                    ]]
                ])
            }
        }

        stage('MVN CLEAN') {
            steps {
                echo "Running Maven Clean"
                sh 'mvn clean'
            }
        }

        stage('MVN COMPILE') {
            steps {
                echo "Running Maven Compile"
                sh 'mvn compile'
            }
        }

        stage('Unit Tests') {
            steps {
                echo "Running Unit Tests"
   sh 'mvn clean test -e'
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    echo "Starting SonarQube Analysis"
                    withSonarQubeEnv('sq1') { // Ensure the SonarQube installation name is correct in Jenkins
                        sh "mvn clean org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar -Dsonar.projectKey=5Sim1_G3_SKISTATION -Dsonar.login=${SONAR_TOKEN}"
                    }
                }
            }
        }

        // Uncomment the following stage if you need to deploy to Nexus
         stage('Deploy to Nexus') {
             steps {
                 echo "Deploying to Nexus"
                 sh 'mvn deploy -Dmaven.test.skip=true'
             }
         }
    }

    post {
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }
    }
}
