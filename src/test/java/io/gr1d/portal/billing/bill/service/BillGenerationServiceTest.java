package io.gr1d.portal.billing.bill.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.gr1d.portal.billing.api.PlansServiceApi;
import io.gr1d.portal.billing.bill.model.ApiMetricsInfo;
import io.gr1d.portal.billing.bill.model.ApiPlan;
import io.gr1d.portal.billing.exception.BillGenerationException;
import io.gr1d.portal.billing.model.CardEnrollment;
import io.gr1d.portal.billing.model.Invoice;
import io.gr1d.portal.billing.model.InvoiceItem;
import io.gr1d.portal.billing.model.InvoiceRecipientSplit;
import io.gr1d.portal.billing.model.enumerations.PaymentGateway;
import io.gr1d.portal.billing.model.enumerations.PaymentStatus;
import io.gr1d.portal.billing.repository.CardEnrollmentRepository;
import io.gr1d.portal.billing.repository.InvoiceRepository;
import io.gr1d.portal.billing.service.BillGenerationService;

/**
 * @author sergio.marcelino
 */
public class BillGenerationServiceTest {
	
	@Mock
	private PlansServiceApi plansService;
	@Mock
	private ApiGatewayService apiGatewayService;
	@Mock
	private CardEnrollmentRepository cardEnrollmentRepository;
	@Mock
	private InvoiceRepository billRepository;
	@Mock
	private BillGenerationNotificator notificator;
	
	private BillGenerationService billGenerationService;
	private static final String OWNER_RECIPIENT_ID = "4d64-a494-45b5b7ee79df";
	
	@Before
	public void startUp() {
		MockitoAnnotations.initMocks(this);
		billGenerationService = new BillGenerationService(apiGatewayService,
				plansService, cardEnrollmentRepository, billRepository, notificator, OWNER_RECIPIENT_ID);
	}
	
	@Test
	public void shouldGenerateBill() {
		final String userUuid = "70eb571b-bb0d-47fa-b331-31ffba50a496";
		final String apiOneUid = "f8a1e41b";
		final String apiTwoUid = "903d4dcec3b9";
		
		final ApiMetricsInfo metrics = new ApiMetricsInfo();
		metrics.setApiUuid(apiOneUid);
		metrics.setHits(3837411L);
		
		final ApiMetricsInfo anotherMetrics = new ApiMetricsInfo();
		anotherMetrics.setApiUuid(apiTwoUid);
		anotherMetrics.setHits(114848L);
		
		final CardEnrollment cardEnrollment = new CardEnrollment();
		cardEnrollment.setCardId("card_cjhth5dj504wlil6ez9s660ie");
		cardEnrollment.setGateway(PaymentGateway.GR1D);
		cardEnrollment.setUserId(userUuid);
		
		final ApiPlan planOne = new ApiPlan();
		final String partnerOneWalletId = "12y98h12";
		planOne.setPercent(50); // 50 %
		planOne.setHitValue(1); // $ 0.000001
		planOne.setPartner(new ApiPlan.ApiPartner());
		planOne.getPartner().setWalletId(partnerOneWalletId);
		
		final ApiPlan planTwo = new ApiPlan();
		final String partnerTwoWalletId = "3443g54h5f";
		planTwo.setPercent(25); // 25 %
		planTwo.setHitValue(15); // $ 0.000015
		planTwo.setPartner(new ApiPlan.ApiPartner());
		planTwo.getPartner().setWalletId(partnerTwoWalletId);
		
		final List<String> apis = Arrays.asList(apiOneUid, apiTwoUid);
		
		when(apiGatewayService.listApis(anyString())).thenReturn(apis);
		when(apiGatewayService.getApiMetrics(eq(userUuid), eq(apiOneUid), any(), any())).thenReturn(metrics);
		when(apiGatewayService.getApiMetrics(eq(userUuid), eq(apiTwoUid), any(), any())).thenReturn(anotherMetrics);
		when(billRepository.countByUserIdAndReferenceMonth(anyString(), anyInt())).thenReturn(0);
		when(billRepository.save(any())).then(invocation -> invocation.getArgument(0));
		when(cardEnrollmentRepository.findFirstByUserIdOrderByVersionDesc(anyString())).thenReturn(cardEnrollment);
		when(plansService.getPlan(eq(apiOneUid))).thenReturn(planOne);
		when(plansService.getPlan(eq(apiTwoUid))).thenReturn(planTwo);
		
		final Invoice bill = billGenerationService.generateBill(userUuid, 2018, 6);
		
		assertThat(bill.getPaymentStatus()).isEqualTo(PaymentStatus.CREATED);
		assertThat(bill.getUserId()).isEqualTo(userUuid);
		assertThat(bill.getValue()).isEqualTo(5560131L);
//		assertThat(bill.getReferenceMonth()).isEqualTo(201806);
		
		final LocalDateTime startDt = LocalDateTime.of(2018, 6, 1, 0, 0);
		final LocalDateTime finishDt = LocalDateTime.of(2018, 7, 1, 0, 0);
		
		verify(billRepository, times(1)).save(eq(bill));
		verify(billRepository, times(1)).countByUserIdAndReferenceMonth(eq(userUuid), eq(201806));
		verify(apiGatewayService, times(1)).listApis(eq(userUuid));
		verify(apiGatewayService, times(1)).getApiMetrics(eq(userUuid), eq(apiOneUid), eq(startDt), eq(finishDt));
		verify(apiGatewayService, times(1)).getApiMetrics(eq(userUuid), eq(apiTwoUid), eq(startDt), eq(finishDt));
		verify(notificator, times(0)).notifyBillGenerationError(any());
		
		assertThat(bill.getItems()).hasSize(2);
		
		final Iterator<InvoiceItem> iteratorDetail = bill.getItems().iterator();
		final InvoiceItem detailApiOne = iteratorDetail.next();
		assertThat(detailApiOne.getInvoice()).isSameAs(bill);
		assertThat(detailApiOne.getExternalId()).isEqualTo(apiOneUid);
		assertThat(detailApiOne.getQuantity()).isEqualTo(metrics.getHits());
		assertThat(detailApiOne.getUnitValue()).isEqualTo(planOne.getHitValue());
		assertThat(detailApiOne.getValue()).isEqualTo(3837411L);
		
		final InvoiceItem detailApiTwo = iteratorDetail.next();
		assertThat(detailApiTwo.getInvoice()).isSameAs(bill);
		assertThat(detailApiTwo.getExternalId()).isEqualTo(apiTwoUid);
		assertThat(detailApiTwo.getQuantity()).isEqualTo(anotherMetrics.getHits());
		assertThat(detailApiTwo.getUnitValue()).isEqualTo(planTwo.getHitValue());
		assertThat(detailApiTwo.getValue()).isEqualTo(1722720L);
		
		assertThat(bill.getSplit()).hasSize(3);
		final List<InvoiceRecipientSplit> billSplits = Lists.newArrayList(bill.getSplit());
		billSplits.sort(Comparator.comparing(InvoiceRecipientSplit::getRecipientId));
		
		assertThat(billSplits.get(0).getInvoice()).isSameAs(bill);
		assertThat(billSplits.get(0).getRecipientId()).isEqualTo(partnerOneWalletId);
		assertThat(billSplits.get(0).getValue()).isEqualTo(191L);
		
		assertThat(billSplits.get(1).getInvoice()).isSameAs(bill);
		assertThat(billSplits.get(1).getRecipientId()).isEqualTo(partnerTwoWalletId);
		assertThat(billSplits.get(1).getValue()).isEqualTo(43L);
		
		assertThat(billSplits.get(2).getInvoice()).isSameAs(bill);
		assertThat(billSplits.get(2).getRecipientId()).isEqualTo(OWNER_RECIPIENT_ID);
		assertThat(billSplits.get(2).getValue()).isEqualTo(322L);
	}
	
	@Test
	public void shouldNotGenerateDuplicatedBills() {
		final String userUuid = "70eb571b-bb0d-47fa-b331-31ffba50a496";
		
		when(billRepository.countByUserIdAndReferenceMonth(anyString(), anyInt())).thenReturn(1);
		
		final Invoice bill = billGenerationService.generateBill(userUuid, 2018, 11);
		assertThat(bill).isNull();
		
		verify(billRepository, times(1)).countByUserIdAndReferenceMonth(eq(userUuid), eq(201811));
	}
	
	@Test
	public void shouldGenerateBillDetail() {
		final ApiMetricsInfo metrics = new ApiMetricsInfo();
		metrics.setApiUuid("f8a1e41b-d743-47af-a3f7-903d4dcec3b9");
		metrics.setHits(3837411L);
		
		final ApiPlan plan = new ApiPlan();
		plan.setHitValue(1); // $ 0.000001
		plan.setPercent(1000); // 10.00 %
		
		when(plansService.getPlan(anyString())).thenReturn(plan);
		
		final InvoiceItem billDetail = billGenerationService.generateBillDetail(metrics);
		
		assertThat(billDetail.getExternalId()).isEqualTo(metrics.getApiUuid());
		assertThat(billDetail.getQuantity()).isEqualTo(3837411L);
		assertThat(billDetail.getValue()).isEqualTo(3837411);
		assertThat(billDetail.getUnitValue()).isEqualTo(plan.getHitValue());
		
		verify(plansService, times(1)).getPlan(eq(metrics.getApiUuid()));
	}
	
	@Test
	public void getRealValue() {
		final long value = 12837156182L;
		final long realValue = billGenerationService.getRealValue(value);
		assertThat(realValue).isEqualTo(1283715);
	}
	
	@Test
	public void shouldNotifyWhenNoCardIsFound() {
		when(billRepository.countByUserIdAndReferenceMonth(anyString(), anyInt())).thenReturn(0);
		when(cardEnrollmentRepository.findFirstByUserIdOrderByVersionDesc(anyString())).thenReturn(null);
		assertThatThrownBy(() -> billGenerationService.generateBill("user", 2018, 7))
				.isInstanceOf(BillGenerationException.class);
		verify(cardEnrollmentRepository, times(1)).findFirstByUserIdOrderByVersionDesc(eq("user"));
		verify(notificator, times(1)).notifyBillGenerationError(any());
	}
}