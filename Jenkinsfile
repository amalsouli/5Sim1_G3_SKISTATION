pipeline {
    agent any

    environment {
        SONAR_TOKEN = credentials('jenkins-sonar') // Token SonarQube
    }

    stages {
        stage('GIT Checkout') {
            steps {
                echo "Getting Project from Git"
                git branch: 'mohamedachi_5SIM1_G3', credentialsId: 'your-credentials-id', url: 'https://github.com/amalsouli/5Sim1_G3_SKISTATION.git'
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
                    // Assurez-vous que sonar.projectKey et autres propriétés sont configurées
                    withSonarQubeEnv(installationName: 'sq1') { // Nom de votre installation SonarQube
                        sh "mvn clean org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar -Dsonar.projectKey=5Sim1_G3_SKISTATION -Dsonar.login=${SONAR_TOKEN}"
                    }
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                        // Déployer l'artefact dans Nexus en utilisant Maven
                        sh "mvn deploy -Dusername=${NEXUS_USER} -Dpassword=${NEXUS_PASS}"
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up...'
            cleanWs() // Nettoyage des fichiers temporaires
        }
    }
}
