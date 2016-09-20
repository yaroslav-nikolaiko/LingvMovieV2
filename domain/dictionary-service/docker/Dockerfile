FROM java:8
ADD dictionary-service.jar /app.jar
CMD java -Xmx128m -jar -Dspring.profiles.active=docker /app.jar
EXPOSE 8080
