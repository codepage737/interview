FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

WORKDIR /app/target/dependency
RUN jar -xf ../*.jar

FROM ibm-semeru-runtimes:open-17-jre-centos7
ARG DEPENDENCY=/app/target/dependency

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

EXPOSE 8080

CMD java -server \
    -Xmx1024m \
    -Xss1024k \
    -XX:MaxMetaspaceSize=135m \
    -XX:CompressedClassSpaceSize=28m \
    -XX:ReservedCodeCacheSize=13m \
    -XX:+IdleTuningGcOnIdle \
    -Xtune:virtualized \
    -cp /app:/app/lib/* \
    com.shahrabi.interview.InterviewApplication
