# Stage 1: Build Maven project
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy Maven project files
COPY pom.xml .
COPY src ./src
COPY testng.xml .
COPY server.js .
COPY db.json .
COPY public ./public

# Build tests (skip execution)
RUN mvn install -DskipTests

# Stage 2: Runtime with Node + Java
FROM maven:3.9.6-eclipse-temurin-21

WORKDIR /app

# Install Node + Playwright browser dependencies
RUN apt-get update && apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_24.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean

# Copy built project from builder stage
COPY --from=builder /app /app

RUN mvn exec:java -e \
    -Dexec.mainClass=com.microsoft.playwright.CLI \
    -Dexec.args="install-deps chromium"

RUN mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"

# Install json-server locally (AFTER copy so it isn’t overwritten)
RUN cd /app && npm init -y && npm install json-server@0.17.4

EXPOSE 3001

ENV TEST_BROWSER=chromium
ENV TEST_HEADLESS=true

# Start mock server and run tests
CMD nohup node server.js & \
    for i in {1..10}; do \
      curl -s http://localhost:3001/users && break; \
      echo "Waiting for API server..."; \
      sleep 2; \
    done && \
    mvn test -DsuiteXmlFile=testng.xml -Dbrowser=$TEST_BROWSER -Dheadless=$TEST_HEADLESS

