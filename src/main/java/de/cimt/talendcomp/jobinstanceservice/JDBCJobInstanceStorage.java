package de.cimt.talendcomp.jobinstanceservice;

import java.io.File;
import java.util.List;
import java.util.Properties;

public class JDBCJobInstanceStorage implements JobInstanceStorage {
	
	private Properties properties = new Properties();
	
	public void setPropertiesFile(String propertiesFile) throws Exception {
		File f = new File(propertiesFile);
		
	}

	@Override
	public long createEntry(JobInfo jobInfo) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateEntry(JobInfo jobInfo) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public long getJobInstanceIdByJobGuid(String guid) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public JobInfo retrievePreviousInstanceData(JobInfo jobInfo, boolean successful, boolean withInput,
	        boolean withOutput, boolean sameRoot, boolean forWorkItem) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeCounters(List<JobDetailCounter> listCounters) throws Exception {
		// TODO Auto-generated method stub

	}

}
