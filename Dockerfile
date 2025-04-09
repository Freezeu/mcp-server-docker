FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 19988

ENTRYPOINT ["java", "-jar", "app.jar", "--docker.host=tcp://host.docker.internal:2375", "--spring.profiles.active=stdio"]