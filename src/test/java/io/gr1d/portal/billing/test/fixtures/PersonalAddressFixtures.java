package io.gr1d.portal.billing.test.fixtures;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import io.gr1d.portal.billing.model.PersonalAddress;

public class PersonalAddressFixtures implements TemplateLoader {
	
	@Override
	public void load() {
		Fixture.of(PersonalAddress.class).addTemplate("valid", new Rule() {
			{
				add("id", 1L);
				add("uuid", "PA-bf7bbacb-755e-11e8-8765-8cec4b1a4fcf");
				add("createdAt", LocalDateTime.now());
				add("lastUpdateAt", null);
				add("active", true);
				
				add("street", "Avenida Brigadeiro Faria Lima");
				add("number", "2391");
				add("neighborhood", "Jardim Paulistano");
				add("city", "São Paulo");
				add("county", "SP");
				add("postalCode", "01452905");
				add("country", "br");
				add("latitude", new BigDecimal("-23.5799195"));
				add("longitude", new BigDecimal("-46.6856682"));
			}
		});
		Fixture.of(PersonalAddress.class).addTemplate("preinsert").inherits("valid", new Rule() {
			{
				add("id", null);
			}
		});
	}
	
}
