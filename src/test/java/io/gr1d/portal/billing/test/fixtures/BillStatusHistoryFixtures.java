package io.gr1d.portal.billing.test.fixtures;

import java.time.LocalDateTime;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import io.gr1d.portal.billing.model.InvoicePaymentStatusHistory;
import io.gr1d.portal.billing.model.enumerations.PaymentStatus;

public class BillStatusHistoryFixtures implements TemplateLoader {
	
	@Override
	public void load() {
		Fixture.of(InvoicePaymentStatusHistory.class).addTemplate("created", new Rule() {
			{
				add("id", random(Long.class, range(1L, 10000L)));
				add("gatewayStatus", "CREATED");
				add("previousStatus", null);
				add("status", PaymentStatus.CREATED);
				add("timestamp", LocalDateTime.now());
			}
		});
		
		Fixture.of(InvoicePaymentStatusHistory.class).addTemplate("processing", new Rule() {
			{
				add("id", random(Long.class, range(1L, 10000L)));
				add("gatewayStatus", "PROCESSING");
				add("previousStatus", PaymentStatus.CREATED);
				add("status", PaymentStatus.PROCESSING);
				add("timestamp", LocalDateTime.now());
			}
		});
		
		Fixture.of(InvoicePaymentStatusHistory.class).addTemplate("success", new Rule() {
			{
				add("id", random(Long.class, range(1L, 10000L)));
				add("gatewayStatus", "PAID");
				add("previousStatus", PaymentStatus.PROCESSING);
				add("status", PaymentStatus.SUCCESS);
				add("timestamp", LocalDateTime.now());
			}
		});
		
		Fixture.of(InvoicePaymentStatusHistory.class).addTemplate("processingRetry", new Rule() {
			{
				add("id", random(Long.class, range(1L, 10000L)));
				add("gatewayStatus", "SOMETHING_HAPPENED_TRY_AGAIN");
				add("previousStatus", PaymentStatus.PROCESSING);
				add("status", PaymentStatus.PROCESSING_RETRY);
				add("timestamp", LocalDateTime.now());
			}
		});
		
		Fixture.of(InvoicePaymentStatusHistory.class).addTemplate("refunded", new Rule() {
			{
				add("id", random(Long.class, range(1L, 10000L)));
				add("gatewayStatus", "GAVE_THE_MONEY_BACK");
				add("previousStatus", PaymentStatus.REFUNDING);
				add("status", PaymentStatus.REFUNDED);
				add("timestamp", LocalDateTime.now());
			}
		});
		
		Fixture.of(InvoicePaymentStatusHistory.class).addTemplate("refunding", new Rule() {
			{
				add("id", random(Long.class, range(1L, 10000L)));
				add("gatewayStatus", "USER_ASKED_FOR_CASH_BACK");
				add("previousStatus", PaymentStatus.SUCCESS);
				add("status", PaymentStatus.REFUNDING);
				add("timestamp", LocalDateTime.now());
			}
		});
	}
	
}
