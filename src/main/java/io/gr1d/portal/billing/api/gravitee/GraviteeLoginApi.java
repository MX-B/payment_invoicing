package io.gr1d.portal.billing.api.gravitee;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "graviteeLoginApi", url = "${gr1d.portal.billing.api.gravitee.url}", configuration = GraviteeLoginFeignConfiguration.class)
public interface GraviteeLoginApi {
	
	@Cacheable("graviteeLogin")
	@PostMapping(path = "/management/user/login")
	GraviteeToken login();

}
