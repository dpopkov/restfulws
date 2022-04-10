# restfulws
RESTful Web Services

### How to run

Make sure database __photo_app__ and user for it exist. 
Use the script `scripts/mysql_db_users.sql`.

Then build:
```
mvn -DskipTests=true install
```
and run:
```
export DB_ADMIN=...
export DB_ADMIN_PASSWORD=...
java -jar target/restfulws-0.0.1-SNAPSHOT.jar
```
or
```
java -Dspring.datasource.username=... -Dspring.datasource.password=... -jar target/restfulws-0.0.1-SNAPSHOT.jar
```
