#FROM eclipse-temurin:20-jdk
FROM gradle:8.3.0-jdk20

WORKDIR /app

#COPY gradle gradle
#COPY build.gradle.kts .
#COPY settings.gradle.kts .
#COPY gradlew .

#RUN ./gradlew --no-daemon dependencies

COPY /app .
#COPY config config

#RUN ./gradlew --no-daemon build
RUN gradle installDist

#ENV JAVA_OPTS "-Xmx512M -Xms512M"
# EXPOSE 7070

#CMD java -jar build/libs/app-1.0-SNAPSHOT.jar
CMD ./build/install/app/bin/app
