package io.gr1d.portal.billing.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.gr1d.portal.billing.model.EmailTemplateConfig;
import io.gr1d.portal.billing.repository.EmailTemplateConfigRepository;
import io.gr1d.portal.billing.service.EmailTemplate;
import io.gr1d.portal.billing.test.SpringTestApplication;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringTestApplication.class)
public class EmailTemplateIntegrationTest {
	
	@Autowired
	private EmailTemplateConfigRepository repository;
	
	@Test
	@FlywayTest
	public void testRepository() {
		final EmailTemplateConfig config = new EmailTemplateConfig();
		config.setCode("ANY_CODE");
		config.setTemplateId("73029418-95f0-48fd-8e0f-2d801d1830aa");
		
		final EmailTemplateConfig savedConfig = repository.save(config);
		assertThat(savedConfig.getId()).isNotNull();
		
		final EmailTemplateConfig byCode = repository.findByCode("ANY_CODE");
		assertThat(byCode.getId()).isEqualTo(savedConfig.getId());
		assertThat(byCode.getTemplateId()).isEqualTo(savedConfig.getTemplateId());
		assertThat(byCode.getCode()).isEqualTo(savedConfig.getCode());
	}
	
	@Test
	@FlywayTest
	public void testAllEnumAreInserted() {
		Arrays.stream(EmailTemplate.values())
				.map(Enum::toString)
				.forEach(template -> assertNotNull(template + " should be inserted on database", repository.findByCode(template)));
	}
	
}
