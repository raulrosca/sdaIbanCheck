Contains one docker-compose.yml file, and two microservice java projects that comunicate with each other through Kafka topics.

Run the project:

Install / open docker
1 -  docker-compose up ( use "docker-compose up -d" if you want to use detached mode and hide the console logs )
2 - start the sdaPdfNotif spring cloud stream project. 
    - you can use any java code editor to open the project and run it (I used inteliJ for debugging ) or you can:
        2.1     -      cd sdaPdfNotif 
        2.2     -      ./mvnw package spring-boot:run -DskipTests=true -Pkafka
3 - go to pdfChecker directory
    - you can use any java code editor to open the project and run it (I used inteliJ for debugging ) or you can:
        3.1     -      cd sdaPdfNotif 
        3.2     -      ./mvnw package spring-boot:run -DskipTests=true -Pkafka

Further development.

Use Kafka connect with spool dir plugin to monitor a directory for when a pdf file apears, and then send the file information to a kafka topic.
Write tests.
Provide blacklisted ibans in another way, maybe DB, maybe api request.
Add apache license.
