FROM alpine:latest AS build

RUN apk add --no-cache bash openjdk8

RUN wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein -O /usr/local/bin/lein
RUN chmod 755 /usr/local/bin/lein

RUN mkdir -p /build/src
COPY project.clj /build/
COPY src/ /build/src/
WORKDIR /build
RUN lein deps
RUN lein uberjar

FROM alpine:latest

RUN apk add --no-cache openjdk8-jre
RUN mkdir /stanga
WORKDIR /stanga
COPY --from=build /build/target/stanga.secretary-bot-0.1.0-standalone.jar .

CMD ["java", "-jar", "stanga.secretary-bot-0.1.0-standalone.jar"]
