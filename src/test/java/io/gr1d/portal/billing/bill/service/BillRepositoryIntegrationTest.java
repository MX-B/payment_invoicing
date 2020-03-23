package io.gr1d.portal.billing.bill.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.UUID;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import io.gr1d.portal.billing.dto.BillDetailDTO;
import io.gr1d.portal.billing.dto.BillListDTO;
import io.gr1d.portal.billing.model.CardEnrollment;
import io.gr1d.portal.billing.model.Invoice;
import io.gr1d.portal.billing.model.InvoiceItem;
import io.gr1d.portal.billing.model.PersonalInfo;
import io.gr1d.portal.billing.model.enumerations.PaymentGateway;
import io.gr1d.portal.billing.model.enumerations.PaymentStatus;
import io.gr1d.portal.billing.repository.CardEnrollmentRepository;
import io.gr1d.portal.billing.repository.InvoiceRepository;
import io.gr1d.portal.billing.test.SpringTestApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringTestApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BillRepositoryIntegrationTest {
	
	@Autowired
	private InvoiceRepository billRepository;
	@Autowired
	private CardEnrollmentRepository cardEnrollmentRepository;
	@Autowired
	private MockMvc mockMvc;
	
	private Invoice createBill() {
		FixtureFactoryLoader.loadTemplates("io.gr1d.portal.billing.test.fixtures");
		
		final InvoiceItem detail = new InvoiceItem();
		detail.setQuantity(1231322);
		detail.setExternalId("80fc5397-e787-427b-966f-3af9049f9335");
		detail.setValue(14845436586L);
		detail.setUnitValue(12587633L);
		
		final PersonalInfo personalInfo = Fixture.from(PersonalInfo.class).gimme("valid");
		personalInfo.setId(null);
		personalInfo.getAddress().setId(null);
		
		final CardEnrollment cardEnrollment = new CardEnrollment();
		cardEnrollment.setPersonalInfo(personalInfo);
		cardEnrollment.setVersion(1);
		cardEnrollment.setUuid("CE-" + UUID.randomUUID().toString());
		cardEnrollment.setActive(true);
		cardEnrollment.setCardId("card_cjhth5dj504wlil6ez9s660ie");
		cardEnrollment.setUserId("af2cca2a-5b9a-4b4d-a8c8-fed0f8e72734");
		cardEnrollment.setGateway(PaymentGateway.GR1D);
		
		final Invoice bill = new Invoice();
		bill.setUserId(cardEnrollmentRepository.save(cardEnrollment).getUserId());
		bill.setValue(123871616567L);
		bill.setStatus(PaymentStatus.PROCESSING, "processing");
		bill.setPaymentGateway(PaymentGateway.GR1D);
//		bill.setReferenceMonth(201806);
		bill.setItems(Collections.singletonList(detail));
		detail.setInvoice(bill);
		
		return bill;
	}
	
	@Test
	@Transactional
	public void testBillInsert() {
		final Invoice bill = createBill();
		final Invoice createdBill = billRepository.save(bill);
		
		assertThat(createdBill.getId()).isNotNull();
		assertThat(createdBill.getUuid()).isNotNull();
		assertThat(createdBill.getCreatedAt()).isNotNull();
		
		final Invoice byUuidBill = billRepository.findByUuid(bill.getUuid());
		
		assertThat(byUuidBill.getId()).isEqualTo(createdBill.getId());
		assertThat(byUuidBill.getUuid()).isEqualTo(createdBill.getUuid());
		
		final Page<BillListDTO> page = billRepository
			.findByUserIdOrderByReferenceMonthDesc(bill.getUserId(), of(0, 10));
		
		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getContent()).hasSize(1);
		
		final BillListDTO billListDTO = page.getContent().get(0);
		assertThat(billListDTO.getUuid()).isNotEmpty();
		assertThat(billListDTO.getReferenceMonth()).isEqualTo("201806");
		assertThat(billListDTO.getValue()).isEqualTo(12387161L);
		assertThat(billListDTO.getStatus()).isEqualTo("PROCESSING");
		assertThat(billListDTO.getDetails()).hasSize(1);
		
		final BillDetailDTO detailDTO = billListDTO.getDetails().get(0);
		assertThat(detailDTO.getItemId()).isEqualTo("80fc5397-e787-427b-966f-3af9049f9335");
		assertThat(detailDTO.getQuantity()).isEqualTo(1231322L);
		assertThat(detailDTO.getValue()).isEqualTo(1484543L);
		assertThat(detailDTO.getUnitValue()).isEqualTo(12587633L);
	}
	
	@Test
	@Transactional
	public void testBillEndpoints() throws Exception {
		final Invoice bill = createBill();
		billRepository.save(bill);
		
		final String uri = String.format("/bill/user/%s", bill.getUserId());
		mockMvc.perform(get(uri)).andDo(print()).andExpect(status().isOk())
			.andExpect(jsonPath("content").isArray())
			.andExpect(jsonPath("content").value(hasSize(1)))
			.andExpect(jsonPath("content[0].reference_month").value("201806"))
			.andExpect(jsonPath("content[0].value").value(12387161))
			.andExpect(jsonPath("content[0].status").value("PROCESSING"))
			.andExpect(jsonPath("content[0].uuid").exists()).andExpect(jsonPath("content[0].details").isArray())
			.andExpect(jsonPath("content[0].details").value(hasSize(1)))
			.andExpect(jsonPath("content[0].details[0].value").value(1484543))
			.andExpect(jsonPath("content[0].details[0].unit_value").value(12587633))
			.andExpect(jsonPath("content[0].details[0].quantity").value(1231322))
			.andExpect(jsonPath("content[0].details[0].item_id").value("80fc5397-e787-427b-966f-3af9049f9335"));
		
		mockMvc.perform(get(uri + "?limit=100&offset=2")).andDo(print()).andExpect(status().isOk())
			.andExpect(jsonPath("content").isArray())
			.andExpect(jsonPath("content").value(hasSize(0))).andExpect(jsonPath("limit").value(100))
			.andExpect(jsonPath("offset").value(2)).andExpect(jsonPath("total_elements").value(1));
		
		final String uriLast = String.format("/bill/user/%s/last", bill.getUserId());
		mockMvc.perform(get(uriLast)).andDo(print()).andExpect(status().isOk())
			.andExpect(jsonPath("reference_month").value("201806"))
			.andExpect(jsonPath("value").value(12387161)).andExpect(jsonPath("status").value("PROCESSING"))
			.andExpect(jsonPath("uuid").exists()).andExpect(jsonPath("details").isArray())
			.andExpect(jsonPath("details").value(hasSize(1))).andExpect(jsonPath("details[0].value").value(1484543))
			.andExpect(jsonPath("details[0].unit_value").value(12587633))
			.andExpect(jsonPath("details[0].quantity").value(1231322))
			.andExpect(jsonPath("details[0].item_id").value("80fc5397-e787-427b-966f-3af9049f9335"))
			.andExpect(jsonPath("status_history").isArray()).andExpect(jsonPath("status_history").value(hasSize(1)))
			.andExpect(jsonPath("status_history[0].status").value("PROCESSING"))
			.andExpect(jsonPath("status_history[0].timestamp").value(matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}([.]\\d+)?")));
		
		final String uriUuid = String.format("/bill/uuid/%s", bill.getUuid());
		mockMvc.perform(get(uriUuid)).andDo(print()).andExpect(status().isOk())
			.andExpect(jsonPath("reference_month").value("201806"))
			.andExpect(jsonPath("value").value(12387161)).andExpect(jsonPath("status").value("PROCESSING"))
			.andExpect(jsonPath("uuid").exists()).andExpect(jsonPath("details").isArray())
			.andExpect(jsonPath("details").value(hasSize(1))).andExpect(jsonPath("details[0].value").value(1484543))
			.andExpect(jsonPath("details[0].unit_value").value(12587633))
			.andExpect(jsonPath("details[0].quantity").value(1231322))
			.andExpect(jsonPath("details[0].item_id").value("80fc5397-e787-427b-966f-3af9049f9335"))
			.andExpect(jsonPath("status_history").isArray()).andExpect(jsonPath("status_history").value(hasSize(1)))
			.andExpect(jsonPath("status_history[0].status").value("PROCESSING"))
			.andExpect(jsonPath("status_history[0].timestamp").value(matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}([.]\\d+)?")));
	}
	
	@Test
	public void testBillWithNotFoundUser() throws Exception {
		mockMvc.perform(get("/bill/user/usernotfound/last")).andDo(print())
				.andExpect(status().isNotFound());
		
		mockMvc.perform(get("/bill/user/usernotfound")).andDo(print())
				.andExpect(status().isNotFound());
	}
	
	public static class RegexMatcher extends BaseMatcher<String> {
		private final String regex;
		
		RegexMatcher(final String regex) {
			this.regex = regex;
		}
		
		@Override
		public boolean matches(final Object o) {
			return ((String) o).matches(regex);
			
		}
		
		@Override
		public void describeTo(final Description description) {
			description.appendText("matches regex=").appendText(regex);
		}
	}
	
	private static RegexMatcher matches(final String regex) {
		return new RegexMatcher(regex);
	}
}
