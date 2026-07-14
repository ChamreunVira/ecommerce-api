FROM eclipse-temurin:21-alpine AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw -DskipTests clean package

FROM eclipse-temurin:21-alpine
WORKDIR /app
ARG JAR_FILE=/app/target/*.jar
COPY --from=build ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java" , "-jar" , "/app/app.jar"]