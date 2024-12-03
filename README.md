# Job Instance Framework Service
This service serves as bridge between the tJobInstanceStart and tJobInstanceEnd components from Talend and the actual database or storage.

The main goals are:
* Separate the storage from the jobs to allow easier change the storage without changing a huge number of jobs
* Minimize the number of database connections by using a database connection pool (if usable)
* Allow also other implementations (like Python based programs) using the job instance framework

This application requires Java 17+

## Service API Description

### Ping
GET /ping

response: "pong" 
status-code: 200

### Metrics
GET /metrics

response: Prometheus formatted metrics of the JVM and the service calls
status-code: 200
 