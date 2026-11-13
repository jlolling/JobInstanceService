package de.cimt.talendcomp.jobinstanceservice;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestJobInstanceStatus {

	@Test
	public void testJsonToObject() throws Exception {
		String name = "test-job";
		String project = "input";
		long id = 10l; 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = sdf.parse("2025-01-13 18:23:55");
		long ut = startDate.getTime();
		String json = "{\"job_name\":\"" + name + "\",\"project\":\"" + project + "\",\"job_instance_id\":" + id + ",\"start_date\":" + ut + ",\"named-counters\":[{\"name\":\"mycounter1\",\"type\":\"input\",\"value\":999},{\"name\":\"mycounter2\",\"type\":\"output\",\"value\":111}]}";
		ObjectMapper mapper = new ObjectMapper();
		JobInstanceStatus c = mapper.readValue(json, JobInstanceStatus.class);
		assertEquals("name wrong", name, c.getName());
		assertEquals("project wrong", project, c.getProject());
		assertEquals("JobInstanceId wrong", id, c.getJobInstanceId());
		assertEquals("start_date wrong", startDate, c.getStartDate());
		assertEquals("wrong number named counters", 2, c.getCounters().size());
	}

	@Test
	public void testObjectToJson() throws Exception {
		JobInstanceStatus s = new JobInstanceStatus();
		s.setJobInstanceId(1000l);
		s.setName("TestJob");
		s.setGuid("123-456-789");
		s.setHostName("localhost");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = sdf.parse("2025-01-13 18:23:55");
		long ut = startDate.getTime();
		s.setStartDate(startDate);
		JobDetailCounter c = new JobDetailCounter("mycounter1", "input", 999);
		s.addJobDetailCounter(c);
		c = new JobDetailCounter("mycounter2", "output", 111);
		s.addJobDetailCounter(c);
		ObjectMapper mapper = new ObjectMapper();
		String n = mapper.writeValueAsString(s);
		System.out.println(n);
		String expected = "{\"rootJob\":true,\"job_instance_id\":1000,\"root_job_instance_id\":0,\"job_guid\":\"123-456-789\",\"job_name\":\"TestJob\",\"project\":null,\"root_job_guid\":null,\"parent_job_guid\":null,\"job_info\":null,\"external_job_id\":null,\"task_name\":null,\"work_item\":null,\"job_result\":null,\"start_date\":" + ut + ",\"stop_date\":null,\"time_range_start\":null,\"time_range_end\":null,\"value_range_start\":null,\"value_range_end\":null,\"count_input\":0,\"count_output\":0,\"count_update\":0,\"count_reject\":0,\"count_delete\":0,\"host_name\":\"localhost\",\"host_pid\":0,\"host_user\":null,\"return_code\":0,\"return_message\":null,\"named-counters\":[{\"name\":\"mycounter1\",\"type\":\"input\",\"value\":999},{\"name\":\"mycounter2\",\"type\":\"output\",\"value\":111}]}";
		String actual = n;
		assertEquals("Wrong json", expected, actual);
	}

}
