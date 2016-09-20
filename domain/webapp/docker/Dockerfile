FROM java:8
ADD webapp.jar /app.jar
CMD java -Xmx128m -jar -Dspring.profiles.active=docker /app.jar
EXPOSE 8080
