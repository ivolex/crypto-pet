FROM gradle:jdk21

COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle build

EXPOSE 8080
WORKDIR /app

CMD java -jar build/libs/crypto-pet-1.0-SNAPSHOT.jar