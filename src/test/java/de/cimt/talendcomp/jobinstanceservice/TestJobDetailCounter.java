package de.cimt.talendcomp.jobinstanceservice;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestJobDetailCounter {

	@Test
	public void testMapObject() throws JsonMappingException, JsonProcessingException {
		String name = "tablename";
		String type = "input";
		Integer value = 10; 
		String json = "{\"name\":\"" + name + "\",\"type\":\"" + type + "\",\"value\":" + value + "}";
		ObjectMapper mapper = new ObjectMapper();
		JobDetailCounter c = mapper.readValue(json, JobDetailCounter.class);
		assertEquals("name wrong", name, c.getName());
		assertEquals("type wrong", type, c.getType());
		assertEquals("value wrong", value, c.getValue());
	}

	@Test
	public void testMapObjectMissingValue() throws JsonMappingException, JsonProcessingException {
		String name = "tablename";
		String type = "input";
		String json = "{\"name\":\"" + name + "\",\"type\":\"" + type + "\"}";
		ObjectMapper mapper = new ObjectMapper();
		JobDetailCounter c = mapper.readValue(json, JobDetailCounter.class);
		assertEquals("name wrong", name, c.getName());
		assertEquals("type wrong", type, c.getType());
		assertTrue("value wrong", c.getValue() == null);
	}

	@Test
	public void testMapObjectMissingName() throws JsonMappingException, JsonProcessingException {
		String type = "input";
		String json = "{\"type\":\"" + type + "\"}";
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.readValue(json, JobDetailCounter.class);
			assertTrue("mandator but missing field name not detected", false);
		} catch (Exception e) {
			System.out.println(e);
			assertTrue(true);
		}
	}

	@Test
	public void testMapObjectNullType() throws JsonMappingException, JsonProcessingException {
		String name = "tablename";
		String json = "{\"name\":\"" + name + "\",\"type\":null}";
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.readValue(json, JobDetailCounter.class);
			assertTrue("null field type not detected", false);
		} catch (Exception e) {
			System.out.println(e);
			assertTrue(true);
		}
	}

	@Test
	public void testMapObjectBlankType() throws JsonMappingException, JsonProcessingException {
		String name = "tablename";
		String json = "{\"name\":\"" + name + "\",\"type\":\" \"}";
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.readValue(json, JobDetailCounter.class);
			assertTrue("blank field type not detected", false);
		} catch (Exception e) {
			System.out.println(e);
			assertTrue(true);
		}
	}
	
	@Test
	public void testObjectToJson() throws Exception {
		JobDetailCounter c = new JobDetailCounter("counter1", "input", 99);
		ObjectMapper mapper = new ObjectMapper();
		String actual = mapper.writeValueAsString(c);
		System.out.println(actual);
		String expected = "{\"name\":\"counter1\",\"type\":\"input\",\"value\":99}";
		assertEquals("Wrong json", expected, actual);
	}

}
