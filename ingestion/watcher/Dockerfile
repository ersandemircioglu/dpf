ARG JAR_FILE

FROM openjdk:17-alpine
ARG JAR_FILE
ADD ./target/${JAR_FILE} /app/
ENV APP_JAR=/app/${JAR_FILE}

CMD java -Xmx200m -jar ${APP_JAR}