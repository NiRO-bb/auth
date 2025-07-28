# Auth microservice
This project presents microservice for authentication and authorization.

## Install
### Preferenced requirements
* Java 21
* Maven 3.9.9
* Spring Boot 3.5.3
* Docker (for testing)
* <b>PostgreSQL only</b>

### Steps to install project
1. Clone repository
```shell
git clone https://github.com/NiRO-bb/auth.git
```

2. Build with Maven
You must replace the following:
* `<client_id>` with client id value from google cloud
* `<client_secret>` with client secret value from google cloud
```shell
mvn clean package  "-Dspring.security.oauth2.client.registration.google.client-id=<client_id>" "-Dspring.security.oauth2.client.registration.google.client-secret=<client_secret>"
```

## Usage
Then launch JAR with specified database.
<b>Below just a pattern!</b>
You <b>must</b> replace the following:
* `<jar_name>` with name of JAR file that produced by Maven (actual is `AuthService-0.0.1-SNAPSHOT.jar`)
* `<port>` with your real port
* `<database>` with name of your real database
* `<username>` with name of user who has access to specified database
* `<password>` with password of specified user
* `<secret>` with secret - cryptographic key used for signing and verifying the token's integrity (<i>example: 1af312f5365fdb661334102f81c41582c04cb64048a9bf2fe802b1a04ea4bbc7</i>)
* `<expirations>` with time value (in milliseconds) - JWT expiration
* `<client_id>` with client id value from google cloud
* `<client_secret>` with client secret value from google cloud
```shell
java -jar target/<jar_name>.jar \
 --spring.datasource.url=jdbc:postgresql://localhost:<port>/<database> \
  --spring.datasource.username=<username> \
   --spring.datasource.password=<password> \
   --token.secret.key=<secret> \
   --token.expirations=<expirations> \
   --spring.security.oauth2.client.registration.google.client-id=<client_id> \
   --spring.security.oauth2.client.registration.google.client-secret=<client_secret> 
```

## Contributing
<a href="https://github.com/NiRO-bb/auth/graphs/contributors/">Contributors</a>

## License
No license 