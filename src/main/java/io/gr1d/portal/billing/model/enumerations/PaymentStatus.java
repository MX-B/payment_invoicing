package io.gr1d.portal.billing.model.enumerations;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * An {@link BaseEnum enum} entity that represents a Payment Status
 * 
 * @author Sergio Marcelino
 * @author Rafael M. Lins
 *
 */
@Entity
@Table(name = "payment_status")
public class PaymentStatus extends BaseEnum {
	private static final long serialVersionUID = -1355340639582452017L;
	
	public static final PaymentStatus CREATED = new PaymentStatus(1L, "CREATED", "gr1d.portal.billing.enum.paymentStatus.created");
	public static final PaymentStatus PROCESSING = new PaymentStatus(2L, "PROCESSING", "gr1d.portal.billing.enum.paymentStatus.processing");
	public static final PaymentStatus PROCESSING_RETRY = new PaymentStatus(3L, "PROCESSING_RETRY", "gr1d.portal.billing.enum.paymentStatus.processing_retry");
	public static final PaymentStatus REFUNDING = new PaymentStatus(4L, "REFUNDING", "gr1d.portal.billing.enum.paymentStatus.refunding");
	public static final PaymentStatus REFUNDED = new PaymentStatus(5L, "REFUNDED", "gr1d.portal.billing.enum.paymentStatus.refunded");
	public static final PaymentStatus FAILED = new PaymentStatus(6L, "FAILED", "gr1d.portal.billing.enum.paymentStatus.failed");
	public static final PaymentStatus SUCCESS = new PaymentStatus(7L, "SUCCESS", "gr1d.portal.billing.enum.paymentStatus.success");
	
	public PaymentStatus() {
		// NOOP
	}
	
	private PaymentStatus(final Long id, final String name, final String description) {
		setId(id);
		setName(name);
		setDescription(description);
		
		addEnum(this);
	}
	
	/**
	 * @return A {@link PaymentStatus} of a given {@code name}
	 */
	public static PaymentStatus valueOf(final String name) {
		return getEnum(name, PaymentStatus.class);
	}

	public boolean canBeCharged() {
		return equals(FAILED) || equals(CREATED);
	}
}
