package io.gr1d.portal.billing.test.fixtures;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import io.gr1d.portal.billing.model.PersonalAddress;
import io.gr1d.portal.billing.model.PersonalInfo;

public class PersonalInfoFixtures implements TemplateLoader {
	
	@Override
	public void load() {
		Fixture.of(PersonalInfo.class).addTemplate("valid", new Rule() {
			{
				add("id", random(Long.class, range(1L, 200L)));
				add("uuid", "PI-" + UUID.randomUUID().toString());
				add("createdAt", LocalDateTime.now());
				add("lastUpdateAt", null);
				add("active", true);
				
				add("fullName", firstName());
				add("email", "test@email.com");
				add("documentType", random(Integer.class, range(1, 2)));
				add("document", cnpj());
				add("phone", "+5511999998888");
				add("nationality", "br");
				add("dateOfBirth", LocalDate.now().minusYears(25));
				add("address", one(PersonalAddress.class, "valid"));
			}
		});
		Fixture.of(PersonalInfo.class).addTemplate("preinsert").inherits("valid", new Rule() {
			{
				add("id", null);
				add("address", one(PersonalAddress.class, "preinsert"));
			}
		});
	}
	
}
