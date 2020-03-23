package io.gr1d.portal.billing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;

@Entity
@Table(name = "invoice_item")
public class InvoiceItem extends BaseModel {
	private static final long serialVersionUID = 8057013706253339030L;
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "invoice_id", nullable = false)
	private Invoice invoice;
	
	@NotEmpty
	@Column(name = "external_id", nullable = false, length = 64)
	private String externalId;
	
	@Min(1)
	@Max(999999999999L)	// 12 digits, Max = 999 Billions
	@Column
	private long quantity;
	
	@Formula("quantity * unit_price")
	private long value;
	
	@Min(1)
	@Max(100000000000L)
	@Column(name = "unit_value")
	private long unitValue;
	
	public Invoice getInvoice() {
		return invoice;
	}

	public InvoiceItem setInvoice(final Invoice invoice) {
		this.invoice = invoice;
		return this;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(final String externalId) {
		this.externalId = externalId;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(final long quantity) {
		this.quantity = quantity;
	}

	public long getValue() {
		return value;
	}

	public void setValue(final long value) {
		this.value = value;
	}

	public long getUnitValue() {
		return unitValue;
	}

	public void setUnitValue(final long unitValue) {
		this.unitValue = unitValue;
	}
}
