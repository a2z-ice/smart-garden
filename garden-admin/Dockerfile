  
FROM java:8-jdk-alpine
ADD target/garden-admin-0.0.1-SNAPSHOT.jar garden-admin-0.0.1-SNAPSHOT.jar
EXPOSE 8181
ENTRYPOINT ["java", "-jar", "garden-admin-0.0.1-SNAPSHOT.jar"]