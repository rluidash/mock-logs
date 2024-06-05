This MockLogGenerator java app is to generate mock Java logs for testing Elasticsearch. These logs can then be ingested into Elasticsearch using tools like Logstash or Filebeat. Here's a step-by-step guide:

Step 1: Create a Simple Java Logging Application
Set Up Your Project Structure:
Create a new directory for your project and set up the following structure:

```
mock-logs/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   └── MockLogGenerator.java
└── build.gradle
```

Create the Java Class:
Inside MockLogGenerator.java, write the following code:

```
package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MockLogGenerator {

    private static final Logger logger = LogManager.getLogger(MockLogGenerator.class);

    public static void main(String[] args) {
        Random random = new Random();
        String[] messages = {"User logged in", "User logged out", "User updated profile", "User deleted account"};

        while (true) {
            try {
                int index = random.nextInt(messages.length);
                String message = messages[index];
                logger.info(message);
                TimeUnit.SECONDS.sleep(1); // Generate a log every second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
```

Configure Gradle:
Create a build.gradle file to manage dependencies and build your project:

```
plugins {
    id 'java'
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.apache.logging.log4j:log4j-core:2.17.1'
    implementation 'org.apache.logging.log4j:log4j-api:2.17.1'
}

application {
    mainClassName = 'com.example.MockLogGenerator'
}
```

Build and Run Your Project:
Navigate to the project directory and run the following commands to build and execute your application:

```
gradle build
gradle run
```

This will start generating logs to the console. To direct these logs to a file, configure Log4j with a log4j2.xml configuration file.

Step 2: Configure Log4j to Write to a File
Create log4j2.xml:
Create a src/main/resources/log4j2.xml file with the following content:

```
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <File name="FileAppender" fileName="logs/app.log" append="true">
            <PatternLayout pattern="%d{ISO8601} [%t] %-5p %c - %m%n"/>
        </File>
    </Appenders>
    <Loggers>
        <Root level="info">
            <AppenderRef ref="FileAppender"/>
        </Root>
    </Loggers>
</Configuration>
```

Rebuild and Run Your Project:
Rebuild and run your project to start generating logs in the logs/app.log file.

```
gradle build
gradle run
```

Step 3: Ingest Logs into Elasticsearch
Using Logstash:
Create a Logstash configuration file logstash.conf to ingest logs:

```
input {
    file {
        path => "/path/to/your/logs/app.log"
        start_position => "beginning"
        sincedb_path => "/dev/null"
    }
}

filter {
    grok {
        match => { "message" => "%{TIMESTAMP_ISO8601:timestamp} \[%{DATA:thread}\] %{LOGLEVEL:level} %{DATA:class} - %{GREEDYDATA:message}" }
    }
}

output {
    elasticsearch {
        hosts => ["localhost:9200"]
        index => "mock-logs"
    }
    stdout { codec => rubydebug }
}
```

Run Logstash with the configuration file:

```
logstash -f logstash.conf
```

Using Filebeat:
Configure Filebeat to ship logs to Elasticsearch:

```
filebeat.inputs:
- type: log
  enabled: true
  paths:
    - /path/to/your/logs/app.log

output.elasticsearch:
  hosts: ["localhost:9200"]
  index: "mock-logs"
```

Start Filebeat:

```
filebeat -e
```

Step 4: Verify Data in Elasticsearch
Use Kibana or Elasticsearch's _search API to verify that the logs are ingested correctly:

```
curl -X GET "localhost:9200/mock-logs/_search?pretty"
By following these steps, you can generate mock Java logs and ingest them into Elasticsearch for testing and analysis.