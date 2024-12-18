/**
 * Copyright 2025 Jan Lolling jan.lolling@gmail.com
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobInstanceStatus {
	
	private long jobInstanceId = 0l;
	private long processInstanceId = 0;
	private String guid;
	private String name;
	private String project;
	private String rootJobGuid;
	private String parentJobGuid;
	private String jobInfo;
	private String extJobId;
	private String taskName;
	private String workItem;
	private String jobResult;
	private Date startDate;
	private Date stopDate;
	private Date timeRangeStart;
	private Date timeRangeEnd;
	private String valueRangeStart;
	private String valueRangeEnd;
	private int countInput;
	private int countOutput;
	private int countUpdate;
	private int countReject;
	private int countDelete;
	private String hostName;
	private int hostPid;
	private String hostUser;
	private int returnCode;
	private String returnMessage;
	
	@JsonCreator
	public JobInstanceStatus(
			@JsonProperty(value = "taskName", required = true) String taskName,
			@JsonProperty(value = "jobName", required = true) String jobName,
			@JsonProperty(value = "jobGuid", required = true) String jobGuid,
			@JsonProperty(value = "parentJobGuid", required = true) String parentJobGuid,
			@JsonProperty(value = "rootJobGuid", required = true) String rootJobGuid,
			@JsonProperty(value = "jobInfo") String jobInfo,
			@JsonProperty(value = "externalJobId") String externalJobId,
			@JsonProperty(value = "workItem") String workItem,
			@JsonProperty(value = "startDate", required = true) Date startDate,
			@JsonProperty(value = "hostName", required = true) String hostName,
			@JsonProperty(value = "hostUser", required = true) String hostUser,
			@JsonProperty(value = "hostPid", required = true) int hostPid,
			@JsonProperty(value = "timeRangeStart") Date timeRangeStart,
			@JsonProperty(value = "timeRangeEnd") Date timeRangeEnd,
			@JsonProperty(value = "valueRangeStart") String valueRangeStart,
			@JsonProperty(value = "valueRangeEnd") String valueRangeEnd,
			@JsonProperty(value = "stopDate") Date stopDate,
			@JsonProperty(value = "countInput") int countInput,
			@JsonProperty(value = "countOutput") int countOutput,
			@JsonProperty(value = "countUpdate") int countUpdate,
			@JsonProperty(value = "countReject") int countReject,
			@JsonProperty(value = "countDelete") int countDelete,
			@JsonProperty(value = "returnCode") int returnCode,
			@JsonProperty(value = "returnMessage") String returnMessage
			) {
		this.taskName = taskName;
		this.name = jobName;
		this.parentJobGuid = parentJobGuid;
		this.rootJobGuid = rootJobGuid;
		this.jobInfo = jobInfo;
		this.extJobId = externalJobId;
		this.workItem = workItem;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.hostName = hostName;
		this.hostUser = hostUser;
		this.hostPid = hostPid;
		this.timeRangeStart = timeRangeStart;
		this.timeRangeEnd = timeRangeEnd;
		this.valueRangeStart = valueRangeStart;
		this.valueRangeEnd = valueRangeEnd;
		this.countInput = countInput;
		this.countOutput = countOutput;
		this.countUpdate = countUpdate;
		this.countReject = countReject;
		this.countDelete = countDelete;
		this.returnCode = returnCode;
		this.returnMessage = returnMessage;
	}
	
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

	public int getCountInput() {
		return countInput;
	}
	
	public void setCountInput(int countInput) {
		this.countInput = countInput;
	}
	
	public int getCountOutput() {
		return countOutput;
	}
	
	public void setCountOutput(int countOutput) {
		this.countOutput = countOutput;
	}
	
	public int getCountReject() {
		return countReject;
	}
	
	public void setCountReject(int countReject) {
		this.countReject = countReject;
	}
	
	public int getCountDelete() {
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
	
	public int getHostPid() {
		return hostPid;
	}
	
	public void setHostPid(int hostPid) {
		this.hostPid = hostPid;
	}
	
	public int getReturnCode() {
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
	
	public void checkTimeRange(Date timeRangeDate) {
		if (timeRangeDate != null) {
			if (this.timeRangeStart == null || this.timeRangeStart.after(timeRangeDate)) {
				this.timeRangeStart = timeRangeDate;
			}
			if (this.timeRangeEnd == null || this.timeRangeEnd.before(timeRangeDate)) {
				this.timeRangeEnd = timeRangeDate;
			}
		}
	}
	
	public void checkTimeRange(Long timeRangeLong) {
		if (timeRangeLong != null) {
			Date timeRangeDate = new Date(timeRangeLong);
			if (this.timeRangeStart == null || this.timeRangeStart.after(timeRangeDate)) {
				this.timeRangeStart = timeRangeDate;
			}
			if (this.timeRangeEnd == null || this.timeRangeEnd.before(timeRangeDate)) {
				this.timeRangeEnd = timeRangeDate;
			}
		}
	}

	public void checkValueRange(String newValue) {
		if (newValue != null && newValue.trim().isEmpty() == false) {
			if (valueRangeStart == null) {
				valueRangeStart = newValue.trim();
			} else {
				if (valueRangeStart.compareTo(newValue) > 0) {
					valueRangeStart = newValue;
				}
			}
			if (valueRangeEnd == null) {
				valueRangeEnd = newValue.trim();
			} else {
				if (valueRangeEnd.compareTo(newValue) < 0) {
					valueRangeEnd = newValue;
				}
			}
		}
	}

	public void checkValueRange(Long newValue) {
		if (newValue != null) {
			if (valueRangeStart == null || valueRangeStart.isEmpty()) {
				valueRangeStart = String.valueOf(newValue);
			} else {
				long cv = Long.valueOf(valueRangeStart);
				if (cv > newValue) {
					valueRangeStart = String.valueOf(newValue);
				}
			}
			if (valueRangeEnd == null || valueRangeEnd.isEmpty()) {
				valueRangeEnd = String.valueOf(newValue);
			} else {
				long cv = Long.valueOf(valueRangeEnd);
				if (cv < newValue) {
					valueRangeEnd = String.valueOf(newValue);
				}
			}
		}
	}

	public void checkValueRange(Character newValue) {
		if (newValue != null) {
			if (valueRangeStart == null || valueRangeStart.isEmpty()) {
				valueRangeStart = String.valueOf(newValue);
			} else {
				char cv = valueRangeStart.charAt(0);
				if (cv > newValue) {
					valueRangeStart = String.valueOf(newValue);
				}
			}
			if (valueRangeEnd == null || valueRangeEnd.isEmpty()) {
				valueRangeEnd = String.valueOf(newValue);
			} else {
				char cv = valueRangeEnd.charAt(0);
				if (cv < newValue) {
					valueRangeEnd = String.valueOf(newValue);
				}
			}
		}
	}

	public void checkValueRange(Double newValue) {
		if (newValue != null) {
			if (valueRangeStart == null || valueRangeStart.isEmpty()) {
				valueRangeStart = String.valueOf(newValue);
			} else {
				double cv = Double.valueOf(valueRangeStart);
				if (cv > newValue) {
					valueRangeStart = String.valueOf(newValue);
				}
			}
			if (valueRangeEnd == null || valueRangeEnd.isEmpty()) {
				valueRangeEnd = String.valueOf(newValue);
			} else {
				double cv = Double.valueOf(valueRangeEnd);
				if (cv < newValue) {
					valueRangeEnd = String.valueOf(newValue);
				}
			}
		}
	}

	public void checkValueRange(Float newValue) {
		if (newValue != null) {
			if (valueRangeStart == null || valueRangeStart.isEmpty()) {
				valueRangeStart = String.valueOf(newValue);
			} else {
				float cv = Float.valueOf(valueRangeStart);
				if (cv > newValue) {
					valueRangeStart = String.valueOf(newValue);
				}
			}
			if (valueRangeEnd == null || valueRangeEnd.isEmpty()) {
				valueRangeEnd = String.valueOf(newValue);
			} else {
				float cv = Float.valueOf(valueRangeEnd);
				if (cv < newValue) {
					valueRangeEnd = String.valueOf(newValue);
				}
			}
		}
	}

	public void checkValueRange(Integer newValue) {
		if (newValue != null) {
			if (valueRangeStart == null || valueRangeStart.isEmpty()) {
				valueRangeStart = String.valueOf(newValue);
			} else {
				int cv = Integer.valueOf(valueRangeStart);
				if (cv > newValue) {
					valueRangeStart = String.valueOf(newValue);
				}
			}
			if (valueRangeEnd == null || valueRangeEnd.isEmpty()) {
				valueRangeEnd = String.valueOf(newValue);
			} else {
				int cv = Integer.valueOf(valueRangeEnd);
				if (cv < newValue) {
					valueRangeEnd = String.valueOf(newValue);
				}
			}
		}
	}

	public void checkValueRange(Short newValue) {
		if (newValue != null) {
			if (valueRangeStart == null || valueRangeStart.isEmpty()) {
				valueRangeStart = String.valueOf(newValue);
			} else {
				short cv = Short.valueOf(valueRangeStart);
				if (cv > newValue) {
					valueRangeStart = String.valueOf(newValue);
				}
			}
			if (valueRangeEnd == null || valueRangeEnd.isEmpty()) {
				valueRangeEnd = String.valueOf(newValue);
			} else {
				short cv = Short.valueOf(valueRangeEnd);
				if (cv < newValue) {
					valueRangeEnd = String.valueOf(newValue);
				}
			}
		}
	}

	public void checkValueRange(Byte newValue) {
		if (newValue != null) {
			if (valueRangeStart == null || valueRangeStart.isEmpty()) {
				valueRangeStart = String.valueOf(newValue);
			} else {
				byte cv = Byte.valueOf(valueRangeStart);
				if (cv > newValue) {
					valueRangeStart = String.valueOf(newValue);
				}
			}
			if (valueRangeEnd == null || valueRangeEnd.isEmpty()) {
				valueRangeEnd = String.valueOf(newValue);
			} else {
				byte cv = Byte.valueOf(valueRangeEnd);
				if (cv < newValue) {
					valueRangeEnd = String.valueOf(newValue);
				}
			}
		}
	}

	public void checkValueRange(BigDecimal newValue) {
		if (newValue != null) {
			if (valueRangeStart == null || valueRangeStart.isEmpty()) {
				valueRangeStart = String.valueOf(newValue);
			} else {
				BigDecimal cv = new BigDecimal(valueRangeStart);
				if (cv.compareTo(newValue) > 0) {
					valueRangeStart = String.valueOf(newValue);
				}
			}
			if (valueRangeEnd == null || valueRangeEnd.isEmpty()) {
				valueRangeEnd = String.valueOf(newValue);
			} else {
				BigDecimal cv = new BigDecimal(valueRangeEnd);
				if (cv.compareTo(newValue) < 0) {
					valueRangeEnd = String.valueOf(newValue);
				}
			}
		}
	}

	public void checkValueRange(BigInteger newValue) {
		if (newValue != null) {
			if (valueRangeStart == null || valueRangeStart.isEmpty()) {
				valueRangeStart = String.valueOf(newValue);
			} else {
				BigInteger cv = new BigInteger(valueRangeStart);
				if (cv.compareTo(newValue) > 0) {
					valueRangeStart = String.valueOf(newValue);
				}
			}
			if (valueRangeEnd == null || valueRangeEnd.isEmpty()) {
				valueRangeEnd = String.valueOf(newValue);
			} else {
				BigInteger cv = new BigInteger(valueRangeEnd);
				if (cv.compareTo(newValue) < 0) {
					valueRangeEnd = String.valueOf(newValue);
				}
			}
		}
	}

	public void addCountInput(Number in) {
		if (in != null) {
			countInput = countInput + in.intValue();
		}
	}
	
	public void subCountInput(Number in) {
		if (in != null) {
			countInput = countInput - in.intValue();
		}
	}

	public void addCountOutput(Number out) {
		if (out != null) {
			countOutput = countOutput + out.intValue();
		}
	}
	
	public void addCountUpdate(Number out) {
		if (out != null) {
			countUpdate = countUpdate + out.intValue();
		}
	}

	public void subCountOutput(Number out) {
		if (out != null) {
			countOutput = countOutput - out.intValue();
		}
	}

	public void subCountUpdate(Number out) {
		if (out != null) {
			countUpdate = countUpdate - out.intValue();
		}
	}

	public void addCountReject(Number rej) {
		if (rej != null) {
			countReject = countReject + rej.intValue();
		}
	}
	
	public void subCountReject(Number rej) {
		if (rej != null) {
			countReject = countReject - rej.intValue();
		}
	}

	public void addCountDelete(Number in) {
		if (in != null) {
			countDelete = countDelete + in.intValue();
		}
	}
	
	public void subCountDelete(Number in) {
		if (in != null) {
			countDelete = countDelete - in.intValue();
		}
	}

    public void resetCounter() {
    	countOutput = 0;
    	countInput = 0;
    	countReject = 0;
    	returnCode = 0;
    	returnMessage = null;
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
	
	public boolean isRootJob() {
		if (rootJobGuid == null) {
			// if there is no root guid the current job is root
			return true;
		} else {
			if (guid.equals(rootJobGuid)) {
				return true;
			} else {
				return false;
			}
		}
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

}
