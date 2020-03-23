package io.gr1d.portal.billing.api.gravitee;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "graviteeApi", url = "${gr1d.portal.billing.api.gravitee.url}", configuration = GraviteeFeignConfiguration.class)
public interface GraviteeApi {
	
	@Cacheable("graviteeListUsers")
	@GetMapping(value = "/management/users?page={page}")
	GraviteePage<GraviteeUser> listUsers(@PathVariable("page") int page);
	
	@Cacheable("graviteeListApplications")
	@GetMapping(value = "/management/applications")
	List<GraviteeApplication> listApplications();
	
	@GetMapping(value = "/management/applications/{appId}/subscriptions?page={page}")
	GraviteePage<GraviteeApplicationSubscription> listApplicationSubscriptions(
			@PathVariable("appId") String appId, @PathVariable("page") int page);
	
	@GetMapping(value = "/management/apis/{apiId}/analytics?type=group_by&aggs=field:status&interval=1000000000&from={startDt}&to={finishDt}&query={appId}&field=status")
	GraviteeMetrics getMetrics(@PathVariable("apiId") String apiId, @PathVariable("appId") String appId,
							   @PathVariable("startDt") long startDt, @PathVariable("finishDt") long finishDt);

}
