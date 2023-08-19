FROM openjdk:17-alpine

ENV TZ="Europe/Berlin"

COPY . /app
WORKDIR /app

COPY ./target/tennis-booking-bot-0.0.1-SNAPSHOT.jar ./

EXPOSE 2271

CMD ["java", "-jar", "tennis-booking-bot-0.0.1-SNAPSHOT.jar"]
