package io.gr1d.portal.billing.test.controller;

import static io.gr1d.portal.billing.test.TestUtils.checkXss;
import static io.gr1d.portal.billing.test.fixtures.FixtureUtils.json;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import io.gr1d.portal.billing.request.CardAuthorizationRequest;
import io.gr1d.portal.billing.test.SpringTestApplication;

/**
 * <p>Test Cases:
 * 
 * <ul>
 * <li>Authorize Card when everything is OK [{@link #testCardAuthorizeDefaultOk() Test}]
 * <li>Authorize Card with invalid Gateway [{@link #testCardAuthorizeUnknownGateway() Test}]
 * <li>Authorize Card with incomplete/invalid information - HTTP 422 Unprocessable Entity
 * <li>Authorize Card against specific Gateway
 * <li>Authorize Card when:
 *     <ol>
 *     <li>New Card + New PersonalInfo [{@link #testAuthorizeDefaultGateway() Test}]
 *     <li>New Card + Old PersonalInfo [{@link #testAuthorizeDefaultGateway() Test}]
 *     <li>Unmodified Card + Unmodified PersonalInfo [{@link #testAuthorizeDefaultGateway() Test}]
 *     <li>Unmodified Card + Modified PersonalInfo [{@link #testAuthorizeDefaultGateway() Test}]
 *     <li>Modified Card + Unmodified PersonalInfo [{@link #testAuthorizeDefaultGateway() Test}]
 *     <li>Modified Card + Modified PersonalInfo [{@link #testAuthorizeDefaultGateway() Test}]
 *     </ol>
 * </li>
 * </ul>
 * 
 * @author Rafael M. Lins
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringTestApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CardAuthorizationControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Before
	public void setUp() throws IllegalArgumentException, UnsupportedEncodingException {
		FixtureFactoryLoader.loadTemplates("io.gr1d.portal.billing.test.fixtures");
	}
	
	@Test
	public void testCardAuthorizeDefaultOk() throws Exception {
		final CardAuthorizationRequest request = Fixture.from(CardAuthorizationRequest.class).gimme("fixedCard_fixedPersonalInfo");
		final String payload = json(request, objectMapper);
		
		final ResultActions result = mockMvc
				.perform(post("/authorize-card").contentType(APPLICATION_JSON).accept(APPLICATION_JSON).content(payload))
				.andDo(print())
				.andExpect(status().isOk());
		
		checkXss(result);
		
//		checkXss(mockMvc.perform(get("/card/123")).andDo(print()).andExpect(status().isOk())
//			.andExpect(jsonPath("card_first_digits").value("513020"))
//			.andExpect(jsonPath("card_last_digits").value("5212"))
//			.andExpect(jsonPath("card_holder_name").value("TEST TESTINGTON"))
//			.andExpect(jsonPath("card_expiration_date").doesNotExist()).andExpect(jsonPath("card_cvv").doesNotExist())
//			.andExpect(jsonPath("personal_info.full_name").value("Test test"))
//			.andExpect(jsonPath("personal_info.document_type").value(1))
//			.andExpect(jsonPath("personal_info.document").value("12312312312"))
//			.andExpect(jsonPath("personal_info.phone").value("+5511999998888"))
//			.andExpect(jsonPath("personal_info.email").value("test@test.com"))
//			.andExpect(jsonPath("personal_info.date_of_birth").value("1987-04-04")));
	}
	
//	@Test
//	public void testRestAuthorizeGatewayOk() throws Exception {
//		final String payload = "{\"user_id\":\"1234\",\"card_number\":\"5130202065415212\",\"card_expiration_date\":\"0220\",\"card_cvv\":\"261\",\"card_holder_name\":\"TEST TESTINGTON\",\"full_name\":\"Test test\",\"email\":\"test@test.com\",\"document_type\":1,\"document\":\"12312312312\",\"phone\":\"+5511999998888\",\"date_of_birth\":\"1987-04-04\"}";
//
//		checkXss(mockMvc.perform(post("/authorize-card/pagarme").contentType(MediaType.APPLICATION_JSON)
//			.accept(MediaType.APPLICATION_JSON).content(payload)).andDo(print()).andExpect(status().isOk()));
//
//		checkXss(mockMvc.perform(get("/card/1234")).andDo(print()).andExpect(status().isOk())
//			.andExpect(jsonPath("card_first_digits").value("513020"))
//			.andExpect(jsonPath("card_last_digits").value("5212"))
//			.andExpect(jsonPath("card_holder_name").value("TEST TESTINGTON"))
//			.andExpect(jsonPath("card_expiration_date").doesNotExist()).andExpect(jsonPath("card_cvv").doesNotExist())
//			.andExpect(jsonPath("personal_info.full_name").value("Test test"))
//			.andExpect(jsonPath("personal_info.document_type").value(1))
//			.andExpect(jsonPath("personal_info.document").value("12312312312"))
//			.andExpect(jsonPath("personal_info.phone").value("+5511999998888"))
//			.andExpect(jsonPath("personal_info.email").value("test@test.com"))
//			.andExpect(jsonPath("personal_info.date_of_birth").value("1987-04-04")));
//	}
//
//	@Test
//	public void testCardAuthorizeUnknownGateway() throws Exception {
//		final String payload = "{\"user_id\":\"12345\",\"card_number\":\"5130202065415212\",\"card_expiration_date\":\"0220\",\"card_cvv\":\"261\",\"card_holder_name\":\"TEST TESTINGTON\",\"full_name\":\"Test test\",\"email\":\"test@test.com\",\"document_type\":1,\"document\":\"12312312312\",\"phone\":\"+5511999998888\",\"date_of_birth\":\"1987-04-04\"}";
//
//		checkXss(mockMvc
//			.perform(post("/authorize-card/__test_gateway__").contentType(MediaType.APPLICATION_JSON)
//				.accept(MediaType.APPLICATION_JSON).content(payload))
//			.andDo(print()).andExpect(status().isNotImplemented()));
//	}
//
//	@Test
//	public void testRestAuthorizeDefaultInvalidPayload() throws Exception {
//		final String payload = "{\"user_id\":\"123456\",\"card_number\":\"ABC\",\"card_expiration_date\":\"ABC\",\"card_cvv\":\"123\",\"card_holder_name\":\"TEST TESTINGTON\",\"full_name\":\"Test test\",\"email\":\"test@test.com\",\"document_type\":1,\"document\":\"12312312312\",\"phone\":\"+5511999998888\",\"date_of_birth\":\"1987-04-04\"}";
//
//		checkXss(
//			mockMvc
//				.perform(post("/authorize-card").contentType(MediaType.APPLICATION_JSON)
//					.accept(MediaType.APPLICATION_JSON).content(payload))
//				.andDo(print()).andExpect(status().isUnprocessableEntity()));
//	}
}
