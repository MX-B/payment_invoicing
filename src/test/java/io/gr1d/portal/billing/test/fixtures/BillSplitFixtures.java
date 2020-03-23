package io.gr1d.portal.billing.test.fixtures;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import io.gr1d.portal.billing.model.InvoiceRecipientSplit;

public class BillSplitFixtures implements TemplateLoader {
	
	@Override
	public void load() {
		Fixture.of(InvoiceRecipientSplit.class).addTemplate("10", new Rule() {
			{
				add("id", random(Long.class, range(1L, 200L)));
				add("uuid", "BS-" + UUID.randomUUID().toString());
				add("createdAt", LocalDateTime.now());
				add("lastUpdateAt", null);
				add("active", true);
				
				add("recipientId", "test-recipient-id");
				add("value", 10L);
			}
		});
		
		Fixture.of(InvoiceRecipientSplit.class).addTemplate("20", new Rule() {
			{
				add("id", random(Long.class, range(1L, 200L)));
				add("uuid", "BS-" + UUID.randomUUID().toString());
				add("createdAt", LocalDateTime.now());
				add("lastUpdateAt", null);
				add("active", true);
				
				add("recipientId", "test-recipient-id");
				add("value", 20L);
			}
		});
		
		Fixture.of(InvoiceRecipientSplit.class).addTemplate("25", new Rule() {
			{
				add("id", random(Long.class, range(1L, 200L)));
				add("uuid", "BS-" + UUID.randomUUID().toString());
				add("createdAt", LocalDateTime.now());
				add("lastUpdateAt", null);
				add("active", true);
				
				add("recipientId", "test-recipient-id");
				add("value", 25L);
			}
		});
		
		Fixture.of(InvoiceRecipientSplit.class).addTemplate("50", new Rule() {
			{
				add("id", random(Long.class, range(1L, 200L)));
				add("uuid", "BS-" + UUID.randomUUID().toString());
				add("createdAt", LocalDateTime.now());
				add("lastUpdateAt", null);
				add("active", true);
				
				add("recipientId", "test-recipient-id");
				add("value", 50L);
			}
		});
		
		Fixture.of(InvoiceRecipientSplit.class).addTemplate("75", new Rule() {
			{
				add("id", random(Long.class, range(1L, 200L)));
				add("uuid", "BS-" + UUID.randomUUID().toString());
				add("createdAt", LocalDateTime.now());
				add("lastUpdateAt", null);
				add("active", true);
				
				add("recipientId", "test-recipient-id");
				add("value", 75L);
			}
		});
	}
	
}
