FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app
COPY . /app

RUN mvn clean package -DskipTests && ls -lah target/

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/encomendasdois-0.0.1-SNAPSHOT.jar /app/app.jar


CMD ["java", "-jar", "/app/app.jar"]

