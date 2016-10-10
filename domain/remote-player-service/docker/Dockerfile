FROM java:8
ADD remote-player-service.jar /app.jar
CMD java -Xmx128m -jar -Dspring.profiles.active=docker /app.jar
EXPOSE 8080
