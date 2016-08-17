FROM java:8
ADD webapp.jar /app.jar
CMD java -jar -Dspring.profiles.active=docker /app.jar
EXPOSE 8080
