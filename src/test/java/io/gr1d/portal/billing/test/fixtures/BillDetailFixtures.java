package io.gr1d.portal.billing.test.fixtures;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import io.gr1d.portal.billing.model.InvoiceItem;

public class BillDetailFixtures implements TemplateLoader {
	
	@Override
	public void load() {
		Fixture.of(InvoiceItem.class).addTemplate("100", new Rule() {
			{
				add("id", random(Long.class, range(1L, 10000L)));
				add("active", true);
				add("itemId", "test-api-id");
				add("createdAt", LocalDateTime.now());
				add("quantity", 1000L);
				add("uuid", "BD-" + UUID.randomUUID().toString());
				add("value", 1000000000L);
				add("unitValue", 1000000L);
			}
		});
		
		Fixture.of(InvoiceItem.class).addTemplate("250", new Rule() {
			{
				add("id", random(Long.class, range(1L, 10000L)));
				add("active", true);
				add("itemId", "test-api-id");
				add("createdAt", LocalDateTime.now());
				add("quantity", 5000L);
				add("uuid", "BD-" + UUID.randomUUID().toString());
				add("value", 2500000000L);
				add("unitValue", 500000L);
			}
		});
	}
	
}
