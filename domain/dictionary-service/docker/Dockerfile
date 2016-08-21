FROM java:8
ADD dictionary-service.jar /app.jar
CMD java -Xmx512m -jar -Dspring.profiles.active=docker /app.jar
EXPOSE 8080
