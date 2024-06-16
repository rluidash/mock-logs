This MockLogGenerator java app is to generate mock Java logs for testing Elasticsearch. These logs can then be ingested into Elasticsearch using tools like Logstash or Filebeat. Here's a step-by-step guide:

The MockLogGenerator.java directory structure is shown as following:

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

Build and run the MockLogGenerator app locally:

```
gradle build
gradle run
```
The created mock logs are located in logs/app.log file. These logs contain different severity levels and messages.


Running ElasticSearch locally and ingest the logs from MockLogGenerator app. Use the docker-compose.yml file and it will create Elasticsearch, Kiabana, Logstash and MockLogGenerator containers.

```
docker-compose up --build
```

Verify all containers are created and running
```
docker ps -a

CONTAINER ID   IMAGE                                                  COMMAND                  CREATED       STATUS       PORTS                              NAMES
0ccd7e19fa91   mock-logs-logstash                                     "/usr/local/bin/dock…"   3 hours ago   Up 3 hours   5044/tcp, 9600/tcp                 logstash
59097305df5e   docker.elastic.co/kibana/kibana:7.17.0                 "/bin/tini -- /usr/l…"   3 hours ago   Up 3 hours   0.0.0.0:5601->5601/tcp             kibana
c12dc4719083   docker.elastic.co/elasticsearch/elasticsearch:7.17.0   "/bin/tini -- /usr/l…"   3 hours ago   Up 3 hours   0.0.0.0:9200->9200/tcp, 9300/tcp   elasticsearch
ca8d8420d35a   mock-logs-loggenerator                                 "java -cp build/libs…"   3 hours ago   Up 3 hours                                      loggenerator

```

Access Kibana and setup the index pattern for mock-logs-* index.

Navigate to the Kibana UI:
Open Kibana in your web browser: http://localhost:5601.

Create an Index Pattern:
 - Go to Management -> Stack Management -> Kibana -> Index Patterns.
 - Click on Create index pattern.
 - Enter the index pattern name. Since the Logstash configuration specifies the index pattern as mock-logs-*, enter mock-logs-* in the index pattern field.
 - Click Next step.
Select a Time Field:
 - Select the time field that matches your logs' timestamp (e.g., @timestamp depends on your Logstash configuration).
 - Click Create index pattern.

View Logs in Kibana:
 - Go to Discover in left-hand menu
 - Select the newly created index pattern `mock-logs-*`

Verify Elasticsearch using curl command:
```
curl -X GET "localhost:9200/mock-logs-*/_search?pretty"
```
curl -X GET "localhost:9200/mock-logs-*/_search?pretty"
