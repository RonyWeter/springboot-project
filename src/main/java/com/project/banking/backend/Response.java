package com.project.banking.backend;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Response {

	private boolean success;
	
	@JsonProperty("status_code")
	private int statusCode;
	
	private String message;
	
	private Object data;
}
