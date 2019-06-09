FROM stangaracing/stanga-secretary-bot-build:latest AS build

RUN mkdir -p /build/src
COPY project.clj /build/
COPY src/ /build/src/
WORKDIR /build
RUN lein deps
RUN lein uberjar

FROM stangaracing/stanga-secretary-bot-runtime:latest

WORKDIR /stanga
COPY --from=build /build/target/stanga.secretary-bot-0.1.0-standalone.jar .
CMD ["java", "-jar", "stanga.secretary-bot-0.1.0-standalone.jar"]
