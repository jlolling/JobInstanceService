package de.cimt.talendcomp.jobinstanceservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobDetailCounter {
	
	private String counterName;
	private String counterType;
	private Integer value;
	
	@JsonCreator
	public JobDetailCounter(
			@JsonProperty(value = "name", required = true)  String counterName, 
			@JsonProperty(value = "type", required = true) String counterType, 
			@JsonProperty(value = "value") Integer value) {
		if (counterName == null || counterName.isBlank()) {
			throw new IllegalArgumentException("name cannot be null or empty");
		}
		this.counterName = counterName;
		if (counterType == null || counterType.isBlank()) {
			throw new IllegalArgumentException("type cannot be null or empty");
		}
		this.counterType = counterType;
		this.value = value;
	}
	
	public String getName() {
		return counterName;
	}

	public void setName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("name cannot be null or empty");
		}
		this.counterName = name;
	}

	public String getType() {
		return counterType;
	}

	
	public void setType(String type) {
		if (type == null || type.isBlank()) {
			throw new IllegalArgumentException("type cannot be null or empty");
		}
		this.counterType = type;
	}

	public Integer getValue() {
		return value;
	}

}
