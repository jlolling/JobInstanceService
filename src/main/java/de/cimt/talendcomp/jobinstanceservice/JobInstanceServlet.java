package de.cimt.talendcomp.jobinstanceservice;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.ee10.servlet.DefaultServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JobInstanceServlet extends DefaultServlet {

	private static Logger log = LogManager.getLogger(JobInstanceServlet.class);
	private static final long serialVersionUID = 1L;
	private JobInstanceStorage storage = null;
	public static final String path = "/job-instance";
	private String propertiesFilePath = null;
	protected final static ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void init() throws ServletException {
		super.init();
		// setup the JobInstanceStorage
		if (propertiesFilePath == null) {
			throw new ServletException("Properties file path for db pool not set!");
		}
		JDBCJobInstanceStorage jdbc = new JDBCJobInstanceStorage();
		try {
			jdbc.initialize(propertiesFilePath);
		} catch (Exception e) {
			throw new ServletException("Init jdbc storage failed: " + e.getMessage(), e);
		}
		storage = jdbc;
	}

	/**
	 * get status entries
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		String path = request.getRequestURI();
		response.setCharacterEncoding("UTF-8"); // must be done before we get the writer!
		// check if the path addresses one instance status directly
		Long jobInstanceId = JobInstanceStorage.extractJobInstanceIdFromPath(path);
		if (jobInstanceId != null) {
			try {
				JobInstanceStatus status = storage.getJobInstanceStatus(jobInstanceId);
				if (status != null) {
					// return the status as response
					response.setHeader("total-rows", "1");
					response.setContentType("application/json; charset=utf-8");
					final Writer out = response.getWriter();
					try (BufferedWriter br = new BufferedWriter(out)) {
						String json = objectMapper.writeValueAsString(status);
						br.write(json);						
					} catch (Exception e) {
						sendError(response, 500, "Processing job-instance-status response failed: " + e.getMessage());
						return;
					}
				} else {
					sendError(response, 404, "No job-instance-status found for ID=" + jobInstanceId);
					return;
				}
			} catch (Exception e) {
				sendError(response, 500, "Get JobInstanceStatus for ID=" + jobInstanceId + " failed: " + e.getMessage());
				return;
			}
		} else {
			List<NameValuePair> params = new ArrayList<>(); // dummy empty list
			try {
				params = new URIBuilder(path).getQueryParams();
				Boolean returnObjects = getValueAsBoolean("return_objects", params);
				if (returnObjects != null && returnObjects == true) {
					List<JobInstanceStatus> list = storage.selectObjects(
							getValueAsString("exclude_job", params),
							getValueAsString("include_jobs", params),
							getValueAsString("task_name", params),
							getValueAsString("work_item", params),
							getValueAsBoolean("with_input", params),
							getValueAsBoolean("with_output", params),
							getValueAsBoolean("is_successful", params),
							getValueAsBoolean("is_running", params),
							getValueAsInteger("return_code", params),
							getValueAsLong("before_job_instance_id", params),
							getValueAsLong("after_job_instance_id", params),
							getValueAsLong("root_job_instance_id", params));
					response.setHeader("total-rows", String.valueOf(list.size()));
					response.setContentType("application/json; charset=utf-8");
					final Writer out = response.getWriter();
					try (BufferedWriter br = new BufferedWriter(out)) {
						String json = objectMapper.writeValueAsString(list);
						br.write(json);						
					} catch (Exception e) {
						sendError(response, 500, "Send job-instance-status (objects) response failed: " + e.getMessage());
						return;
					}
				} else {
					List<Long> list = storage.selectIds(
							getValueAsString("exclude_job", params),
							getValueAsString("include_jobs", params),
							getValueAsString("task_name", params),
							getValueAsString("work_item", params),
							getValueAsBoolean("with_input", params),
							getValueAsBoolean("with_output", params),
							getValueAsBoolean("is_successful", params),
							getValueAsBoolean("is_running", params),
							getValueAsInteger("return_code", params),
							getValueAsLong("before_job_instance_id", params),
							getValueAsLong("after_job_instance_id", params),
							getValueAsLong("root_job_instance_id", params));
					response.setHeader("total-rows", String.valueOf(list.size()));
					response.setContentType("application/json; charset=utf-8");
					final Writer out = response.getWriter();
					try (BufferedWriter br = new BufferedWriter(out)) {
						String json = objectMapper.writeValueAsString(list);
						br.write(json);						
					} catch (Exception e) {
						sendError(response, 500, "Send job-instance-status (ids) response failed: " + e.getMessage());
						return;
					}
				}
			} catch (URISyntaxException e) {
				sendError(response, 403, "Extract params from query: " + request.getQueryString() + " failed: " + e.getMessage());
				return;
			} catch (Exception e1) {
				sendError(response, 500, "select job-instance_status failed: " + e1.getMessage());
				return;
			}
		}
	}
	
	private String getValueAsString(String paramName, List<NameValuePair> params) {
		String value = null;
		for (NameValuePair param : params) {
			if (param.getName().equalsIgnoreCase(paramName)) {
				value = param.getValue();
				break;
			}
		}
		return value;
	}
	
	private Long getValueAsLong(String paramName, List<NameValuePair> params) {
		Long value = null;
		String s = getValueAsString(paramName, params);
		if (s != null && s.isBlank() == false) {
			value = Long.valueOf(s);
		}
		return value;
	}

	private Integer getValueAsInteger(String paramName, List<NameValuePair> params) {
		Integer value = null;
		String s = getValueAsString(paramName, params);
		if (s != null && s.isBlank() == false) {
			value = Integer.valueOf(s);
		}
		return value;
	}

	private Boolean getValueAsBoolean(String paramName, List<NameValuePair> params) {
		Boolean value = null;
		String s = getValueAsString(paramName, params);
		if (s != null && s.isBlank() == false) {
			value = Boolean.valueOf(s);
		}
		return value;
	}

	/**
	 * Create status entry
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8"); // must be done before we get the writer!
		Reader r = request.getReader();
		String payload = IOUtils.toString(r);
		JobInstanceStatus status = null;
		try {
			status = objectMapper.readValue(payload, JobInstanceStatus.class);
		} catch (Exception e) {
			sendError(response, 403, "Parse payload to JobInstanceStatus from payload: " + payload + "\nfailed: " + e.getMessage());
			return;
		}
		try {
			storage.createEntry(status);
		} catch (Exception e) {
			sendError(response, 500, "Create job-instance_status failed: " + e.getMessage());
			return;
		}
		// build response
		ObjectNode rn = objectMapper.createObjectNode();
		rn.put("job_instance_id", status.getJobInstanceId());
		rn.put("root_job_instanceId", status.getProcessInstanceId());
		final Writer out = response.getWriter();
		response.setHeader("total-rows", "1");
		response.setContentType("application/json; charset=utf-8");
		try (BufferedWriter br = new BufferedWriter(out)) {
			br.write(rn.toString());						
		} catch (Exception e) {
			sendError(response, 500, "Send job_instance-status (ids) response failed: " + e.getMessage());
			return;
		}
	}

	/**
	 * Update a status entry
	 */
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Reader r = request.getReader();
		String payload = IOUtils.toString(r);
		JobInstanceStatus status = null;
		try {
			status = objectMapper.readValue(payload, JobInstanceStatus.class);
		} catch (Exception e) {
			sendError(response, 403, "Parse payload to JobInstanceStatus from payload: " + payload + "\nfailed: " + e.getMessage());
			return;
		}
		try {
			storage.updateEntry(status);
		} catch (Exception e) {
			sendError(response, 500, "Update job_instance_status failed: " + e.getMessage());
			return;
		}
		response.setStatus(204);
	}

	public void sendError(HttpServletResponse resp, int code, String message) {
		log.error(this.getClass().getSimpleName() + ": code: " + code + " message: " + message);
		try {
			resp.sendError(code, message);
		} catch (IOException e) {
			log.error("sendError code: " + code + " message: " + message + " failed: " + e.getMessage());
			e.printStackTrace();
		}
	}	
	
    @Override
    public void destroy() {
    	super.destroy();
    	if (storage != null) {
    		storage.close();
    	}
    }

	public String getPropertiesFilePath() {
		return propertiesFilePath;
	}

	public void setPropertiesFile(String propertiesFilePath) {
		this.propertiesFilePath = propertiesFilePath;
	}

}
