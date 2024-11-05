pipeline {
    agent any
    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    environment {
        IMAGE_TAG = "${env.BUILD_NUMBER}"
        SonarQubeEnv = credentials('admin') // Ensure this credential ID exists in Jenkins
    }

    stages {
        stage('Cleanup Workspace') {
            steps {
                cleanWs()
            }
        }
        stage('Checkout from SCM') {
            steps {
                git credentialsId: 'github', branch: 'main', url: 'https://github.com/Oumayma-cherif/ski.git'
            }
        }

        stage('Maven Clean') {
            steps {
                sh 'mvn clean install -U -DskipTests'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Mockito Tests') {
            steps {
                script {
                    echo "Starting unit tests with Mockito..."
                    sh 'mvn test -Dspring.profiles.active=test'
                    echo "Unit tests completed successfully!"
                }
            }
            post {
                success {
                    echo "All unit tests passed."
                }
                failure {
                    echo "Some unit tests failed."
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    // Start SonarQube in Docker
                    sh 'docker start sonarqube'

                    // Wait until SonarQube is ready
                    sh '''
                    echo "Waiting for SonarQube to be operational..."
                    until curl -s http://localhost:9000/api/system/status | grep -q "UP"; do
                        echo "SonarQube is not ready yet, waiting 10 seconds..."
                        sleep 10
                    done
                    echo "SonarQube is now ready!"
                    '''

                    // Execute SonarQube analysis
                    sh 'mvn sonar:sonar -Dspring.profiles.active=test -Dsonar.projectKey=gestion-station-ski -Dsonar.host.url=http://localhost:9000 -Dsonar.login=${SonarQubeEnv}'
                }
            }
        }

        stage('Nexus') {
            steps {
                script {
                    // Start the Nexus container
                    sh 'docker start nexus'

                    // Verify that Nexus is running
                    def nexusRunning = sh(script: 'docker ps | grep nexus', returnStatus: true) == 0
                    if (!nexusRunning) {
                        error "Nexus container is not running. Please check the configuration."
                    } else {
                        echo "Nexus is up and running."
                    }

                    // Pause to ensure Nexus is initialized
                    sh 'sleep 10'

                    // Deploy to Nexus without injecting credentials
                    sh 'mvn clean deploy -DskipTests -s /var/lib/jenkins/.m2/settings.xml'
                }
            }
        }

        stage('Docker Image') {
            steps {
                // Package with Maven
                sh 'mvn clean package -DskipTests'

                // Build the Docker image
                sh 'docker build -t oumaymacherif_5sim1-ski .'
            }
        }

        stage('Docker Hub') {
            steps {
                // Login to Docker Hub
                sh 'docker login -u oumaymacherif -p YOUR_DOCKER_HUB_PASSWORD'

                // Push the image to Docker Hub
                sh 'docker push oumaymacherif_5sim1-ski'
            }
        }

        stage('Docker Compose') {
            steps {
                script {
                    // Launch Docker Compose to start the containers
                    sh 'docker compose up -d'

                    // Pause to ensure the containers have time to start
                    sh 'sleep 10'

                    // Check the Spring app container
                    def appContainer = sh(script: "docker ps | grep app-spring", returnStatus: true)
                    if (appContainer == 0) {
                        echo "Spring container creation successful for gestion-station-ski."
                    } else {
                        error "Failed to start the Spring container for gestion-station-ski."
                    }

                    // Check the MySQL db container
                    def dbContainer = sh(script: "docker ps | grep mysqldb", returnStatus: true)
                    if (dbContainer == 0) {
                        echo "MySQL container creation successful for MySQL 5.7."
                    } else {
                        error "Failed to start the MySQL container for MySQL 5.7."
                    }
                }
            }
        }

        stage('Grafana') {
            steps {
                script {
                    // Start mysql-exporter2 container
                    echo "Starting mysql-exporter2 container..."
                    sh 'docker start mysql-exporter2'

                    // Wait for the container to initialize
                    sleep 180

                    // Check if mysql-exporter2 is running
                    def mysqlExporterContainer = sh(script: "docker ps | grep mysql-exporter2", returnStatus: true)
                    if (mysqlExporterContainer == 0) {
                        echo "mysql-exporter2 container created successfully."
                    } else {
                        error "Failed to start mysql-exporter2 container."
                    }

                    // Start Prometheus container
                    echo "Starting Prometheus container..."
                    sh 'docker start prometheus'

                    // Wait for the container to initialize
                    sleep 180

                    // Check if Prometheus is running
                    def prometheusContainer = sh(script: "docker ps | grep prometheus", returnStatus: true)
                    if (prometheusContainer == 0) {
                        echo "Prometheus container created successfully."
                    } else {
                        error "Failed to start Prometheus container."
                    }

                    // Start Grafana container
                    echo "Starting Grafana container..."
                    sh 'docker start grafana'

                    // Wait for the container to initialize
                    sleep 180

                    // Check if Grafana is running
                    def grafanaContainer = sh(script: "docker ps | grep grafana", returnStatus: true)
                    if (grafanaContainer == 0) {
                        echo "Grafana container created successfully."
                    } else {
                        error "Failed to start Grafana container."
                    }
                }
            }
        }
    }
}
