# Sensors

This project was created for purposes of learning more about containerization, distributed systems and microservices.

### Architecture overview

![Architecture overview](/images/overviewV1.png)

### Develop with docker

To make a use of the existing docker-compose file:

- Make use of gradlew scripts in each project and build fresh versions with ``` ./gradlew clean build ``` command
- Publish *messaging* to your local maven repository using gradle publishing script ``` publishToMavenLocal ```
- Run a command ``` docker-compose up --build ```
- Access the API locally using one of the following URLs
    - localhost:8080/sensor/add-value
    - localhost:8080/sensor/get-latest/{sensorId}
    - localhost:8080/sensor/get-values/{sensorId}