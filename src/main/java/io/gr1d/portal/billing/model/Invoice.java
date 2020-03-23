package io.gr1d.portal.billing.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.gr1d.portal.billing.model.enumerations.PaymentGateway;
import io.gr1d.portal.billing.model.enumerations.PaymentStatus;

/**
 * This class represents an Invoice of something.
 * 
 * @author Rafael M. Lins
 *
 */
@Entity
@Table(name = "invoice")
public class Invoice extends BaseModel {
	private static final long serialVersionUID = -9150757794046600900L;
	
	@Column(name = "user_id", nullable = false, length = 64)
	private String userId;
	
	@Column(name = "gateway_transaction_id", length = 64)
	private String gatewayTransactionId;
	
	@Min(0)
	@Column(nullable = false)
	private long value;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "payment_status_id", nullable = false)
	private PaymentStatus paymentStatus;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "payment_gateway_id", nullable = false)
	private PaymentGateway paymentGateway;
	
	@OneToMany(mappedBy = "invoice", cascade = ALL, fetch = LAZY)
	private Collection<InvoicePaymentStatusHistory> paymentStatusHistory;
	
	@OneToMany(mappedBy = "invoice", cascade = ALL, fetch = LAZY)
	private Collection<InvoiceItem> items;
	
	@OneToMany(mappedBy = "invoice", cascade = ALL, fetch = LAZY)
	private Collection<InvoiceRecipientSplit> split;
	
	@Override
	protected String nextUuid() {
		return String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	public String getGatewayTransactionId() {
		return gatewayTransactionId;
	}

	public void setGatewayTransactionId(final String gatewayTransactionId) {
		this.gatewayTransactionId = gatewayTransactionId;
	}

	public long getValue() {
		return value;
	}

	public void setValue(final long value) {
		this.value = value;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(final PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public PaymentGateway getPaymentGateway() {
		return paymentGateway;
	}

	public void setPaymentGateway(final PaymentGateway paymentGateway) {
		this.paymentGateway = paymentGateway;
	}

	public Collection<InvoicePaymentStatusHistory> getPaymentStatusHistory() {
		return paymentStatusHistory;
	}

	public void setPaymentStatusHistory(final Collection<InvoicePaymentStatusHistory> paymentStatusHistory) {
		this.paymentStatusHistory = paymentStatusHistory;
	}

	public Collection<InvoiceItem> getItems() {
		return items;
	}

	public void setItems(final Collection<InvoiceItem> items) {
		this.items = items;
	}

	public Collection<InvoiceRecipientSplit> getSplit() {
		return split;
	}

	public void setSplit(final Collection<InvoiceRecipientSplit> split) {
		this.split = split;
	}

	public boolean setStatus(final PaymentStatus status, final String gatewayStatus) {
		return setStatus(status, gatewayStatus, LocalDateTime.now());
	}
	
	public boolean setStatus(final PaymentStatus status, final String gatewayStatus, final LocalDateTime timestamp) {
		if (getPaymentStatus() == null || !status.equals(getPaymentStatus())) {
			final InvoicePaymentStatusHistory entry = new InvoicePaymentStatusHistory();
			entry.setInvoice(this);
			entry.setStatus(status);
			entry.setTimestamp(timestamp);
			entry.setGatewayStatus(gatewayStatus);
			entry.setPreviousStatus(getPaymentStatus());
			
			setPaymentStatus(status);
			
			if (paymentStatusHistory == null) {
				paymentStatusHistory = new ArrayList<>(1);
			}
			
			return paymentStatusHistory.add(entry);
		}
		
		return false;
	}
}
