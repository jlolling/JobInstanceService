package de.cimt.talendcomp.jobinstanceservice;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.eclipse.jetty.ee10.servlet.DefaultServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JobInstanceServlet extends DefaultServlet {

	private static final long serialVersionUID = 1L;
	private JobInstanceStorage storage = null;
	public static final String path = "/job-instance";
	private String propertiesFilePath = null;

	@Override
	public void init() throws ServletException {
		super.init();
		// setup the JobInstanceStorage
		if (propertiesFilePath == null) {
			throw new ServletException("Properties file path for db pool not set!");
		}
		Properties poolProperties = new Properties();
		
		// load the properties from file
		storage = new JDBCJobInstanceStorage();
		// instantiate the pool
		
		// set properties
		
		// connect
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		String path = request.getRequestURI();
		List<NameValuePair> params = new ArrayList<>(); // dummy empty list
		try {
			params = new URIBuilder(path).getQueryParams();
		} catch (URISyntaxException e) {
			throw new ServletException("Extract params from query: " + request.getQueryString() + " failed: " + e.getMessage(), e);
		}
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(request, response);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPut(req, resp);
	}

	
	
	

}
