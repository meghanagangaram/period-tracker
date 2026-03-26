FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . .
RUN apt update && apt install -y maven
RUN mvn clean package
CMD ["java", "-jar", "target/*.jar"]