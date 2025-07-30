FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY ./target/*.jar auth.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "auth.jar"]