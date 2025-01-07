# Job Instance Framework Service
This service serves as bridge between the tJobInstanceStart and tJobInstanceEnd components from Talend and the actual database or storage.

The main goals are:
* Separate the storage from the jobs to allow easier change the storage without changing a huge number of jobs
* Minimize the number of database connections by using a database connection pool (if usable)
* Allow also other implementations (like Python based programs) using the job instance framework
* all content are provided as json.

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

### Create job instance status entry
POST /job_instance

payload:

```
{
	"job_name":"my_talend_job_name",
	"job_project":"DWH_Project",
	"job_guid":"AzDvy",
	"job_version":"1.2.0",
	"parent_job_guid":null,
	"root_job_guid:"AzDvy",
	"task_name":"my_task_name",
	"started":"2024-12-10 08:34:12.567",
	"work_item":"my-work-item",
	"host_name":"localhost",
	"host_pid":1234567,
	"host_user":"talend",
	"time_range_start":null,
	"time_range_end":null,
	"value_range_start":null,
	"value_range_end":null
}
```
response: 
http-code=201

```
{
	"job_instance_id":12345,
	"root_job_instance_id":12,
	"parent_job_instance_id":33
}
```

### Update job instance status entry
PUT /job_instance

payload:

```
{
	"job_instance_id":12345,
	"job_ended_at":"20204-12-10 08:44:00.123",
	"job_result":"my job result",
	"return_code":0,
	"return_message":null,
	"count_input":4444,
	"count_output":12,
	"count_updated":0,
	"count_deleted":0,
	"count"rejected":0,
	"work_item":"my new work-item",
	"time_range_start":null,
	"time_range_end":null,
	"value_range_start":null,
	"value_range_end":null,
	"named-counter": [
		{
			"name":"counter1",
			"type":"input",
			"value":99
		},
		{
			"name":"counter2",
			"type":"output",
			"value":99
		}	
	]
}
```

response:
http-status: 204
No response body

### Get the job instance ids of previous jobs
This returns the job_instance_ids based on filters.

GET /job_instance

URL-Parameters:
* job_name: the name of the job
* task_name: the name of the task
* work_item: null if not relevant, otherwise only jobs with the same work_item will be returned
* with_input: true/false to filter jobs which has input count > 0
* with_output: true/false to filter jobs which has output/update/delete count > 0
* is_successful: only successful jobs it true, if not set not filter, if false only failed jobs
* is_running: only running jobs
* return_code: only jobs with a specific return-code
* before_instance_id: job before with older/smaller job instance ids
* root_job_instance_id: the root of this job

response: http-status: 200
Array with job_instance_id

```
[12345,5678]
```

### Get a particular job instance status
GET /job_instance/12345

response: http-code: 200

```
{
	"job_instance_id":12345,
	"root_job_instance_id":12,
	"parent_job_instance_id":33,
	"job_name":"my_talend_job_name",
	"job_project":"DWH_Project",
	"job_guid":"AzDvy",
	"job_version":"1.2.0",
	"parent_job_guid":null,
	"root_job_guid:"AzDvy",
	"task_name":"my_task_name",
	"started":"2024-12-10 08:34:12.567",
	"work_item":"my-work-item",
	"host_name":"localhost",
	"host_pid":1234567,
	"host_user":"talend",
	"job_ended_at":"20204-12-10 08:44:00.123"
	"job_result":"my job result",
	"return_code":0,
	"return_message":null,
	"count_input":4444,
	"count_output":12,
	"count_updated":0,
	"count_deleted":0,
	"count"rejected":0,
	"work_item":"my new work-item",
	"time_range_start":null,
	"time_range_end":null,
	"value_range_start":null,
	"value_range_end":null
	"named-counter": [
		{
			"name":"counter1",
			"type":"input",
			"value":99
		},
		{
			"name":"counter2",
			"type":"output",
			"value":99
		}	
	]
}
```