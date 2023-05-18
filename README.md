# spring-boot-crud-mservice
This is a test project for Users Crud Operations API with Spring Boot 3 and Java 17.

The μservice runs on default port 8080 and there is also a swagger available on
http://localhost:8080/swagger-ui/index.html.

As database, I used the in memory h2 of Spring.
The documentation of the APS's is implemented with the OPENApi and the unit tests
are done with the RestAssured library.

To run the project you need to have install gradle 7.6.1 (At least)
and run the following commands: 

gradle build
gradle bootRun
