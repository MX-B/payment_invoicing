package io.gr1d.portal.billing.api;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.gr1d.portal.billing.bill.model.ApiPlan;
import io.gr1d.portal.billing.config.SubscriptionsFeignConfiguration;

/**
 * @author sergio.marcelino
 */
@FeignClient(name = "plansService", url = "${gr1d.portal.billing.api.subscriptions.url}", configuration = SubscriptionsFeignConfiguration.class)
public interface PlansServiceApi {
	
	@Cacheable("apiPlans")
	@GetMapping(value = "/api/{apiUuid}")
	ApiPlan getPlan(@PathVariable("apiUuid") String apiUuid);
	
}
