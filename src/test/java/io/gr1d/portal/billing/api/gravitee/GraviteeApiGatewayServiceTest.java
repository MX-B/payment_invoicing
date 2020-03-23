package io.gr1d.portal.billing.api.gravitee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import io.gr1d.portal.billing.bill.model.ApiMetricsInfo;

public class GraviteeApiGatewayServiceTest {
	
	private static final String USER_ONE = "4ab3aab9-dffc-45dc-a3aa-f5fc29024600";
	private static final String USER_TWO = "36c44aff-cd1f-4a48-9958-17a46ef79592";
	
	@Mock
	private GraviteeApi graviteeApi;
	
	private GraviteeApiGatewayService service;
	
	@Before
	public void startUp() {
		FixtureFactoryLoader.loadTemplates("io.gr1d.portal.billing.test.fixtures");
		MockitoAnnotations.initMocks(this);
		service = new GraviteeApiGatewayService(graviteeApi);
	}
	
	@Test
	public void listUsers() {
		final GraviteeUser userOne = new GraviteeUser();
		userOne.setId(USER_ONE);
		final GraviteePage<GraviteeUser> usersPage1 = new GraviteePage<>();
		usersPage1.setData(Collections.singletonList(userOne));
		usersPage1.setPage(createPage(1));
		
		final GraviteeUser userTwo = new GraviteeUser();
		userTwo.setId(USER_TWO);
		final GraviteePage<GraviteeUser> usersPage2 = new GraviteePage<>();
		usersPage2.setData(Collections.singletonList(userTwo));
		usersPage2.setPage(createPage(2));
		
		when(graviteeApi.listUsers(eq(1))).thenReturn(usersPage1);
		when(graviteeApi.listUsers(eq(2))).thenReturn(usersPage2);
		
		final List<String> strings = service.listUsers();
		assertThat(strings).containsExactlyInAnyOrder(USER_ONE, USER_TWO);
		verify(graviteeApi, times(1)).listUsers(eq(1));
		verify(graviteeApi, times(1)).listUsers(eq(2));
	}
	
	private GraviteePageOptions createPage(final int current) {
		final GraviteePageOptions options = new GraviteePageOptions();
		options.setCurrent(current);
		options.setPerPage(1);
		options.setSize(1);
		options.setTotalElements(2);
		options.setTotalPages(2);
		return options;
	}
	
	private GraviteePageOptions createEmptyPage() {
		final GraviteePageOptions options = new GraviteePageOptions();
		options.setCurrent(1);
		options.setPerPage(1);
		options.setSize(0);
		options.setTotalElements(1);
		options.setTotalPages(1);
		return options;
	}
	
	@Test
	public void listApis() {
		final GraviteeUser userOne = new GraviteeUser();
		userOne.setId(USER_ONE);
		final List<GraviteeApplication> applications = Fixture.from(GraviteeApplication.class)
				.gimme(5, "active", "inactive", "inactive", "active", "active");
		final List<GraviteeApplicationSubscription> subscriptions = Fixture.from(GraviteeApplicationSubscription.class)
				.gimme(2,"accepted", "pending");
		final GraviteeApplication activeApplication = applications.get(0);
		final GraviteeApplication inactiveApplication = applications.get(1);
		final GraviteeApplication unsubscribesApplication = applications.get(4);
		
		subscriptions.forEach(sub -> sub.setApplication(activeApplication.getId()));
		
		final GraviteePage<GraviteeApplicationSubscription> subsPage1 = new GraviteePage<>();
		subsPage1.setData(Collections.singletonList(subscriptions.get(0)));
		subsPage1.setPage(createPage(1));
		
		final GraviteePage<GraviteeApplicationSubscription> subsPage2 = new GraviteePage<>();
		subsPage2.setData(Collections.singletonList(subscriptions.get(1)));
		subsPage2.setPage(createPage(2));
		
		final GraviteePage<GraviteeApplicationSubscription> emptyPage = new GraviteePage<>();
		emptyPage.setData(Collections.emptyList());
		emptyPage.setPage(createEmptyPage());
		
		activeApplication.setOwner(userOne);
		inactiveApplication.setOwner(userOne);
		unsubscribesApplication.setOwner(userOne);
		
		when(graviteeApi.listApplications()).thenReturn(applications);
		when(graviteeApi.listApplicationSubscriptions(eq(activeApplication.getId()), eq(1))).thenReturn(subsPage1);
		when(graviteeApi.listApplicationSubscriptions(eq(activeApplication.getId()), eq(2))).thenReturn(subsPage2);
		when(graviteeApi.listApplicationSubscriptions(eq(unsubscribesApplication.getId()), eq(1))).thenReturn(emptyPage);
		
		final List<String> apis = service.listApis(USER_ONE);
		assertThat(apis).contains(subscriptions.get(0).getApi());
		verify(graviteeApi, times(1)).listApplications();
		verify(graviteeApi, times(1)).listApplicationSubscriptions(eq(activeApplication.getId()), eq(1));
		verify(graviteeApi, times(1)).listApplicationSubscriptions(eq(activeApplication.getId()), eq(2));
		verify(graviteeApi, times(1)).listApplicationSubscriptions(eq(unsubscribesApplication.getId()), eq(1));
	}
	
	@Test
	public void getApiMetrics() {
		final GraviteeUser userOne = new GraviteeUser();
		userOne.setId(USER_ONE);
		final List<GraviteeApplicationSubscription> subscriptions = Fixture.from(GraviteeApplicationSubscription.class)
				.gimme(2,"accepted", "pending");
		final List<GraviteeApplication> applications = Fixture.from(GraviteeApplication.class)
				.gimme(5, "active", "inactive", "inactive", "active", "active");
		final GraviteeApplication activeApplication = applications.get(0);
		final GraviteeApplication inactiveApplication = applications.get(1);
		final GraviteePage<GraviteeApplicationSubscription> subsPage1 = new GraviteePage<>();
		
		final GraviteeApplicationSubscription subscription = subscriptions.get(0);
		subsPage1.setData(Collections.singletonList(subscription));
		subsPage1.setPage(createPage(1));
		
		final GraviteePage<GraviteeApplicationSubscription> subsPage2 = new GraviteePage<>();
		subsPage2.setData(Collections.singletonList(subscriptions.get(1)));
		subsPage2.setPage(createPage(2));
		
		activeApplication.setOwner(userOne);
		inactiveApplication.setOwner(userOne);
		
		final LocalDateTime startDt = LocalDate.of(2018, 7, 1).atStartOfDay();
		final LocalDateTime finishDt = startDt.plusMonths(1).minusNanos(1);
		final GraviteeMetrics metrics = new GraviteeMetrics();
		metrics.setValues(new HashMap<>());
		metrics.getValues().put("201", 102L);
		metrics.getValues().put("200", 1453L);
		metrics.getValues().put("400", 65564L);
		metrics.getValues().put("502", 1894875L);
		
		when(graviteeApi.listApplications()).thenReturn(applications);
		when(graviteeApi.listApplicationSubscriptions(eq(activeApplication.getId()), eq(1))).thenReturn(subsPage1);
		when(graviteeApi.listApplicationSubscriptions(eq(activeApplication.getId()), eq(2))).thenReturn(subsPage2);
		when(graviteeApi.getMetrics(anyString(), anyString(), anyLong(), anyLong())).thenReturn(metrics);
		
		final ApiMetricsInfo apiMetrics = service.getApiMetrics(USER_ONE, subscription.getApi(), startDt, finishDt);
		final String applicationParameter = String.format("(application:%s)", subscription.getApplication());
		verify(graviteeApi, times(1))
				.getMetrics(eq(subscription.getApi()), eq(applicationParameter),
						eq(1530403200000L), eq(1533081599999L));
		
		assertThat(apiMetrics.getApiUuid()).isEqualTo(subscription.getApi());
		assertThat(apiMetrics.getHits()).isEqualTo(1555L);
	}
}