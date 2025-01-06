package de.cimt.talendcomp.jobinstanceservice;

import java.io.IOException;

import org.eclipse.jetty.ee10.servlet.DefaultServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JobInstanceServlet extends DefaultServlet {

	private static final long serialVersionUID = 1L;
	private JobInstanceStorage storage = null;
	public static final String path = "/job-instance";

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(request, response);
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

	public JobInstanceStorage getStorage() {
		return storage;
	}

	public void setStorage(JobInstanceStorage storage) {
		this.storage = storage;
	}
	
	

}
