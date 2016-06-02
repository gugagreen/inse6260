# inse6260
## Team:
## Gustavo Gallindo
## Maxime Lamothe

You will need to have maven installed to compile the project.

### DB setup:
- You will need to have a MySQL dbms, with a 
- We assume the you have a db called "inse6260", with username/password == root/nimda, but that can be configured in file "/sis/src/main/resources/application.properties".
- To setup the db, run "/sis/db/create_tables.sql" and "/sis/db/inserts.sql"

### Maven
* To compile the project, run:
  * $ mvn clean compile
* To run the project, run:
  * $ mvn spring-boot:run
* To run in debug mode, use:
  * $ mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
  * Then you can start a new Debug Configuration in your IDE using port 5005.
* To run tests
  * $ mvn clean test
