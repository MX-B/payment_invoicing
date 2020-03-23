package io.gr1d.portal.billing.model;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.gr1d.portal.billing.model.enumerations.PaymentStatus;
import io.gr1d.portal.billing.util.ToString;

@Entity
@Table(name = "invoice_payment_status_history")
public class InvoicePaymentStatusHistory implements Serializable {
	private static final long serialVersionUID = 7767309072883582426L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	@NotNull
	@ManyToOne(optional = false, fetch = LAZY)
	@JoinColumn(name = "invoice_id", nullable = false)
	private Invoice invoice;
	
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "previous_status_id")
	private PaymentStatus previousStatus;
	
	@NotNull
	@ManyToOne(optional = false, fetch = LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private PaymentStatus status;
	
	@Size(max = 256)
	@Column(name = "gateway_status", length = 256)
	private String gatewayStatus;
	
	@NotNull
	@Column(nullable = false)
	private LocalDateTime timestamp;
	
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(final Invoice invoice) {
		this.invoice = invoice;
	}

	public PaymentStatus getPreviousStatus() {
		return previousStatus;
	}

	public void setPreviousStatus(final PaymentStatus previousStatus) {
		this.previousStatus = previousStatus;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(final PaymentStatus status) {
		this.status = status;
	}

	public String getGatewayStatus() {
		return gatewayStatus;
	}

	public void setGatewayStatus(final String gatewayStatus) {
		this.gatewayStatus = gatewayStatus;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return ToString.toString(this);
	}
}
