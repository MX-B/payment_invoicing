package io.gr1d.portal.billing.api.gravitee;

import org.springframework.cloud.openfeign.FeignClientProperties.FeignClientConfiguration;
import org.springframework.context.annotation.Bean;

import feign.RequestInterceptor;

public class GraviteeFeignConfiguration extends FeignClientConfiguration {
	
	@Bean
	public RequestInterceptor requestInterceptor(final GraviteeLoginApi loginApi) {
		return new GraviteeRequestInterceptor(loginApi);
	}
	
}
