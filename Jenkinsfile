pipeline {
    agent any
    environment {
        SONAR_TOKEN = credentials('sonari') // Assumes you've added a secret token in Jenkins credentials store
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

        // Uncomment the following stage if you need to deploy to Nexus
        stage('Deploy to Nexus') {
           steps {
            sh 'mvn clean deploy -DskipTests'
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
