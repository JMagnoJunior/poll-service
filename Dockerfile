# syntax=docker/dockerfile:experimental
FROM adoptopenjdk/maven-openjdk11 AS build
ENV APPLICATION_USER builduser
ENV MAVEN_OPTS='-Xmx512M -Xss512k -XX:+PrintFlagsFinal -Xlog:gc -verbose:gc -XX:MetaspaceSize=64M -XX:MaxMetaspaceSize=128M -XX:CompressedClassSpaceSize=16M'
RUN useradd -u 1000 -ms /bin/bash $APPLICATION_USER
RUN mkdir -p /home/$APPLICATION_USER/.m2 \
    && mkdir -p /home/$APPLICATION_USER/.m2/repository \
    && mkdir -p /home/$APPLICATION_USER/myapp \
    && chown -Rh $APPLICATION_USER:$APPLICATION_USER /home/$APPLICATION_USER \
    && chown -Rh $APPLICATION_USER:$APPLICATION_USER /home/builduser/.m2/repository
WORKDIR /home/$APPLICATION_USER/myapp
COPY ./certificates/downloads.mongodb.com.cer /app/certificates/downloads.mongodb.com.cer
RUN $JAVA_HOME/bin/keytool -import -alias poll-download-mongodb -file  /app/certificates/downloads.mongodb.com.cer -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt
COPY pom.xml .
RUN --mount=type=cache,mode=1777,uid=1000,target=/home/builduser/.m2/repository mvn dependency:go-offline -nsu -U -V --batch-mode -T4
COPY . .
RUN mvn install -nsu
RUN cp /home/builduser/myapp/target/*.jar app.jar
USER $APPLICATION_USER
ENTRYPOINT ["java", "-Xmx512M", "-Xss512k", "-XX:MetaspaceSize=64M", "-XX:MaxMetaspaceSize=128M", "-XX:CompressedClassSpaceSize=16M", "-Dspring.profiles.active=production" ,"-jar", "/home/builduser/myapp/app.jar"]
