package io.gr1d.portal.billing.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import io.gr1d.portal.billing.model.EmailTemplateConfig;
import io.gr1d.portal.billing.repository.EmailTemplateConfigRepository;
import io.gr1d.portal.billing.service.EmailService;
import io.gr1d.portal.billing.service.EmailTemplate;

public class EmailServiceTest {
	
	@Mock
	private SendGrid sendGrid;
	@Mock
	private EmailTemplateConfigRepository repository;
	
	private EmailService emailService;
	
	private static final String SENDER_EMAIL = "sender@gr1d.io";
	private static final String DEFAULT_RECEIVER = "default@gr1d.io";
	
	@Before
	public void setUp() throws IllegalArgumentException {
		initMocks(this);
		emailService = new EmailService(SENDER_EMAIL, DEFAULT_RECEIVER, repository, sendGrid);
	}
	
	@Test
	public void testEmails() throws IOException {
		final Response response = new Response();
		response.setStatusCode(200);
		
		final String templateId = "SOME_TEMPLATE_ID";
		final EmailTemplateConfig config = new EmailTemplateConfig();
		config.setCode(EmailTemplate.USER_BILL_CHARGE_ERROR.toString());
		config.setTemplateId(templateId);
		
		when(sendGrid.api(any())).thenReturn(response);
		when(repository.findByCode(any())).thenReturn(config);
		
		emailService.createEmail().add("variable", "value").withTemplate(EmailTemplate.USER_BILL_CHARGE_ERROR)
			.withDefaultReceiver().withContent("hello world").withContentType("text/html").send();
		
		final ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
		verify(sendGrid, times(1)).api(requestCaptor.capture());
		verify(repository, times(1)).findByCode(eq(EmailTemplate.USER_BILL_CHARGE_ERROR.toString()));
		
		final Request request = requestCaptor.getValue();
		assertThat(request.getBody()).isEqualTo("{" + "\"from\":{\"email\":\"sender@gr1d.io\"},"
			+ "\"personalizations\":[{\"to\":[{\"email\":\"default@gr1d.io\"}],"
			+ "\"substitutions\":{\"{{variable}}\":\"value\"}}],"
			+ "\"content\":[{\"type\":\"text/html\",\"value\":\"hello world\"}],"
			+ "\"template_id\":\"SOME_TEMPLATE_ID\"" + "}");
	}
	
}
