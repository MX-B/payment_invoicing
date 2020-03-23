package io.gr1d.portal.billing.api.gravitee;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.gr1d.portal.billing.bill.model.ApiMetricsInfo;
import io.gr1d.portal.billing.bill.service.ApiGatewayService;

@Service
public class GraviteeApiGatewayService implements ApiGatewayService {
	
	private final GraviteeApi graviteeApi;
	
	@Autowired
	public GraviteeApiGatewayService(final GraviteeApi graviteeApi) {
		this.graviteeApi = graviteeApi;
	}
	
	@Override
	public List<String> listUsers() {
		int page = 1;
		GraviteePage<GraviteeUser> result = graviteeApi.listUsers(page);
		final List<String> list = new ArrayList<>(result.getPage().getTotalElements());
		result.getData().stream().map(GraviteeUser::getId).forEach(list::add);
		
		while (result.hasMorePages()) {
			page++;
			result = graviteeApi.listUsers(page);
			result.getData().stream().map(GraviteeUser::getId).forEach(list::add);
		}
		
		return list;
	}
	
	private List<GraviteeApplicationSubscription> getSubscriptions(final String user) {
		return graviteeApi.listApplications().stream()
				.filter(app -> user.equals(app.getOwner().getId()))
				.filter(app -> "ACTIVE".equals(app.getStatus()))
				.map(GraviteeApplication::getId)
				.map(this::listApplicationSubscriptions)
				.flatMap(Set::stream)
				.filter(sub -> sub.getStatus().equals("accepted"))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<String> listApis(final String user) {
		return getSubscriptions(user).stream()
				.map(GraviteeApplicationSubscription::getApi)
				.collect(Collectors.toList());
	}
	
	private Set<GraviteeApplicationSubscription> listApplicationSubscriptions(String appId) {
		int page = 1;
		GraviteePage<GraviteeApplicationSubscription> result = graviteeApi.listApplicationSubscriptions(appId, page);
		final Set<GraviteeApplicationSubscription> list = new HashSet<>(result.getPage().getTotalElements());
		list.addAll(result.getData());
		
		while (result.hasMorePages()) {
			list.addAll(result.getData());
			page++;
			result = graviteeApi.listApplicationSubscriptions(appId, page);
		}
		
		return list;
	}
	
	@Override
	public ApiMetricsInfo getApiMetrics(final String user, final String apiUuid, final LocalDateTime startDt, final LocalDateTime finishDt) {
		final long start = startDt.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
		final long finish = finishDt.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
		final long hits = getSubscriptions(user).stream()
				.filter(sub -> sub.getApi().equals(apiUuid))
				.map(GraviteeApplicationSubscription::getApplication)
				.map(app -> graviteeApi.getMetrics(apiUuid, getAppQuery(app), start, finish))
				.mapToLong(GraviteeMetrics::sum2xxHits).sum();
		return new ApiMetricsInfo().setApiUuid(apiUuid).setHits(hits);
	}
	
	private String getAppQuery(final String app) {
		return String.format("(application:%s)", app);
	}
}
