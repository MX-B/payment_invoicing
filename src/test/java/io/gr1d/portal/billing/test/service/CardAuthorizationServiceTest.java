package io.gr1d.portal.billing.test.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import io.gr1d.portal.billing.exception.UnknownGatewayException;
import io.gr1d.portal.billing.model.CardEnrollment;
import io.gr1d.portal.billing.model.enumerations.PaymentGateway;
import io.gr1d.portal.billing.repository.CardEnrollmentRepository;
import io.gr1d.portal.billing.repository.PersonalAddressRepository;
import io.gr1d.portal.billing.repository.PersonalInfoRepository;
import io.gr1d.portal.billing.request.CardAuthorizationRequest;
import io.gr1d.portal.billing.service.CardAuthorizationService;
import io.gr1d.portal.billing.strategy.card.authorization.CardAuthorizationFactory;
import io.gr1d.portal.billing.strategy.card.authorization.CardAuthorizationStrategy;

/**
 * <p>Test cases:
 * 
 * <ul>
 * <li>Authorize Card with Specific Gateway [{@link #testAuthorizeString() Test}]
 * <li>Authorize Card with Unsupported Gateway (throw exception) [{@link #testAuthorizeUnsupportedGateway() Test 1}, {@link #testAuthorizeStringIsNull() Test 2}, {@link #testAuthorizeEnumIsNull() Test 3}]
 * <li>Authorize Card without Gateway (use default) [{@link #testAuthorizeDefaultGateway() Test}]
 * <li>Authorize Card with Invalid Data (for some reason, the gateway says the card is invalid - throw exception) [{@link #testAuthorizeDefaultGateway() Test}]
 * </ul>
 * 
 * @author Rafael M. Lins
 *
 */
public class CardAuthorizationServiceTest {
	@Mock
	private CardAuthorizationFactory strategyFactory;
	
	@Mock
	private CardEnrollmentRepository cardEnrollmentRepository;
	
	@Mock
	private EntityManager entityManager;
	
	@Mock
	private PersonalAddressRepository personalAddressRepository;
	
	@Mock
	private PersonalInfoRepository personalInfoRepository;
	
	@Mock
	private CardAuthorizationStrategy strategy;
	
	private CardAuthorizationService service;
	
	@Before
	public void setUp() throws IllegalArgumentException, UnsupportedEncodingException {
		FixtureFactoryLoader.loadTemplates("io.gr1d.portal.billing.test.fixtures");
		initMocks(this);
		service = new CardAuthorizationService(strategyFactory, cardEnrollmentRepository, entityManager, personalAddressRepository, personalInfoRepository, "gr1d");
	}
	
	@Test
	public void testAuthorizeDefaultGateway() {
		final CardAuthorizationRequest request = new CardAuthorizationRequest();
		final CardEnrollment result = new CardEnrollment();
		
		when(strategyFactory.getImplementation(eq(PaymentGateway.GR1D))).thenReturn(strategy);
		when(strategy.authorizeCard(eq(request))).thenReturn(result);
		
		service.authorize(request);
		
		verify(strategyFactory, times(1)).getImplementation(eq(PaymentGateway.GR1D));
		verify(strategy, times(1)).authorizeCard(eq(request));
		verify(cardEnrollmentRepository, times(1)).save(eq(result));
	}
	
	@Test
	public void testAuthorizeString() {
		final CardAuthorizationRequest request = new CardAuthorizationRequest();
		final CardEnrollment result = new CardEnrollment();
		final String gatewayDiscriminator = "gr1d";
		
		when(strategyFactory.getImplementation(eq(PaymentGateway.GR1D))).thenReturn(strategy);
		when(strategy.authorizeCard(eq(request))).thenReturn(result);
		
		service.authorize(request, gatewayDiscriminator);
		
		verify(strategyFactory, times(1)).getImplementation(eq(PaymentGateway.GR1D));
		verify(strategy, times(1)).authorizeCard(eq(request));
		verify(cardEnrollmentRepository, times(1)).save(eq(result));
	}
	
	@Test
	public void testAuthorizeEnum() {
		final CardAuthorizationRequest request = new CardAuthorizationRequest();
		final CardEnrollment result = new CardEnrollment();
		final PaymentGateway gateway = PaymentGateway.PAGARME;
		
		when(strategyFactory.getImplementation(eq(PaymentGateway.PAGARME))).thenReturn(strategy);
		when(strategy.authorizeCard(eq(request))).thenReturn(result);
		
		service.authorize(request, gateway);
		
		verify(strategyFactory, times(1)).getImplementation(eq(PaymentGateway.PAGARME));
		verify(strategy, times(1)).authorizeCard(eq(request));
		verify(cardEnrollmentRepository, times(1)).save(eq(result));
	}
	
	@Test
	public void testAuthorizeUnsupportedGateway() {
		final CardAuthorizationRequest request = new CardAuthorizationRequest();
		final String randomGatewayName = "randomGateway_" + UUID.randomUUID().toString();
		
		assertThatThrownBy(() -> service.authorize(request, randomGatewayName))
			.isInstanceOf(UnknownGatewayException.class);
	}
	
	@Test
	public void testAuthorizeEnumIsNull() {
		final CardAuthorizationRequest request = new CardAuthorizationRequest();
		
		assertThatThrownBy(() -> service.authorize(request, (PaymentGateway) null))
			.isInstanceOf(UnknownGatewayException.class);
	}
	
	@Test
	public void testAuthorizeStringIsNull() {
		final CardAuthorizationRequest request = new CardAuthorizationRequest();
		
		assertThatThrownBy(() -> service.authorize(request, (String) null))
			.isInstanceOf(UnknownGatewayException.class);
	}
}
