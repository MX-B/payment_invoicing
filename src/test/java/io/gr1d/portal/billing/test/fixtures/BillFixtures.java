package io.gr1d.portal.billing.test.fixtures;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import io.gr1d.portal.billing.model.CardEnrollment;
import io.gr1d.portal.billing.model.Invoice;
import io.gr1d.portal.billing.model.InvoiceItem;
import io.gr1d.portal.billing.model.InvoicePaymentStatusHistory;
import io.gr1d.portal.billing.model.InvoiceRecipientSplit;
import io.gr1d.portal.billing.model.enumerations.PaymentGateway;
import io.gr1d.portal.billing.model.enumerations.PaymentStatus;

/**
 * Initializes and creates the fixtures with test data for testing
 * 
 * @author Rafael M. Lins
 *
 */
public class BillFixtures implements TemplateLoader {
	@Override
	public void load() {
		Fixture.of(Invoice.class).addTemplate("valid", new Rule() {
			{
				add("id", random(Long.class, range(1L, 200L)));
				add("uuid", "BILL-" + UUID.randomUUID().toString());
				add("createdAt", LocalDateTime.now());
				add("lastUpdateAt", null);
				add("active", true);
				// add("userId", "test-user-id");
				// add("cardId", "test-card-id");
				add("cardEnrollment", one(CardEnrollment.class, "valid"));
				add("value", 3500000000L); // R$ 350,00
				add("status", PaymentStatus.CREATED);
				add("gateway", random(PaymentGateway.class, PaymentGateway.GR1D, PaymentGateway.PAGARME));
//				add("referenceMonth", Integer
//					.parseInt(String.format("%04d%02d", LocalDate.now().getYear(), LocalDate.now().getMonthValue())));
				add("statusHistory", has(3).of(InvoicePaymentStatusHistory.class, "created", "processing", "success"));
				add("details", has(2).of(InvoiceItem.class, "100", "250"));
				add("split", has(2).of(InvoiceRecipientSplit.class, "50"));
			}
		});
		Fixture.of(Invoice.class).addTemplate("billCREATED").inherits("valid", new Rule() {
			{
				add("status", PaymentStatus.CREATED);
			}
		});
		Fixture.of(Invoice.class).addTemplate("billFAILED").inherits("valid", new Rule() {
			{
				add("status", PaymentStatus.FAILED);
			}
		});
		Fixture.of(Invoice.class).addTemplate("preinsert").inherits("valid", new Rule() {
			{
				add("id", null);
				add("cardEnrollment", one(CardEnrollment.class, "preinsert"));
				add("details", null);
				add("split", null);
				add("statusHistory", null);
			}
		});
	}
}
