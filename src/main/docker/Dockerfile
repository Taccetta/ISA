FROM eclipse-temurin:17-jre-focal

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS=""

# Add a jhipster user to run our application so that it doesn't need to run as root
RUN adduser --disabled-password --gecos "" jhipster

WORKDIR /app

# Copy the jar file to the container
COPY target/*.jar app.jar

# Create logs directory and set permissions
RUN mkdir -p /app/logs && chown -R jhipster:jhipster /app

USER jhipster

EXPOSE 8080

# Set the default command to run when starting the container
CMD java ${JAVA_OPTS} -jar /app/app.jar --spring.profiles.active=prod 