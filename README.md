# Sensors

This project was created for purposes of learning more about containerization, distributed systems and microservices.

# Project overview

The idea of the project was to create a system which will be fed some simple sensor values and be able to show those values for specific sensor.

## Architecture overview

![Architecture overview](/images/overviewV3.png)

## Develop with docker

Docker compose file written for the *sensor* part of architecture contains two same services that represent sensor REST API. This is not the best practice and it was done for learning purposes.

### Run API and services:

- Make use of gradlew scripts in each project and build fresh versions with ``` ./gradlew clean build ``` command
- Publish *messaging* to your local maven repository using gradle publishing script ``` publishToMavenLocal ```
- Run a command ``` docker-compose up --build ```
- Access the API locally
    - Visit http://localhost:8080/swagger-ui.html to see all possible endpoints

![Sensor swagger](/images/swagger.png)

### Run the ELK (Elasticsearch-Kibana-Logstash) stack:

- Make sure Docker Engine is allotted at least 4GiB of memory
- If you are developing on linux set the `vm.max_map_count` to at least `262144` - `sysctl -w vm.max_map_count=262144`
- Run ```docker-compose up --build``` inside the ELK folder

ELK is configured to run on the same network as API and other services.

Read more on [elasticsearch multi-node cluster](https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html#docker-prod-prerequisites) for further configuration.

#### Log examples

Nginx logs:

![Nginx logs](/images/nginx.png)

Custom JAVA logs:

![Sensor logs](/images/sensor.png)

## Notes

The configuration of services used in this application is basic and can be used as an example for developing something more complex.


