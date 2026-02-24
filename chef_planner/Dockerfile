
FROM maven:3-eclipse-temurin-21 AS build
WORKDIR /app


COPY pom.xml .



RUN mvn -DskipTests dependency:go-offline

COPY src/ src/

RUN mvn -DskipTests package


FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=build /app/target/*.jar /app/app.jar
COPY docker/entrypoint.sh /app/entrypoint.sh


RUN sed -i 's/\r$//' /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

USER spring:spring

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseContainerSupport"
EXPOSE 8080

ENTRYPOINT ["/app/entrypoint.sh"]
