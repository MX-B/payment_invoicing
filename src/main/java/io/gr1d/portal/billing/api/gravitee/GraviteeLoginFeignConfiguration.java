package io.gr1d.portal.billing.api.gravitee;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import feign.auth.BasicAuthRequestInterceptor;

public class GraviteeLoginFeignConfiguration {
	
	@Value("${gr1d.portal.billing.api.gravitee.username}")
	private String username;
	
	@Value("${gr1d.portal.billing.api.gravitee.password}")
	private String password;
	
	@Bean
	public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
		return new BasicAuthRequestInterceptor(username, password);
	}
	
}
