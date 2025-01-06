package de.cimt.talendcomp.jobinstanceservice;

import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JDBCJobInstanceStorage implements JobInstanceStorage {
	
	private static Logger log = LogManager.getLogger(JDBCJobInstanceStorage.class);
	private Properties properties = new Properties();
	private JDBCConnectionPool connectionPool = null;
	public static final String TABLE_JOB_INSTANCE_STATUS = "JOB_INSTANCE_STATUS";
	public static final String VIEW_JOB_INSTANCE_STATUS = "JOB_INSTANCE_STATUS_VIEW";
	public static final String JOB_INSTANCE_ID = "JOB_INSTANCE_ID";
	public static final String PROCESS_INSTANCE_ID = "PROCESS_INSTANCE_ID";
	public static final String PROCESS_INSTANCE_NAME = "PROCESS_INSTANCE_NAME";
	public static final String JOB_NAME = "JOB_NAME";
	public static final String JOB_PROJECT = "JOB_PROJECT";
	public static final String JOB_DISPLAY_NAME = "JOB_DISPLAY_NAME";
	public static final String JOB_GUID = "JOB_GUID";
	public static final String JOB_EXT_ID = "JOB_EXT_ID";
	public static final String JOB_INFO = "JOB_INFO";
	public static final String ROOT_JOB_GUID = "ROOT_JOB_GUID";
	public static final String JOB_WORK_ITEM = "WORK_ITEM";
	public static final String JOB_TIME_RANGE_START = "TIME_RANGE_START";
	public static final String JOB_TIME_RANGE_END = "TIME_RANGE_END";
	public static final String JOB_VALUE_RANGE_START = "VALUE_RANGE_START";
	public static final String JOB_VALUE_RANGE_END = "VALUE_RANGE_END";
	public static final String JOB_STARTED_AT = "JOB_STARTED_AT";
	public static final String JOB_ENDED_AT = "JOB_ENDED_AT";
	public static final String JOB_RESULT_ITEM = "JOB_RESULT";
	public static final String JOB_INPUT = "COUNT_INPUT";
	public static final String JOB_OUTPUT = "COUNT_OUTPUT";
	public static final String JOB_UPDATED = "COUNT_UPDATED";
	public static final String JOB_REJECTED = "COUNT_REJECTED";
	public static final String JOB_DELETED = "COUNT_DELETED";
	public static final String JOB_RETURN_CODE = "RETURN_CODE";
	public static final String JOB_RETURN_MESSAGE = "RETURN_MESSAGE";
	public static final String JOB_HOST_NAME = "HOST_NAME";
	public static final String JOB_HOST_PID = "HOST_PID";
	public static final String JOB_HOST_USER = "HOST_USER";
	private String tableName = TABLE_JOB_INSTANCE_STATUS;
	private String schemaName = null;
	public static final String COUNTER_TABLE = "JOB_INSTANCE_COUNTERS";
	private static final String COUNTER_NAME = "COUNTER_NAME";
	private static final String COUNTER_TYPE = "COUNTER_TYPE";
	private static final String COUNTER_VALUE = "COUNTER_VALUE";
	private String countertableName = COUNTER_TABLE;
	private String sequenceExpression = null;
	private boolean autoIncrementColumn = true;
	private boolean useGeneratedJID = false;
	private static JID jid = new JID();
	private int messageMaxLength = 1000;
	
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
	
	public Connection getConnection() throws Exception {
		return connectionPool.borrowConnection();
	}

	@Override
	public long createEntry(JobInstanceStatus jobInfo) throws Exception {
		if (jobInfo.isRootJob() == false && jobInfo.getProcessInstanceId() == 0) {
			long id = getJobInstanceIdByJobGuid(jobInfo.getRootJobGuid());
			if (id > 0) {
				jobInfo.setProcessInstanceId(id);
			} else if (jobInfo.getParentJobGuid() != null && jobInfo.getParentJobGuid().isEmpty() == false) {
				id = getJobInstanceIdByJobGuid(jobInfo.getParentJobGuid());
				if (id > 0) {
					jobInfo.setProcessInstanceId(id);
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("insert into ");
		sb.append(tableName);
		sb.append(" (");
		if (autoIncrementColumn == false || useGeneratedJID) {
			sb.append(JOB_INSTANCE_ID); // 1
			sb.append(",");
		}
		sb.append(JOB_NAME); // 2
		sb.append(",");
		sb.append(JOB_GUID); // 3
		sb.append(",");
		sb.append(ROOT_JOB_GUID); // 4
		sb.append(",");
		sb.append(JOB_WORK_ITEM); // 5
		sb.append(",");
		sb.append(JOB_TIME_RANGE_START); // 6
		sb.append(",");
		sb.append(JOB_TIME_RANGE_END); // 7
		sb.append(",");
		sb.append(JOB_VALUE_RANGE_START); // 8
		sb.append(",");
		sb.append(JOB_VALUE_RANGE_END); // 9
		sb.append(",");
		sb.append(JOB_STARTED_AT); // 10
		sb.append(",");
		sb.append(PROCESS_INSTANCE_ID); // 11
		sb.append(",");
		sb.append(JOB_HOST_NAME); // 12
		sb.append(",");
		sb.append(JOB_HOST_PID); // 13
		sb.append(",");
		sb.append(JOB_EXT_ID); // 14
		sb.append(",");
		sb.append(JOB_INFO); // 15
		sb.append(",");
		sb.append(JOB_HOST_USER); // 16
		sb.append(",");
		sb.append(JOB_PROJECT); // 17
		sb.append(",");
		sb.append(JOB_DISPLAY_NAME); // 18
		sb.append(")");
		sb.append(" values (");
		int paramIndex = 1;
		if (useGeneratedJID) {
			sb.append("?,");
		} else {
			if (autoIncrementColumn == false) {
				sb.append("("); // first field job_instance_id
				sb.append(sequenceExpression);
				sb.append("),");
			}
		}
		// parameter 1-18 or 2-19
		sb.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		String sql = sb.toString();
		log.debug(sql);
		try (Connection conn = getConnection()) {
			PreparedStatement psInsert = null;
			try {
				psInsert = conn.prepareStatement(sql,
						(autoIncrementColumn ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS));
			} catch (Exception nse) {
				// Snowflake and Exasol database does not support the return of generated keys
				psInsert = conn.prepareStatement(sql);
				autoIncrementColumn = false;
			}
			// #1
			// start set parameters
			if (useGeneratedJID) {
				long genJid = jid.createJID();
				log.debug("Use generated job_instance_id=" + genJid);
				psInsert.setLong(paramIndex++, genJid);
			}
			psInsert.setString(paramIndex++, jobInfo.getName());
			if (jobInfo.getGuid() == null) {
				throw new IllegalStateException("Job guid is null. Please call setJobGuid(String) before!");
			}
			psInsert.setString(paramIndex++, jobInfo.getGuid());
			if (jobInfo.getRootJobGuid() != null) {
				psInsert.setString(paramIndex++, jobInfo.getRootJobGuid());
			} else {
				psInsert.setNull(paramIndex++, Types.VARCHAR);
			}
			if (jobInfo.getWorkItem() != null) {
				psInsert.setString(paramIndex++, jobInfo.getWorkItem());
			} else {
				psInsert.setNull(paramIndex++, Types.VARCHAR);
			}
			if (jobInfo.getTimeRangeStart() != null) {
				psInsert.setTimestamp(paramIndex++, new Timestamp(jobInfo.getTimeRangeStart().getTime()));
			} else {
				psInsert.setNull(paramIndex++, Types.TIMESTAMP);
			}
			if (jobInfo.getTimeRangeEnd() != null) {
				psInsert.setTimestamp(paramIndex++, new Timestamp(jobInfo.getTimeRangeEnd().getTime()));
			} else {
				psInsert.setNull(paramIndex++, Types.TIMESTAMP);
			}
			if (jobInfo.getValueRangeStart() != null) {
				psInsert.setString(paramIndex++, jobInfo.getValueRangeStart());
			} else {
				psInsert.setNull(paramIndex++, Types.VARCHAR);
			}
			if (jobInfo.getValueRangeEnd() != null) {
				psInsert.setString(paramIndex++, jobInfo.getValueRangeEnd());
			} else {
				psInsert.setNull(paramIndex++, Types.VARCHAR);
			}
			if (jobInfo.getStartDate() == null) {
				throw new IllegalArgumentException("Job start date is null. Please call setJobStartedAt(long) before!");
			}
			psInsert.setTimestamp(paramIndex++, new Timestamp(jobInfo.getStartDate().getTime()));
			psInsert.setLong(paramIndex++, jobInfo.getProcessInstanceId());
			psInsert.setString(paramIndex++, jobInfo.getHostName());
			psInsert.setInt(paramIndex++, jobInfo.getHostPid());
			psInsert.setString(paramIndex++, jobInfo.getExtJobId());
			psInsert.setString(paramIndex++, jobInfo.getJobInfo());
			psInsert.setString(paramIndex++, jobInfo.getHostUser());
			psInsert.setString(paramIndex++, jobInfo.getProject());
			if (jobInfo.getTaskName() != null) {
				psInsert.setString(paramIndex++, jobInfo.getTaskName());
			} else {
				psInsert.setNull(paramIndex++, Types.VARCHAR);
			}
			int count = psInsert.executeUpdate();
			if (count == 0) {
				throw new SQLException("No dataset inserted!");
			}
			long currentJobInstanceId = -1;
			if (useGeneratedJID) {
				psInsert.close();
				currentJobInstanceId = jid.getJID();
			} else if (autoIncrementColumn) {
				// sometimes this does not work
				ResultSet rsKeys = psInsert.getGeneratedKeys();
				if (rsKeys.next()) {
					currentJobInstanceId = rsKeys.getLong(1);
				}
				rsKeys.close();
				psInsert.close();
			} else {
				psInsert.close();
				currentJobInstanceId = getJobInstanceIdByJobGuid(jobInfo.getGuid());
			}
			jobInfo.setJobInstanceId(currentJobInstanceId);
		}
		if (jobInfo.getJobInstanceId() == -1) {
			throw new SQLException("No job_instances entry found for jobGuid=" + jobInfo.getGuid());
		}
		return jobInfo.getJobInstanceId();
	}

	@Override
	public void updateEntry(JobInstanceStatus jobInfo) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("update ");
		sb.append(tableName);
		sb.append(" set ");
		sb.append(JOB_ENDED_AT); // 1
		sb.append("=?,");
		sb.append(JOB_RESULT_ITEM); // 2
		sb.append("=?,");
		sb.append(JOB_TIME_RANGE_START); // 3
		sb.append("=?,");
		sb.append(JOB_TIME_RANGE_END); // 4
		sb.append("=?,");
		sb.append(JOB_INPUT); // 5
		sb.append("=?,");
		sb.append(JOB_OUTPUT); // 6
		sb.append("=?,");
		sb.append(JOB_REJECTED); // 7
		sb.append("=?,");
		sb.append(JOB_DELETED); // 8
		sb.append("=?,");
		sb.append(JOB_RETURN_CODE); // 9
		sb.append("=?,");
		sb.append(JOB_RETURN_MESSAGE); // 10
		sb.append("=?,");
		sb.append(JOB_VALUE_RANGE_START); // 11
		sb.append("=?,");
		sb.append(JOB_VALUE_RANGE_END); // 12
		sb.append("=?,");
		sb.append(JOB_UPDATED); // 13
		sb.append("=? ");
		sb.append("where ");
		sb.append(JOB_INSTANCE_ID); // 14
		sb.append("=?");
		String sql = sb.toString();
		try (Connection connection = getConnection()) {
			PreparedStatement psUpdate = connection.prepareStatement(sql);
			int paramIndex = 1;
			psUpdate.setTimestamp(paramIndex++, new Timestamp(System.currentTimeMillis()));
			if (jobInfo.getJobResult() != null) {
				psUpdate.setString(paramIndex++, jobInfo.getJobResult());
			} else {
				psUpdate.setNull(paramIndex++, Types.VARCHAR);
			}
			if (jobInfo.getTimeRangeStart() != null) {
				psUpdate.setTimestamp(paramIndex++, new Timestamp(jobInfo.getTimeRangeStart().getTime()));
			} else {
				psUpdate.setNull(paramIndex++, Types.TIMESTAMP);
			}
			if (jobInfo.getTimeRangeEnd() != null) {
				psUpdate.setTimestamp(paramIndex++, new Timestamp(jobInfo.getTimeRangeEnd().getTime()));
			} else {
				psUpdate.setNull(paramIndex++, Types.TIMESTAMP);
			}
			psUpdate.setInt(paramIndex++, jobInfo.getCountInput());
			psUpdate.setInt(paramIndex++, jobInfo.getCountOutput());
			psUpdate.setInt(paramIndex++, jobInfo.getCountReject());
			psUpdate.setInt(paramIndex++, jobInfo.getCountDelete());
			psUpdate.setInt(paramIndex++, jobInfo.getReturnCode());
			psUpdate.setString(paramIndex++, enforceTextLength(jobInfo.getReturnMessage(), messageMaxLength, 1));
			if (jobInfo.getValueRangeStart() != null) {
				psUpdate.setString(paramIndex++, jobInfo.getValueRangeStart());
			} else {
				psUpdate.setNull(paramIndex++, Types.VARCHAR);
			}
			if (jobInfo.getValueRangeEnd() != null) {
				psUpdate.setString(paramIndex++, jobInfo.getValueRangeEnd());
			} else {
				psUpdate.setNull(paramIndex++, Types.VARCHAR);
			}
			psUpdate.setInt(paramIndex++, jobInfo.getCountUpdate());
			psUpdate.setLong(paramIndex++, jobInfo.getJobInstanceId());
			int count = psUpdate.executeUpdate();
			if (count != 1) {
				throw new Exception("Update of job_instance_status id=" + jobInfo.getJobInstanceId() + " failed because no entry was updated!");
			}
			psUpdate.close();
		}
	}

	@Override
	public long getJobInstanceIdByJobGuid(String jobGuid) throws Exception {
		if (jobGuid == null || jobGuid.trim().isEmpty()) {
			throw new IllegalArgumentException("jobGuid cannot be null or empty");
		}
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append(JOB_INSTANCE_ID);
		sb.append(" from ");
		sb.append(tableName);
		sb.append(" where ");
		sb.append(JOB_GUID);
		sb.append("=? order by ");
		sb.append(JOB_STARTED_AT);
		sb.append(" desc");
		String sql = sb.toString();
		log.debug(sql);
		long id = 0;
		try (Connection conn = getConnection()) {
			PreparedStatement psSelect = conn.prepareStatement(sql);
			psSelect.setString(1, jobGuid);
			ResultSet rs = psSelect.executeQuery();
			if (rs.next()) {
				id = rs.getLong(1);
			}
			rs.close();
			psSelect.close();
		} catch (Exception e) {
			throw new Exception("getJobInstanceIdByJobGuid for jobGuid=" + jobGuid + " failed: " + e.getMessage(), e);
		}
		return id;
	}

	@Override
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
			Long rootJobInstanceId) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append(JOB_INSTANCE_ID);
		sb.append(" from ");
		sb.append(tableName);
		sb.append(" where 1=1");
		if (excludeJobName != null) {
			sb.append(" and ");
			sb.append(JOB_NAME);
			sb.append(" <> '");
			sb.append(excludeJobName.trim());
			sb.append("'");
		}
		if (includeJobNames != null) {
			String[] array = includeJobNames.split(",");
			sb.append(" and ");
			sb.append(JOB_NAME);
			sb.append(" in (");
			boolean firstLoop = true;
			for (String jobName : array) {
				if (firstLoop) {
					firstLoop = false;
				} else {
					sb.append(",");
				}
				sb.append("'");
				sb.append(jobName.trim());
				sb.append("'");
			}
			sb.append(")");
		}
		if (taskName != null) {
			sb.append(" and ");
			sb.append(JOB_DISPLAY_NAME);
			sb.append(" = '");
			sb.append(taskName);
			sb.append("'");
		} 
		if (workItem != null) {
			sb.append(" and ");
			sb.append(JOB_WORK_ITEM);
			sb.append(" = '");
			sb.append(workItem.trim());
			sb.append("'");
		}
		if (withInput != null) {
			if (withInput) {
				sb.append(" and ");
				sb.append(JOB_INPUT);
				sb.append(" > 0");
			} else {
				sb.append(" and ");
				sb.append(JOB_INPUT);
				sb.append(" = 0");
			}
		}
		if (withOutput != null) {
			if (withOutput) {
				sb.append(" and (");
				sb.append(JOB_OUTPUT);
				sb.append(" > 0 or ");
				sb.append(JOB_UPDATED);
				sb.append(" > 0 or ");
				sb.append(JOB_DELETED);
				sb.append(" > 0)");
			} else {
				sb.append(" and (");
				sb.append(JOB_OUTPUT);
				sb.append(" = 0 and ");
				sb.append(JOB_UPDATED);
				sb.append(" = 0 and ");
				sb.append(JOB_DELETED);
				sb.append(" = 0)");
			}
		}
		if (beforeJobInstanceId != null) {
			sb.append(" and ");
			sb.append(JOB_INSTANCE_ID);
			sb.append(" > ");
			sb.append(beforeJobInstanceId);
		} 
		if (rootJobInstanceId != null) {
			sb.append(" and ");
			sb.append(PROCESS_INSTANCE_ID);
			sb.append(" = ");
			sb.append(rootJobInstanceId);
		} 
		if (returnCode != null) {
			sb.append(" and ");
			sb.append(JOB_RETURN_CODE);
			sb.append(" = ");
			sb.append(returnCode);
		} 
		if (running != null) {
			sb.append(" and ");
			sb.append(JOB_ENDED_AT);
			if (running) {
				sb.append(" is null");
			} else {
				sb.append(" is not null");
			}
		} 
		if (successful != null) {
			sb.append(" and ");
			sb.append(JOB_RETURN_CODE);
			if (successful) {
				if (okResultCodes != null) {
					sb.append(" in (");
					sb.append(okResultCodes);
					sb.append(") ");
				} else {
					sb.append(" = 0 ");
				}
			} else {
				if (okResultCodes != null) {
					sb.append(" not in (");
					sb.append(okResultCodes);
					sb.append(") ");
				} else {
					sb.append(" > 0 ");
				}
			}
		}
		sb.append(" order by ");
		sb.append(JOB_STARTED_AT);
		String sql = sb.toString();
		log.debug(sql);
		List<Long> result = new ArrayList<Long>();
		try (Connection conn = getConnection()) {
			PreparedStatement psSelect = conn.prepareStatement(sql);
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				result.add(rs.getLong(1));
			}
			rs.close();
			psSelect.close();
		}
		return result;
	}

	private JobInstanceStatus getJobInstanceStatusFromResultSet(ResultSet rs) throws SQLException {
		JobInstanceStatus ji = new JobInstanceStatus();
		ji.setJobInstanceId(rs.getLong(JOB_INSTANCE_ID));
		ji.setName(rs.getString(JOB_NAME));
		ji.setJobInfo(rs.getString(JOB_INFO));
		ji.setGuid(rs.getString(JOB_GUID));
		ji.setStartDate(rs.getTimestamp(JOB_STARTED_AT));
		ji.setStopDate(rs.getTimestamp(JOB_ENDED_AT));
		ji.setWorkItem(rs.getString(JOB_WORK_ITEM));
		ji.setTimeRangeStart(rs.getTimestamp(JOB_TIME_RANGE_START));
		ji.setTimeRangeEnd(rs.getTimestamp(JOB_TIME_RANGE_END));
		ji.setValueRangeStart(rs.getString(JOB_VALUE_RANGE_START));
		ji.setValueRangeEnd(rs.getString(JOB_VALUE_RANGE_END));
		ji.setProcessInstanceId(rs.getLong(PROCESS_INSTANCE_ID));
		ji.setTaskName(rs.getString(JOB_DISPLAY_NAME));
		ji.setProcessInstanceId(rs.getLong(PROCESS_INSTANCE_ID));		
		ji.setJobResult(rs.getString(JOB_RESULT_ITEM));
		ji.setCountInput(rs.getInt(JOB_INPUT));
		ji.setCountOutput(rs.getInt(JOB_OUTPUT));
		ji.setCountUpdate(rs.getInt(JOB_UPDATED));
		ji.setCountReject(rs.getInt(JOB_REJECTED));
		ji.setCountDelete(rs.getInt(JOB_DELETED));
		ji.setHostName(rs.getString(JOB_HOST_NAME));
		ji.setHostPid(rs.getInt(JOB_HOST_PID));
		ji.setReturnCode(rs.getInt(JOB_RETURN_CODE));
		ji.setReturnMessage(rs.getString(JOB_RETURN_MESSAGE));
		ji.setExtJobId(rs.getString(JOB_EXT_ID));
		ji.setProject(rs.getString(JOB_PROJECT));
		return ji;
	}

	@Override
	public void writeCounters(List<JobDetailCounter> listCounters, long jobInstanceId) throws Exception {
		if (listCounters.isEmpty() == false) {
			StringBuilder sb = new StringBuilder();
			sb.append("insert into ");
			sb.append(countertableName);
			sb.append(" (");
			sb.append(JOB_INSTANCE_ID);
			sb.append(",");
			sb.append(COUNTER_NAME);
			sb.append(",");
			sb.append(COUNTER_TYPE);
			sb.append(",");
			sb.append(COUNTER_VALUE);
			sb.append(") values (?,?,?,?)");
			boolean hasValues = false;
			try (Connection connection = getConnection()) {
				PreparedStatement ps = connection.prepareStatement(sb.toString());
				for (JobDetailCounter entry : listCounters) {
					Integer value = entry.getValue();
					if (value != null) {
						ps.setLong(1, jobInstanceId);
						ps.setString(2, entry.getName());
						ps.setString(3, entry.getType());
						ps.setInt(4, value);
						ps.addBatch();
						hasValues = true;
					}
				}
				if (hasValues) {
					ps.executeBatch();
					if (connection.getAutoCommit() == false) {
						connection.commit();
					}
					ps.close();
				}
			} catch (SQLException sqle) {
				SQLException ne = sqle.getNextException();
				if (ne != null) {
					throw new Exception(sqle.getMessage() + ", Next Exception:" + ne.getMessage(), sqle);
				} else {
					throw sqle;
				}
			}
		}
	}

	@Override
	public JobInstanceStatus getJobInstanceStatus(long jobInstanceId) throws Exception {
		JobInstanceStatus jis = null;
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ");
		sb.append(tableName);
		sb.append(" where ");
		sb.append(JOB_INSTANCE_ID);
		sb.append(" = ? ");
		String sql = sb.toString();
		try (Connection conn = getConnection()) {
			PreparedStatement psSelect = conn.prepareStatement(sql);
			psSelect.setLong(1, jobInstanceId);
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				jis = getJobInstanceStatusFromResultSet(rs);
			}
			rs.close();
			psSelect.close();
		}
		return jis;
	}

	/**
	 * limits the message text to avoid overflow database field
	 * @param size to limit
	 * @param cutPosition 0= cuts at end, 1= cuts in the middle, 2=cuts at the start
	 * @return limited text
	 */
	public static String enforceTextLength(String message, int size, int cutPosition) {
		if (message != null && message.trim().isEmpty() == false) {
			message = message.trim();
			if (message.length() > size) {
				size = size - 3; // to have space for "..."
				if (cutPosition == 0) {
					return message.substring(0, size) + "...";
				} else if (cutPosition == 1) {
					StringBuilder sb = new StringBuilder();
					sb.append(message.substring(0, size / 2));
					sb.append("...");
					sb.append(message.substring(message.length() - size / 2, message.length()));
					return sb.toString();
				} else {
					return "..." + message.substring(message.length() - size);
				}
			} else {
				return message;
			}
		} else {
			return null;
		}
	}

	public void setMaxMessageLength(int messageMaxLength) {
		this.messageMaxLength = messageMaxLength;
	}

}
