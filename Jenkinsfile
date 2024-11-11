pipeline {
    agent any
    environment {
        SONAR_TOKEN = credentials('sonari')
    }
    stages {
        stage('GIT Checkout') {
            steps {
                echo "Getting Project from Git"
                git branch: 'mohamedachi_5SIM1_G3', credentialsId: '12', url: 'https://github.com/amalsouli/5Sim1_G3_SKISTATION.git'
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
                sh 'mvn test'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('sq1') {
                        sh """
                        mvn clean install sonar:sonar \
                        -Dsonar.projectKey=5Sim1_G3_SKISTATION \
                        -Dsonar.login=${SONAR_TOKEN} \
                        -Dsonar.java.binaries=target/classes
                        """
                    }
                }
            }
        }
        stage('Deploy to Nexus') {
            steps {
                script {
                    try {
                        sh 'mvn clean deploy -PskipTests'
                    } catch (Exception e) {
                        echo "Deployment to Nexus failed: ${e.message}"
                        unstable("Nexus deployment failed")
                    }
                }
            }
        }
    }
    post {
        always {
            echo 'Cleaning up...'
            cleanWs()
        }
        failure {
            echo 'Build failed!'
        }
        success {
            echo 'Build completed successfully!'
        }
    }
}
