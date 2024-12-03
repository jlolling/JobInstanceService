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
	public long createEntry(JobInfo jobInfo) throws Exception;
	
	/**
	 * Update the job instance entry with metric and result info
	 * @param jobInfo
	 * @throws Exception
	 */
	public void updateEntry(JobInfo jobInfo) throws Exception;
	
	/**
	 * Returns the job_instance_id for a job
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	public long getJobInstanceIdByJobGuid(String guid) throws Exception;
	
	/**
	 * Returns the job run info for the previous run if the job
	 * @param jobInfo info about the current job
	 * @param successful - current job must be successful
	 * @param withInput - current job must have input
	 * @param withOutput - current job must have output
	 * @param sameRoot - current job must inside the same root job
	 * @param forWorkItem
	 * @return job-info for the previous run filtered by the criteria above
	 * @throws Exception
	 */
	public JobInfo retrievePreviousInstanceData(JobInfo jobInfo, boolean successful, boolean withInput, boolean withOutput, boolean sameRoot, boolean forWorkItem) throws Exception;

	/**
	 * Write job detail counters
	 * @param listCounters
	 * @throws Exception
	 */
	public void writeCounters(List<JobDetailCounter> listCounters) throws Exception;
	
}
