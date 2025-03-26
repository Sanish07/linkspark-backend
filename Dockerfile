# Using Amazon Corretto 21 JDK image to build the Spring Boot app
FROM amazoncorretto:21-alpine AS build

# Setting the working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY mvnw ./
COPY .mvn/ .mvn/

# Ensure the Maven wrapper is executable
RUN chmod +x mvnw

# Copy the pom.xml and install dependencies
COPY pom.xml ./
RUN ./mvnw dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Use Amazon Corretto 21 Java runtime image to run the application
FROM amazoncorretto:21-alpine

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Specify the command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
