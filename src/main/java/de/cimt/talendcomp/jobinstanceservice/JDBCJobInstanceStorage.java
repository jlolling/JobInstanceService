package de.cimt.talendcomp.jobinstanceservice;

import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JDBCJobInstanceStorage implements JobInstanceStorage {
	
	private static Logger log = LogManager.getLogger(JDBCJobInstanceStorage.class);
	private Properties properties = new Properties();
	private JDBCConnectionPool connectionPool = null;
	
	/**
	 * Initialize the storage by a properties file.
	 * The properties mainly contains the connection pool properties.
	 * Please refer https://commons.apache.org/proper/commons-dbcp/configuration.html
	 * The property defaultCatalog have to set for the database containing the tables
	 * The additional property schema is need for the database schema in which the tables exists
	 * @param propertiesFile
	 * @throws Exception
	 */
	public void initialize(String propertiesFile) throws Exception {
		File f = new File(propertiesFile);
		try {
			properties.load(new FileReader(f, Charset.forName("UTF-8")));
		} catch (Exception e) {
			throw new Exception("Load properties from file: " + f.getAbsolutePath() + " failed: " + e.getMessage(), e);
		}
		// setup connection pool
		connectionPool = new JDBCConnectionPool(properties);
	}

	@Override
	public long createEntry(JobInstanceStatus jobInfo) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateEntry(JobInstanceStatus jobInfo) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public long getJobInstanceIdByJobGuid(String guid) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Long> select(
			String jobName,
			String taskName,
			String workItem,
			Boolean withInput, 
			Boolean withOutput, 
			Boolean successful, 
			Boolean failed,
			Boolean running,
			Integer returnCode,
			Long beforeJobInstanceId, 
			Long rootJobInstanceId) throws Exception {
		return null;
	}

	@Override
	public void writeCounters(List<JobDetailCounter> listCounters) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public JobInstanceStatus getJobInstanceStatus(long jobInstanceId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
