package io.gr1d.portal.billing.model;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "invoice_recipient_split")
public class InvoiceRecipientSplit extends BaseModel {
	private static final long serialVersionUID = -8668982950503151267L;
	
	@NotNull
	@ManyToOne(optional = false, fetch = LAZY)
	@JoinColumn(name = "invoice_id", nullable = false)
	private Invoice invoice;
	
	@NotEmpty
	@Column(name = "recipient_id", nullable = false, length = 64)
	private String recipientId;
	
	@Min(0)
	@Column(nullable = false)
	private long value;
	
	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(final Invoice invoice) {
		this.invoice = invoice;
	}

	public String getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(final String recipientId) {
		this.recipientId = recipientId;
	}

	public long getValue() {
		return value;
	}

	public void setValue(final long value) {
		this.value = value;
	}
}
