FROM maven:3.9.6-eclipse-temurin-17
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
RUN ls -la target
CMD ["sleep", "1000"]