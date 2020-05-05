# Sensors

This project was created for purposes of learning more about containerization, distributed systems and microservices.
The development is in progress.

### Architecture overview

![Architecture overview](/images/overviewV1.png)

### Develop with docker

To make a use of the existing docker-compose file:

- Make use of gradlew scripts in each project and build fresh versions with ``` ./gradlew clean build ``` command
- Publish *messaging* to your local maven repository using gradle publishing script ``` publishToMavenLocal ```
- Run a command ``` docker-compose up --build ```
- Access the API locally
    - Visit http://localhost:8080/swagger-ui.html to see all possible endpoints