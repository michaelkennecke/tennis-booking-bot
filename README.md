# Tennis Booking Bot :tennis:

A selenium bot to find and book tennis courts.

## How to run :rocket:

1. Clean and build the project

```
mvn clean
mvn install -DskipTest
```

2. Enter your username and password in the docker-compose.yml
3. Start the tennis-booking-bot with selenium

```
docker-compose --profile=prod up -d
```

## How to develop :hammer_and_pick:

1. Start selenium

```
docker-compose --profile=dev up -d
```

2. Adjust `applicatoin.yml` to your needs

3. Start the tennis-booking-bot

```
mvn spring-boot:run
```
