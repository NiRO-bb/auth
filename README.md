# Auth microservice
This project presents microservice for authentication and authorization.

## Install
### Preferenced requirements
* Java 21
* Maven 3.9.9
* Spring Boot 3.5.3
* Docker
* PostgreSQL

### Steps to install project
1. Clone repository
```shell
git clone https://github.com/NiRO-bb/auth.git
```

2. Create .env files
You must write .env_dev and .env_prod files with following values:
* TOKEN_SECRET_KEY
* TOKEN_EXPIRATIONS
* SPRING_DATASOURCE_URL
* SPRING_DATASOURCE_USERNAME
* SPRING_DATASOURCE_PASSWORD
* GOOGLE_CLIENT_ID
* GOOGLE_CLIENT_SECRET
<p>.env_dev - for local development </p>
<p>.env_prod - for container (docker) development</p>

3. Build with Maven
```shell
mvn clean package
```

## Usage
1. Launch Docker
```shell
docker compose up
```
<p>or</p>

2. Launch JAR
```shell
java -jar target/AuthService-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

## Contributing
<a href="https://github.com/NiRO-bb/auth/graphs/contributors/">Contributors</a>

## License
No license 