FROM stangaracing/stanga-secretary-bot-runtime:latest

WORKDIR /stanga
COPY target/secretary-bot-0.1.0-standalone.jar .
CMD [ \
  "java", \
  "-Xmx256M", \
  "-Xms64M", \
  "-XX:MaxMetaspaceSize=256M", \
  "-XX:+UnlockExperimentalVMOptions", \
  "-XX:+UseCGroupMemoryLimitForHeap", \
  "-jar", \
  "secretary-bot-0.1.0-standalone.jar" \
]
