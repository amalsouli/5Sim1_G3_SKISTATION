def success() {
    def imageUrl = 'https://semaphoreci.com/wp-content/uploads/2020/02/cic-cd-explained.jpg'
    def imageWidth = '800px'
    def imageHeight = 'auto'

    echo "Sending success email..."
    emailext(
        body: """
        <html>
        <body>
            <p>YEEEEY, The Jenkins job was successful.</p>
            <p>You can view the build at: <a href="${BUILD_URL}">${BUILD_URL}</a></p>
            <p><img src="${imageUrl}" alt="Your Image" width="${imageWidth}" height="${imageHeight}"></p>
        </body>
        </html>
        """,
        subject: "Jenkins Build - Success",
        to: 'oumayma.cherif@esprit.tn',
        from: 'oumayma.cherif@esprit.tn',
        replyTo: 'oumayma.cherif@esprit.tn',
        mimeType: 'text/html'
    )
    echo "Success email sent."
}

def failure() {
    def imageUrl = 'https://miro.medium.com/v2/resize:fit:4800/format:webp/1*ytlj68SIRGvi9mecSDb52g.png'
    def imageWidth = '800px'
    def imageHeight = 'auto'

    echo "Sending failure email..."
    emailext(
        body: """
        <html>
        <body>
            <p>eewww, The Jenkins job Failed.</p>
            <p>You can view the build at: <a href="${BUILD_URL}">${BUILD_URL}</a></p>
            <p><img src="${imageUrl}" alt="Your Image" width="${imageWidth}" height="${imageHeight}"></p>
        </body>
        </html>
        """,
        subject: "Jenkins Build - Failure",
        to: 'oumayma.cherif@esprit.tn',
        from: 'oumayma.cherif@esprit.tn',
        replyTo: 'oumayma.cherif@esprit.tn',
        mimeType: 'text/html'
    )
    echo "Failure email sent."
}

pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'MAVEN_HOME'
    }

    environment {
        NEXUS_CREDENTIALS = credentials('nexus-credentials')
        GITHUB_CREDENTIALS = credentials('github')
        SONAR_TOKEN = credentials('sonar.token')
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub')
        IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        stage("Cleanup Workspace") {
            steps {
                cleanWs()
            }
        }

        stage("Checkout from SCM") {
            steps {
                git branch: 'main',
                credentialsId: 'github',
                url: 'https://github.com/Oumayma-cherif/Devops.git'
            }
        }

        stage("Build Backend Application") {
            steps {
                sh "mvn clean package -DskipTests"
            }
        }

        stage("Build Frontend Application") {
            steps {
                script {
                    // Navigate to the frontend directory and build the Angular application
                    dir('frontend') {
                        sh "npm install"
                        sh "ng build --prod"
                    }
                }
            }
        }

        stage("SonarQube Analysis") {
            steps {
                script {
                    sh 'docker start sonarqube || echo "SonarQube already running."'
                    sh '''
                    echo "Waiting for SonarQube to be operational..."
                    until curl -s http://localhost:9000/api/system/status | grep -q "UP"; do
                        echo "SonarQube is not ready, waiting for 10 seconds..."
                        sleep 10
                    done
                    echo "SonarQube is now ready!"
                    '''
                    sh "mvn sonar:sonar -Dsonar.projectKey=gestion-station-ski -Dsonar.host.url=http://localhost:9000 -Dsonar.login=${SONAR_TOKEN}"
                }
            }
        }

        stage("Nexus Deployment") {
            steps {
                script {
                    sh 'docker start nexus || echo "Nexus already running."'
                    sh 'sleep 10'
                    sh "mvn clean deploy -DskipTests -s /var/lib/jenkins/.m2/settings.xml"
                }
            }
        }

        stage("Build Backend Docker Image") {
            steps {
                script {
                    // Build the Docker image for the backend
                    sh "docker build -t oumayy/gestion-devops-backend:${IMAGE_TAG} ./backend"
                }
            }
        }

        stage("Build Frontend Docker Image") {
            steps {
                script {
                    // Build the Docker image for the frontend
                    sh "docker build -t oumayy/gestion-devops-frontend:${IMAGE_TAG} ./frontend"
                }
            }
        }

        stage("Publish Docker Images") {
            steps {
                script {
                    // Push both frontend and backend Docker images to Docker Hub
                    sh "echo ${DOCKER_HUB_CREDENTIALS_PSW} | docker login -u ${DOCKER_HUB_CREDENTIALS_USR} --password-stdin"
                    sh "docker push oumayy/gestion-devops-backend:${IMAGE_TAG}"
                    sh "docker push oumayy/gestion-devops-frontend:${IMAGE_TAG}"
                }
            }
        }

        stage("Start Services with Docker Compose") {
            steps {
                script {
                    dir("${WORKSPACE}") {
                        sh "docker-compose up -d"
                    }
                }
            }
        }

        stage("Email Notification") {
            steps {
                script {
                    currentBuild.resultIsBetterOrEqualTo('SUCCESS') ? success() : failure()
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
