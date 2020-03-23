package io.gr1d.portal.billing.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ToStringConfig {
	@Autowired
	private ObjectMapper objectMapper;
	
	@PostConstruct
	public void setToStringObjectMapper() {
		ToString.init(objectMapper);
	}
	
}
