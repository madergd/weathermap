# design
Download and create the artifact running mvn clean install from the root project folder. 

Running from withing IntelliJ with jettyrunner works fine
http://localhost:8080/weathermap/weather.jsp

# Running 
with jettyrunner downloaded here http://repo2.maven.org/maven2/org/mortbay/jetty/jetty-runner/ is not finding the servlet
java -jar jetty-runner.jar weathermap-1.0-SNAPSHOT.jar
http://localhost:8080/web/weather.jsp
I did not have time to investigate

# Note
Sunrise and sunset are coming back incorrect for Hong Kong, see http://openweathermap.org/city/1819729

# TODOs
Internationalization, styling, tests

