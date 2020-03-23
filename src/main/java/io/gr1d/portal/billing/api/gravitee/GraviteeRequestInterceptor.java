package io.gr1d.portal.billing.api.gravitee;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class GraviteeRequestInterceptor implements RequestInterceptor {
	
	private final GraviteeLoginApi loginApi;
	
	GraviteeRequestInterceptor(final GraviteeLoginApi loginApi) {
		this.loginApi = loginApi;
	}
	
	@Override
	public void apply(final RequestTemplate template) {
		final GraviteeToken token = loginApi.login();
		template.header("Authorization", "Bearer " + token.getToken());
	}
}
