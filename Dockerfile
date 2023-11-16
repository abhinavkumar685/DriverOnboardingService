FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY target/springboot-app-mongodb-docker.jar /app/
EXPOSE 8080

CMD ["java", "-jar", "/app/springboot-app-mongodb-docker.jar"]
