FROM openjdk:17
EXPOSE 8089
ADD target/gestion-station-ski-1.0-SNAPSHOT.jar GestionStationSki.jar
ENTRYPOINT ["java", "-jar" , "GestionStationSki.jar"]

