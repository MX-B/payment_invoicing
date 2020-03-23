package io.gr1d.portal.billing.test.controller;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.UnsupportedEncodingException;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import io.gr1d.portal.billing.controller.CardAuthorizationController;
import io.gr1d.portal.billing.model.CardEnrollment;
import io.gr1d.portal.billing.request.CardAuthorizationRequest;
import io.gr1d.portal.billing.response.CardInfoResponse;
import io.gr1d.portal.billing.service.CardAuthorizationService;
import io.gr1d.portal.billing.service.CardInfoService;
import io.gr1d.portal.billing.test.TestUtils;

public class CardAuthorizationControllerUnitTest {
	@Mock
	private CardAuthorizationService cardAuthorizationService;
	
	@Mock
	private CardInfoService cardInfoService;
	
	private CardAuthorizationController controller;
	
	@Before
	public void setUp() throws IllegalArgumentException, UnsupportedEncodingException {
		initMocks(this);
		controller = new CardAuthorizationController(cardAuthorizationService, cardInfoService);
	}
	
	@Test
	public void testAuthorize() {
		final CardAuthorizationRequest request = new CardAuthorizationRequest();
		final CardEnrollment cardEnrollment = new CardEnrollment();
		final CardInfoResponse cardInfoResponse = new CardInfoResponse();
		
		when(cardAuthorizationService.authorize(eq(request))).thenReturn(cardEnrollment);
		when(cardInfoService.getCardInfo(eq(cardEnrollment))).thenReturn(cardInfoResponse);
		
		final ResponseEntity<CardInfoResponse> response = controller.authorize(request, null);
		
		verify(cardAuthorizationService, times(1)).authorize(eq(request));
		verify(cardInfoService, times(1)).getCardInfo(eq(cardEnrollment));
		
		assertThat(response, is(notNullValue()));
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody(), is(cardInfoResponse));
	}
	
	@Test
	public void testAuthorizeWithErrors() {
		final CardAuthorizationRequest request = new CardAuthorizationRequest();
		
		when(cardAuthorizationService.authorize(eq(request))).thenReturn(null);
		
		final ResponseEntity<?> response = controller.authorize(request, null);
		
		verify(cardAuthorizationService, times(1)).authorize(eq(request));
		
		assertThat(response, is(notNullValue()));
		assertThat(response.getStatusCode(), is(HttpStatus.BAD_GATEWAY));
	}
	
	@Test
	public void testAuthorizeWithException() {
		final CardAuthorizationRequest request = new CardAuthorizationRequest();
		
		when(cardAuthorizationService.authorize(eq(request))).thenThrow(ConstraintViolationException.class);
		
		assertThatThrownBy(() -> controller.authorize(request, null))
			.isInstanceOf(ConstraintViolationException.class);
		
		verify(cardAuthorizationService, times(1)).authorize(eq(request));
	}
	
	@Test
	public void testAnnotations() {
		TestUtils.testAnnotations(CardAuthorizationController.class, RestController.class);
	}
	
	@Test
	public void testLogger()
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		TestUtils.testLogger(CardAuthorizationController.class);
	}
}
