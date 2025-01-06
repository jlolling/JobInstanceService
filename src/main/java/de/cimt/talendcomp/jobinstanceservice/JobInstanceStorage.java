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

import java.util.List;

public interface JobInstanceStorage {
	
	/**
	 * Creates and entry in job_instance_status and returns the job_instance_id
	 * @param jobInfo
	 * @return job_instance_id
	 * @throws Exception
	 */
	public long createEntry(JobInstanceStatus jobInfo) throws Exception;
	
	/**
	 * Update the job instance entry with metric and result info
	 * @param jobInfo
	 * @throws Exception
	 */
	public void updateEntry(JobInstanceStatus jobInfo) throws Exception;
	
	/**
	 * Returns the job_instance_id for a job
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	public long getJobInstanceIdByJobGuid(String guid) throws Exception;
	
	/**
	 * Select job instance ids by various criteria.
	 * null values in the parameters means this criteria is not used for selection
	 * @param excludeJobName
	 * @param includeJobNames comma separated List of jobs to select
	 * @param taskName
	 * @param workItem
	 * @param withInput
	 * @param withOutput
	 * @param successful it set to null=no criteria otherwise true or false
	 * @param running
	 * @param returnCode
	 * @param okReturnCodes - comma separated list of return codes for success
	 * @param beforeJobInstanceId
	 * @param rootJobInstanceId
	 * @return List of job instance ids
	 * @throws Exception
	 */
	public List<Long> select(
			String excludeJobName,
			String includeJobNames,
			String taskName,
			String workItem,
			Boolean withInput, 
			Boolean withOutput, 
			Boolean successful, 
			Boolean running,
			Integer returnCode,
			String okResultCodes,
			Long beforeJobInstanceId, 
			Long rootJobInstanceId) throws Exception;

	/**
	 * Returns a particular job instance status
	 * @param jobInstanceId
	 * @return JobInstanceStatus object for the given ID
	 * @throws Exception
	 */
	public JobInstanceStatus getJobInstanceStatus(long jobInstanceId) throws Exception;
	
	/**
	 * Write job detail counters
	 * @param listCounters
	 * @param jobInstanceId
	 * @throws Exception
	 */
	public void writeCounters(List<JobDetailCounter> listCounters, long jobInstanceId) throws Exception;
	
}
