//package io.gr1d.portal.billing.test.charging;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.mockito.MockitoAnnotations.initMocks;
//
//import java.util.Map;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.springframework.context.MessageSource;
//
//import br.com.six2six.fixturefactory.Fixture;
//import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
//import io.gr1d.portal.billing.charging.ChargingExecutor;
//import io.gr1d.portal.billing.exception.BillAlreadyChargedException;
//import io.gr1d.portal.billing.exception.CardChargeException;
//import io.gr1d.portal.billing.model.CardEnrollment;
//import io.gr1d.portal.billing.model.Invoice;
//import io.gr1d.portal.billing.model.InvoiceGatewayOperation;
//import io.gr1d.portal.billing.model.enumerations.PaymentStatus;
//import io.gr1d.portal.billing.service.BillGenerationService;
//import io.gr1d.portal.billing.strategy.charge.ChargeFactory;
//import io.gr1d.portal.billing.strategy.charge.ChargeLogger;
//import io.gr1d.portal.billing.strategy.charge.ChargeStrategy;
//import io.gr1d.portal.billing.test.TestUtils;
//
//public class ChargingExecutorTest {
//	@Mock
//	private ChargeFactory chargeFactory;
//	@Mock
//	private MessageSource messageSource;
//	@Mock
//	private ChargeStrategy strategy;
//	@Mock
//	private BillGenerationService billGenerationService;
//
//	private ChargingExecutor service;
//
//	@Before
//	public void setUp() throws IllegalArgumentException {
//		initMocks(this);
//		FixtureFactoryLoader.loadTemplates("io.gr1d.portal.billing.test.fixtures");
//		service = new ChargingExecutor(chargeFactory, messageSource, billGenerationService);
//	}
//
//	@Test
//	@SuppressWarnings("unchecked")
//	public void testChargeDefaultGateway() throws CardChargeException {
//		final String testGatewayTransactionId = "test-gateway-tx-id";
//		final Invoice bill = Fixture.from(Invoice.class).gimme("valid");
//		final String request = "example request";
//		final String response = "example response";
//		final InvoiceGatewayOperation history = new InvoiceGatewayOperation();
//
//		when(chargeFactory.getImplementation(anyString())).thenReturn(strategy);
//		when(strategy.charge(anyLong(), any(CardEnrollment.class), anyCollection(), anyCollection(), any(), any())).then(invocation -> {
//			final ChargeLogger logger = invocation.getArgument(5);
//			logger.logRequest(request);
//			logger.logResponse(response);
//			return null;
//		}).thenReturn(testGatewayTransactionId);
//		when(billGenerationService.getRealValue(anyLong())).thenReturn(10000L);
//		when(billChargeHistoryService.registerCharge(any(Invoice.class), any(PaymentStatus.class), anyString())).thenReturn(history);
//
//		service.charge(bill);
//
//		final ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
//
//		verify(chargeFactory, times(1)).getImplementation(anyString());
//		verify(strategy, times(1)).charge(anyLong(), any(CardEnrollment.class),
//				anyCollection(), anyCollection(), metadataCaptor.capture(), any());
//		verify(billGenerationService, times(1)).getRealValue(anyLong());
//		verify(billChargeHistoryService, times(1)).registerCharge(same(bill), eq(PaymentStatus.PROCESSING), same(request));
//		verify(billChargeHistoryService, times(1)).registerChargeResponse(same(history), same(response));
//
//		final Map<String, Object> value = metadataCaptor.getValue();
//		assertThat(value.get("bill_uuid")).isEqualTo(bill.getUuid());
//		assertThat(bill.getStatus()).isEqualTo(PaymentStatus.PROCESSING);
//	}
//
//	@Test
//	@SuppressWarnings("unchecked")
//	public void testChargeFailedPayment() throws CardChargeException {
//		final String testGatewayTransactionId = "test-gateway-tx-id";
//		final Invoice bill = Fixture.from(Invoice.class).gimme("valid");
//		bill.setStatus(PaymentStatus.FAILED);
//
//		when(chargeFactory.getImplementation(anyString())).thenReturn(strategy);
//		when(strategy.charge(anyLong(), any(CardEnrollment.class), anyCollection(), anyCollection(), any(), any())).thenReturn(testGatewayTransactionId);
//		when(billGenerationService.getRealValue(anyLong())).thenReturn(10000L);
//
//		service.charge(bill);
//
//		final ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
//
//		verify(chargeFactory, times(1)).getImplementation(anyString());
//		verify(strategy, times(1)).charge(anyLong(), any(CardEnrollment.class), anyCollection(), anyCollection(), metadataCaptor.capture(), any());
//		verify(billGenerationService, times(1)).getRealValue(anyLong());
//
//		final Map<String, Object> value = metadataCaptor.getValue();
//		assertThat(value.get("bill_uuid")).isEqualTo(bill.getUuid());
//		assertThat(bill.getStatus()).isEqualTo(PaymentStatus.PROCESSING_RETRY);
//	}
//
//	@Test
//	@SuppressWarnings("unchecked")
//	public void testChargeOnlyOnFailedOrCreated() throws CardChargeException {
//		final Invoice bill = Fixture.from(Invoice.class).gimme("valid");
//		when(chargeFactory.getImplementation(anyString())).thenReturn(strategy);
//		when(strategy.charge(anyLong(), any(CardEnrollment.class), anyCollection(), anyCollection(), any(), any())).thenReturn("~");
//		when(billGenerationService.getRealValue(anyLong())).thenReturn(10000L);
//
//		bill.setStatus(PaymentStatus.PROCESSING);
//		assertThatThrownBy(() -> service.charge(bill)).isInstanceOf(BillAlreadyChargedException.class);
//
//		bill.setStatus(PaymentStatus.PROCESSING_RETRY);
//		assertThatThrownBy(() -> service.charge(bill)).isInstanceOf(BillAlreadyChargedException.class);
//
//		bill.setStatus(PaymentStatus.SUCCESS);
//		assertThatThrownBy(() -> service.charge(bill)).isInstanceOf(BillAlreadyChargedException.class);
//
//		bill.setStatus(PaymentStatus.REFUNDED);
//		assertThatThrownBy(() -> service.charge(bill)).isInstanceOf(BillAlreadyChargedException.class);
//
//		bill.setStatus(PaymentStatus.REFUNDING);
//		assertThatThrownBy(() -> service.charge(bill)).isInstanceOf(BillAlreadyChargedException.class);
//	}
//
//	@Test
//	public void testLogger() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
//		TestUtils.testLogger(ChargingExecutor.class);
//	}
//}
