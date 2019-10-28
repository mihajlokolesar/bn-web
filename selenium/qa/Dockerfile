FROM  maven:3.3-jdk-8 AS builder

WORKDIR /usr/src/test

COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src/
COPY config ./config/
CMD mvn clean verify -P hub -Dhuburl=http://"$HUB_HOST":"$HUB_PORT"/wd/hub -Dconfig=suite.conf.json -Dbaseurl=$BASE_URL
