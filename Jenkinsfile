pipeline {
    agent any
    environment {
        SONAR_TOKEN = credentials('sonari')
        DOCKER_CREDENTIALS_ID = 'your-docker-credentials-id'  // Update with the ID for Docker credentials in Jenkins
        DOCKER_IMAGE = 'yourdockerhubusername/gestion-station-ski:latest'  // Update with your Docker Hub username and repo
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
                    withSonarQubeEnv(installationName: 'sq1') {
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
        stage('Docker Build') {
            steps {
                script {
                    echo "Building Docker image"
                    sh "docker build -t ${DOCKER_IMAGE} ."
                }
            }
        }
        stage('Docker Push') {
            steps {
                script {
                    echo "Pushing Docker image to Docker Hub"
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USER --password-stdin"
                        sh "docker push ${DOCKER_IMAGE}"
                        sh "docker logout"
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
    }
}
