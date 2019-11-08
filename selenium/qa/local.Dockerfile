FROM  maven:3.3-jdk-8 AS builder

WORKDIR /usr/src/
RUN mkdir report
WORKDIR /usr/src/test
COPY pom.xml .
RUN mvn dependency:resolve
COPY src ./src/
COPY config ./config/
COPY /extra/docker_scripts/start_local.sh .
RUN chmod +x start_local.sh
CMD ["./start_local.sh"]