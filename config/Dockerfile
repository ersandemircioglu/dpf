ARG JAR_FILE

FROM openjdk:17-alpine
ARG JAR_FILE
RUN apk --no-cache add curl
ADD ./target/${JAR_FILE} /app/
ENV APP_JAR=/app/${JAR_FILE}

CMD java -Xmx200m -jar ${APP_JAR}

HEALTHCHECK --interval=10s --timeout=10s CMD curl -f http://localhost:8888/actuator/health || exit 1

EXPOSE 8888