# Use an official OpenJDK image as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /gestion-station-ski

# Copy the current directory contents into the container at /app
COPY target/gestion-station-ski-1.0.jar gestion-station-ski.jar

# Expose the port that the Spring Boot app runs on
EXPOSE 8089

# Run the application
ENTRYPOINT ["java", "-jar", "gestion-station-ski-1.0.jar"]
