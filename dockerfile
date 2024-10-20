FROM openjdk:17
COPY .env /wallet-app/.env
ADD /target/wallet-app-0.0.1.jar wallet-app.jar
ENTRYPOINT ["java", "-jar", "wallet-app.jar"]