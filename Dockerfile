FROM openjdk:11

VOLUME /tmp
#RUN apk add --no-cache netcat-openbsd &&\
#    apk add --no-cache bash &&\
#    apk add -U tzdata &&\
#    cp -f /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime

ARG JAR_FILE
ARG UTIL_DIR
ENV JAVA_OPTS "-Xmx512m -Xms256m"

#RUN addgroup -S javauser && adduser -S javauser -G javauser
WORKDIR /opt/app

ADD ${UTIL_DIR}wait-for-it.sh ./wait-for-it.sh
ADD ${UTIL_DIR}registry.sh ./registry.sh

RUN chmod +x ./registry.sh

ADD ${JAR_FILE} registry.jar
ENTRYPOINT ["/opt/app/registry.sh"]