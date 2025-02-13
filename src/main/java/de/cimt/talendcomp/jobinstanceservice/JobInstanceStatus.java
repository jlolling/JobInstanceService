/**
 * Copyright 2024 Jan Lolling jan.lolling@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cimt.talendcomp.jobinstanceservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobInstanceStatus {
	
	@JsonProperty(value = "job_instance_id") private long jobInstanceId = 0l;
	@JsonProperty(value = "root_job_instance_id") private Long processInstanceId = 0l;
	@JsonProperty(value = "job_guid") private String guid;
	@JsonProperty(value = "job_name") private String name;
	@JsonProperty(value = "project") private String project;
	@JsonProperty(value = "root_job_guid") private String rootJobGuid;
	@JsonProperty(value = "parent_job_guid") private String parentJobGuid;
	@JsonProperty(value = "job_info") private String jobInfo;
	@JsonProperty(value = "external_job_id") private String extJobId;
	@JsonProperty(value = "task_name") private String taskName;
	@JsonProperty(value = "work_item") private String workItem;
	@JsonProperty(value = "job_result") private String jobResult;
	@JsonProperty(value = "start_date") private Date startDate;
	@JsonProperty(value = "stop_date") private Date stopDate;
	@JsonProperty(value = "time_range_start") private Date timeRangeStart;
	@JsonProperty(value = "time_range_end") private Date timeRangeEnd;
	@JsonProperty(value = "value_range_start") private String valueRangeStart;
	@JsonProperty(value = "value_range_end") private String valueRangeEnd;
	@JsonProperty(value = "count_input") private Integer countInput;
	@JsonProperty(value = "count_output") private Integer countOutput;
	@JsonProperty(value = "count_update") private Integer countUpdate;
	@JsonProperty(value = "count_reject") private Integer countReject;
	@JsonProperty(value = "count_delete") private Integer countDelete;
	@JsonProperty(value = "host_name") private String hostName;
	@JsonProperty(value = "host_pid") private Integer hostPid;
	@JsonProperty(value = "host_user") private String hostUser;
	@JsonProperty(value = "return_code") private Integer returnCode;
	@JsonProperty(value = "return_message") private String returnMessage;
	@JsonProperty(value = "named-counters") private List<JobDetailCounter> counters = null;
	
	/**
	 * constructor used for the OR mapper
	 */
	protected JobInstanceStatus() {}

	public long getJobInstanceId() {
		return jobInstanceId;
	}
	
	public void setJobInstanceId(long jobInstanceId) {
		this.jobInstanceId = jobInstanceId;
	}
	
	public long getProcessInstanceId() {
		return processInstanceId;
	}
	
	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	public String getGuid() {
		return guid;
	}
	
	public boolean isRootJob() {
		if (rootJobGuid == null) {
			return true;
		} else if (guid.equals(rootJobGuid)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setGuid(String jobGuid) {
		this.guid = jobGuid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String jobName) {
		if (jobName == null || jobName.trim().isEmpty()) {
			throw new IllegalArgumentException("jobName cannot be null or empty");
		}
		this.name = jobName;
	}
	
	public String getRootJobGuid() {
		return rootJobGuid;
	}
	
	public void setRootJobGuid(String rootJobGuid) {
		this.rootJobGuid = rootJobGuid;
	}
	
	public String getTaskName() {
		return taskName;
	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public String getWorkItem() {
		return workItem;
	}
	
	public void setWorkItem(String workItem) {
		this.workItem = workItem;
	}
	
	public String getJobResult() {
		return jobResult;
	}
	
	public void setJobResult(String jobResult) {
		this.jobResult = jobResult;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		if (startDate == null) {
			throw new IllegalArgumentException("startDate cannot be null or empty");
		}
		this.startDate = startDate;
	}
	
	public Date getStopDate() {
		return stopDate;
	}
	
	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}
	
	public Date getTimeRangeStart() {
		return timeRangeStart;
	}
	
	public void setTimeRangeStart(Date timeRangeDate) {
		this.timeRangeStart = timeRangeDate;
	}
	
	public Date getTimeRangeEnd() {
		return timeRangeEnd;
	}
	
	public void setTimeRangeEnd(Date timeRangeDate) {
		this.timeRangeEnd = timeRangeDate;
	}

	public Integer getCountInput() {
		return countInput;
	}
	
	public void setCountInput(int countInput) {
		this.countInput = countInput;
	}
	
	public Integer getCountOutput() {
		return countOutput;
	}
	
	public void setCountOutput(int countOutput) {
		this.countOutput = countOutput;
	}
	
	public Integer getCountReject() {
		return countReject;
	}
	
	public void setCountReject(int countReject) {
		this.countReject = countReject;
	}
	
	public Integer getCountDelete() {
		return countDelete;
	}
	
	public void setCountDelete(int countDelete) {
		this.countDelete = countDelete;
	}
	
	public String getHostName() {
		return hostName;
	}
	
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	public Integer getHostPid() {
		return hostPid;
	}
	
	public void setHostPid(int hostPid) {
		this.hostPid = hostPid;
	}
	
	public Integer getReturnCode() {
		return returnCode;
	}
	
	public void setReturnCode(Integer returnCode) {
		if (returnCode != null) {
			this.returnCode = returnCode.intValue();
		} else {
			this.returnCode = 0;
		}
	}
	
	public String getReturnMessage() {
		return returnMessage;
	}
	
	public void setReturnMessage(String returnMessage) {
		if (returnMessage != null && returnMessage.trim().isEmpty() == false) {
			this.returnMessage = returnMessage.trim();
		}
	}
	
	public String getValueRangeStart() {
		return valueRangeStart;
	}
	
	public void setValueRangeStart(String valueRangeStart) {
		this.valueRangeStart = valueRangeStart;
	}
	
	public String getValueRangeEnd() {
		return valueRangeEnd;
	}
	
	public void setValueRangeEnd(String valueRangeEnd) {
		this.valueRangeEnd = valueRangeEnd;
	}
	
	public int getCountUpdate() {
		return countUpdate;
	}

	public void setCountUpdate(int countUpdate) {
		this.countUpdate = countUpdate;
	}

	public String getJobInfo() {
		return jobInfo;
	}

	public void setJobInfo(String jobInfo) {
		this.jobInfo = jobInfo;
	}

	public String getExtJobId() {
		return extJobId;
	}

	public void setExtJobId(String extJobId) {
		this.extJobId = extJobId;
	}

	public String getParentJobGuid() {
		return parentJobGuid;
	}

	public void setParentJobGuid(String parentJobGuid) {
		this.parentJobGuid = parentJobGuid;
	}

	public String getHostUser() {
		return hostUser;
	}

	public void setHostUser(String hostUser) {
		this.hostUser = hostUser;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}
	
	public void addJobDetailCounter(JobDetailCounter c) {
		if (counters == null) {
			counters = new ArrayList<JobDetailCounter>();
		}
		counters.add(c);
	}
	
	public List<JobDetailCounter> getCounters() {
		return counters;
	}

}
