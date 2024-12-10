package de.cimt.talendcomp.jobinstanceservice;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestJobDetailCounter {

	@Test
	public void test() {
		String json = "{\"name\":\"testjob\",\"type\":\"input\",\"value\":10}";
		ObjectMapper mapper = new ObjectMapper(null);
	}

}
