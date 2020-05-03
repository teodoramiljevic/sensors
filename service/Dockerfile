FROM openjdk:11-jdk

WORKDIR /app

#COPY . .
#RUN ./gradlew clean build

COPY build/libs/*.jar app.jar

COPY ./wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh

CMD ["java", "-jar", "app.jar"]