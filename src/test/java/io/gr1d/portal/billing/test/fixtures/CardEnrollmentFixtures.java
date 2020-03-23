package io.gr1d.portal.billing.test.fixtures;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import io.gr1d.portal.billing.model.CardEnrollment;
import io.gr1d.portal.billing.model.PersonalInfo;
import io.gr1d.portal.billing.model.enumerations.PaymentGateway;

public class CardEnrollmentFixtures implements TemplateLoader {
	
	@Override
	public void load() {
		Fixture.of(CardEnrollment.class).addTemplate("valid", new Rule() {
			{
				add("id", random(Long.class, range(1L, 200L)));
				add("uuid", "CE-" + UUID.randomUUID().toString());
				add("createdAt", LocalDateTime.now());
				add("lastUpdateAt", null);
				add("active", true);
				
				add("version", 0);
				add("userId", "test-user-id");
				add("cardId", "test-card-id");
				add("gateway", random(PaymentGateway.GR1D, PaymentGateway.PAGARME));
				add("personalInfo", one(PersonalInfo.class, "valid"));
			}
		});
		Fixture.of(CardEnrollment.class).addTemplate("preinsert").inherits("valid", new Rule() {
			{
				add("id", null);
				add("personalInfo", one(PersonalInfo.class, "preinsert"));
			}
		});
	}
	
}
