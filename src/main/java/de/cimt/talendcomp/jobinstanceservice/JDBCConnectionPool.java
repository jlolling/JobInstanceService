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

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JDBCConnectionPool {

	private static Logger log = LogManager.getLogger(JDBCConnectionPool.class);
	protected String user;
	protected String password;
	protected String connectionUrl = null;
	protected String defaultCatalog = null;
	protected boolean testOnBorrow = true;
	//private boolean testWhileIdle = true;
	protected Integer timeIdleConnectionIsChecked = 30000;
	protected Integer timeBetweenChecks = 60000;
	protected Integer initialSize = 0;
	protected Integer maxCountConnectionsTotal = -1;
	protected Integer maxCountConnectionsIdle = -1;
	protected Integer maxWaitForConnection = 0;
	protected Integer numConnectionsPerCheck = 5;
	protected String driverClassName = null;
	private Collection<String> initSQL;
	private BasicDataSource dataSource = null;
	protected String validationQuery = null;
	protected String connectionProperties = null;
	private boolean autoCommit = false;
	private String jndiName = null;
	private boolean enableJMX = true;
	
	/**
	 * Constructor with necessary params
	 * @param connectionUrl
	 * @param user
	 * @param password
	 * @param databaseType
	 */
	public JDBCConnectionPool(String connectionUrl, String user, String password) {
		if (connectionUrl == null || connectionUrl.trim().isEmpty()) {
			throw new IllegalArgumentException("connection url can not be null or empty");
		} else {
			this.connectionUrl = connectionUrl;
		}
		if (user == null || user.trim().isEmpty()) {
			throw new IllegalArgumentException("User can not be null");
		} else if (password == null) {
			throw new IllegalArgumentException(
					"Password can not be null. At least empty String \"\" ");
		} else {
			this.user = user;
			this.password = password;
		}
	}
	
	/**
	 * Initialize the pool by a properties file.
	 * The properties mainly contains the connection pool properties.
	 * Please refer https://commons.apache.org/proper/commons-dbcp/configuration.html
	 * The property defaultCatalog have to set for the database containing the tables
	 * @param propertiesFile
	 * @throws Exception
	 */
	public JDBCConnectionPool(Properties properties) throws Exception {
		if (properties == null) {
			throw new Exception("properties cannot be null");
		} else if (properties.isEmpty()) {
			throw new Exception("properties cannot be empty");
		}
		this.user = properties.getProperty("username");
		this.password = TalendContextPasswordUtil.decryptPassword(properties.getProperty("password", ""));
		this.connectionUrl = properties.getProperty("url");
		this.driverClassName = properties.getProperty("driverClassName");
		this.connectionProperties = properties.getProperty("connectionProperties");
		this.autoCommit = "true".equals(properties.getProperty("defaultAutoCommit"));
		this.defaultCatalog = properties.getProperty("defaultCatalog");
		this.testOnBorrow = "true".equals(properties.getProperty(properties.getProperty("testOnBorrow")));
		this.validationQuery = properties.getProperty("validationQuery");
		this.initialSize = Integer.parseInt(properties.getProperty("initialSize", "0"));
		this.maxCountConnectionsTotal = Integer.parseInt(properties.getProperty("maxTotal", "8"));
		this.maxCountConnectionsIdle = Integer.parseInt(properties.getProperty("maxIdle", "8"));
		this.maxWaitForConnection = Integer.parseInt(properties.getProperty("maxWaitMillis", "-1"));
		this.numConnectionsPerCheck = Integer.parseInt(properties.getProperty("numTestsPerEvictionRun", "3"));
		String sqls = properties.getProperty("connectionInitSqls");
		String[] array = sqls.split(";");
		for (String sql : array) {
			if (sql != null && sql.isBlank() == false) {
				if (this.initSQL == null) {
					this.initSQL = new ArrayList<String>();
				}
				this.initSQL.add(sql);
			}
		}
		this.timeIdleConnectionIsChecked = Integer.parseInt(properties.getProperty("minEvictableIdleTimeMillis"));
		this.timeBetweenChecks = Integer.parseInt(properties.getProperty("timeBetweenEvictionRunsMillis"));
	}

	/**
	 * load given driver
	 * @param driverClassName
	 * @throws SQLException
	 */
	private void loadDriver() throws Exception {
		if (driverClassName == null || driverClassName.trim().isEmpty()) {
			throw new IllegalArgumentException("driver can not be null or empty");
		}
		try {
			Class.forName(driverClassName.trim());
		} catch (ClassNotFoundException e) {
			throw new Exception("Could not load driver class: " + driverClassName, e);
		}
	}
	
	/**
	 * Creates the data sources and initialize the pool
	 * @throws Exception
	 * @throws Exception, UniversalConnectionPoolException
	 */
	public void initializePool() throws Exception {
		if (this.driverClassName == null) {
			throw new IllegalStateException("Please call method loadDriver before setup datasource");
		}
		loadDriver();
		if (this.connectionUrl == null) {
			throw new IllegalStateException("Please use method setConnectionString before initializePool()");
		}
		if (user == null || user.isBlank()) {
			throw new IllegalArgumentException("User can not be null ior empty");
		}
		// use org.apache.commons.dbcp2.BasicDataSource
		this.dataSource = new BasicDataSource();
		this.dataSource.setUsername(this.user);
		this.dataSource.setPassword(this.password);
		this.dataSource.setUrl(this.connectionUrl);
		this.dataSource.setTestOnBorrow(this.testOnBorrow);
		this.dataSource.setTestWhileIdle(true);
		this.dataSource.setMinEvictableIdle(Duration.ofSeconds(this.timeIdleConnectionIsChecked));
		this.dataSource.setDurationBetweenEvictionRuns(Duration.ofSeconds(this.timeBetweenChecks));
		this.dataSource.setInitialSize(this.initialSize);
		this.dataSource.setMaxTotal(this.maxCountConnectionsTotal);
		this.dataSource.setMaxIdle(this.maxCountConnectionsIdle);
		this.dataSource.setDefaultCatalog(this.defaultCatalog);
		if (enableJMX) {
			this.dataSource.setJmxName(buildJmxName());
		}
		//this.dataSource.setMaxIdle(this.maxIdle);
		if (this.maxWaitForConnection == 0) { 
			this.maxWaitForConnection = -1;
		}
		this.dataSource.setMaxWait(Duration.ofMillis(this.maxWaitForConnection));
		this.dataSource.setNumTestsPerEvictionRun(this.numConnectionsPerCheck);
		this.dataSource.setValidationQuery(this.validationQuery);
		if (initSQL != null && initSQL.isEmpty() == false) {
			this.dataSource.setConnectionInitSqls(this.initSQL);
		}
		this.dataSource.setDefaultAutoCommit(autoCommit);
		this.dataSource.setLifo(false);
		this.dataSource.setLogAbandoned(false);
		this.dataSource.setLogExpiredConnections(false);
		if (connectionProperties != null) {
			this.dataSource.setConnectionProperties(connectionProperties);
		}
		// create our first connection to detect connection problems right here
		try {
			Connection testConn = dataSource.getConnection();
			if (testConn == null) {
				throw new Exception("No initial data source available");
			} else {
				testConn.close();
				log.debug("Initial check connection pool: number active: " + dataSource.getNumActive() + "number idle: " + dataSource.getNumIdle());
			}
			log.info("Connection pool successful established and tested.");
		} catch (Exception e) {
			String message = "Test pool failed. URL=" + this.connectionUrl + " USER=" + this.user + ". Error message=" + e.getMessage();
			throw new Exception(message, e);
		}
	}
	
	/**
	 * Borrows a connection from the pool. To give it back simply close the connection
	 * @return connection
	 * @throws SQLException
	 */
	public Connection borrowConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
	private String prepareConnectionProperties(String properties) {
		if (properties != null) {
			if (properties.startsWith("?")) {
				properties = properties.substring(1);
			}
			properties = properties.replace("&", ";");
			return properties;
		} else {
			return "";
		}
	}

	/**
	 * set additional parameter separated by semicolon
	 * @param propertiesStr
	 * @throws SQLException
	 */
	public void setAdditionalProperties(String propertiesStr) throws SQLException {
		if (propertiesStr != null && propertiesStr.trim().isEmpty() == false) {
			this.connectionProperties = prepareConnectionProperties(propertiesStr.trim());
		} else {
			this.connectionProperties = null;
		}
	}
	
	/**
	 * close connection pool
	 * @throws Exception
	 */
	public void closePool() throws Exception {
		if (dataSource == null) {
			throw new IllegalStateException("Connection pool not set up");
		}
		this.dataSource.close();
	}

	public boolean getTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public Integer getTimeIdleConnectionIsChecked() {
		return timeIdleConnectionIsChecked;
	}

	/**
	 * time an connection can be in idle state before it is checked <br>
	 * required testWhileIdle = true<br>
	 * default = 60000ms
	 * 
	 * @param timeIdleConnectionIsChecked
	 */
	public void setTimeIdleConnectionIsChecked(Integer timeIdleConnectionIsChecked) {
		if (timeIdleConnectionIsChecked == null) {
			throw new IllegalArgumentException("timeIdleConnectionIsChecked can not be null");
		} else {
			this.timeIdleConnectionIsChecked = timeIdleConnectionIsChecked;
		}

	}

	public Integer getTimeBetweenChecks() {
		return timeBetweenChecks;
	}

	/**
	 * time between checks for connections in idle state<br>
	 * required testWhileIdle = true<br>
	 * default = 60000 Milli Sec (MySql), Oracle 60000 (Sec)
	 * 
	 * @param timeBetweenChecks
	 */
	public void setTimeBetweenChecks(Integer timeBetweenChecks) {
		if (timeBetweenChecks == null) {
			throw new IllegalArgumentException("timeBetweenChecks can not be null");
		} else {
			this.timeBetweenChecks = timeBetweenChecks;
		}

	}

	public Integer getInitialSize() {
		return initialSize;
	}

	/**
	 * default = 0
	 * 
	 * @param initialSize
	 */
	public void setInitialSize(Integer initialSize) {
		if (initialSize == null) {
			throw new IllegalArgumentException("initialSize can not be null");
		} else {
			this.initialSize = initialSize;
		}

	}

	public Integer getMaxTotal() {
		return maxCountConnectionsTotal;
	}

	public Integer getMaxIdle() {
		return maxCountConnectionsIdle;
	}

	/**
	 * max number of connections in pool<br>
	 * <br>
	 * default = 5
	 * 
	 * @param maxTotal
	 */
	public void setMaxTotal(Integer maxTotal) {
		if (maxTotal == null) {
			throw new IllegalArgumentException("maxTotal can not be null");
		} else {
			this.maxCountConnectionsTotal = maxTotal;
			this.maxCountConnectionsIdle = maxTotal; // max idle should be the same value as max total
		}

	}

	public Integer getMaxWaitForConnection() {
		return maxWaitForConnection;
	}

	/**
	 * Time to wait for connections if maxTotal size is reached<br>
	 * default = 0 
	 * 
	 * @param maxWaitForConnection
	 */
	public void setMaxWaitForConnection(Integer maxWaitForConnection) {
		if (maxWaitForConnection == null) {
			throw new IllegalArgumentException("maxWaitForConnection can not be null");
		} else {
			this.maxWaitForConnection = maxWaitForConnection;
		}
	}

	public Integer getNumConnectionsPerCheck() {
		return numConnectionsPerCheck;
	}

	/**
	 * number of connections in idle state that are checked <br>
	 * default = 5
	 * 
	 * @param numConnectionsPerCheck
	 */
	public void setNumConnectionsPerCheck(Integer numConnectionsPerCheck) {
		if (numConnectionsPerCheck == null) {
			throw new IllegalArgumentException("numConnectionsPerCheck can not be null");
		} else {
			this.numConnectionsPerCheck = numConnectionsPerCheck;
		}
	}
	
	public Collection<String> getInitSQL() {
		return initSQL;
	}

	/**
	 * set SQL that is fired before a connection become available in pool
	 * separated by semicolon
	 * @param initSQL
	 */
	public void setInitSQL(String initSQL) {
		if (initSQL != null && initSQL.trim().isEmpty() == false) {
			this.initSQL = new ArrayList<String>();
			String[] splitted = initSQL.split(";");
			for (String sql : splitted) {
				if (sql != null && sql.trim().isEmpty() == false) {
					this.initSQL.add(sql.trim());
				}
			}
		}
	}
	
	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		if (validationQuery != null && validationQuery.trim().isEmpty() == false) {
			this.validationQuery = validationQuery;
		}
	}
	
	public int getNumActiveConnections() {
		if (dataSource != null) {
			return dataSource.getNumActive();
		} else {
			return 0;
		}
	}

	public boolean isAutoCommit() {
		return autoCommit;
	}

	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public void setJndiName(String jndiName) {
		if (jndiName != null && jndiName.trim().isEmpty() == false) {
			this.jndiName = jndiName.trim();
		} else {
			this.jndiName = null;
		}
	}
	
	public String getJndiName() {
		return jndiName;
	}
	
	public String buildJmxName() {
		if (jndiName != null && jndiName.trim().isEmpty() == false) {
			return "de.cimt.talendcomp.connectionpool:type=BasicConnectionPool,jndiName="  + jndiName.trim().replace(':', '_');
		} else {
			return null;
		}
	}

	public boolean isEnableJMX() {
		return enableJMX;
	}

	public void setEnableJMX(Boolean enableJMX) {
		if (enableJMX != null) {
			this.enableJMX = enableJMX.booleanValue();
		}
	}
	
}
