//package io.gr1d.portal.billing.charging;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import br.com.six2six.fixturefactory.Fixture;
//import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
//import io.gr1d.portal.billing.model.Invoice;
//import io.gr1d.portal.billing.model.InvoiceGatewayOperation;
//import io.gr1d.portal.billing.model.enumerations.PaymentStatus;
//import io.gr1d.portal.billing.repository.BillChargeHistoryRepository;
//import io.gr1d.portal.billing.repository.InvoiceRepository;
//import io.gr1d.portal.billing.repository.CardEnrollmentRepository;
//import io.gr1d.portal.billing.test.SpringTestApplication;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringTestApplication.class)
//public class BillChargeHistoryServiceIntegrationTest {
//
//	@Autowired
//	private BillChargeHistoryService service;
//
//	@Autowired
//	private InvoiceRepository billRepository;
//
//	@Autowired
//	private BillChargeHistoryRepository repository;
//
//	@Autowired
//	private CardEnrollmentRepository cardEnrollmentRepository;
//
//	@Test
//	public void test() {
//		FixtureFactoryLoader.loadTemplates("io.gr1d.portal.billing.test.fixtures");
//		final Invoice bill = Fixture.from(Invoice.class).gimme("preinsert");
//		cardEnrollmentRepository.save(bill.getCardEnrollment());
//		billRepository.save(bill);
//
//		final String request = "this is some request example";
//		InvoiceGatewayOperation history = service.registerCharge(bill, PaymentStatus.REFUNDING, request);
//
//		assertThat(history.getBill()).isSameAs(bill);
//		assertThat(history.getBillPreviousStatus()).isEqualTo(PaymentStatus.CREATED);
//		assertThat(history.getChargeStatus()).isEqualTo(PaymentStatus.REFUNDING);
//		assertThat(history.getCardEnrollment()).isEqualTo(bill.getCardEnrollment());
//		assertThat(history.getGatewayRequest()).isEqualTo(request);
//		assertThat(history.getGatewayResponse()).isNull();
//		assertThat(history.getGatewayWebhookRequest()).isNull();
//		assertThat(history.getId()).isNotNull();
//		assertThat(history.getUuid()).isNotNull();
//
//		final String response = "this is some response example returned from payment gateway";
//		service.registerChargeResponse(history, response);
//
//		history = repository.findById(history.getId()).get();
//		assertThat(history.getGatewayResponse()).isEqualTo(response);
//		assertThat(history.getGatewayWebhookRequest()).isNull();
//		assertThat(history.getBillPreviousStatus()).isEqualTo(PaymentStatus.CREATED);
//
//		final String webhookRequest = "this is a webhook example request";
//		service.registerChargeWebhook(bill.getUuid(), webhookRequest);
//
//		history = repository.findById(history.getId()).get();
//		assertThat(history.getGatewayWebhookRequest()).isEqualTo(webhookRequest);
//		assertThat(history.getBillPreviousStatus()).isEqualTo(PaymentStatus.CREATED);
//
//		service.registerChargeFinish(bill.getUuid(), PaymentStatus.REFUNDED);
//		history = repository.findById(history.getId()).get();
//		assertThat(history.getBillPreviousStatus()).isEqualTo(PaymentStatus.CREATED);
//		assertThat(history.getChargeStatus()).isEqualTo(PaymentStatus.REFUNDED);
//	}
//
//}
