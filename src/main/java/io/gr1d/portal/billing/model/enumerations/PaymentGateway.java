package io.gr1d.portal.billing.model.enumerations;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * An {@link BaseEnum enum} entity that represents a Payment Gateway
 * 
 * @author Sergio Marcelino
 * @author Rafael M. Lins
 *
 */
@Entity
@Table(name = "payment_gateway")
public class PaymentGateway extends BaseEnum {
	private static final long serialVersionUID = 6140906555324249749L;
	
	public static final PaymentGateway GR1D = new PaymentGateway(1L, "GR1D", "gr1d.portal.billing.enum.paymentGateway.gr1d");
	public static final PaymentGateway PAGARME = new PaymentGateway(2L, "PAGARME", "gr1d.portal.billing.enum.paymentGateway.pagarme");
	
	public PaymentGateway() {
		// NOOP
	}
	
	private PaymentGateway(final Long id, final String name, final String description) {
		setId(id);
		setName(name);
		setDescription(description);
		
		addEnum(this);
	}
	
	/**
	 * @return A {@link PaymentGateway} of a given {@code name}
	 */
	public static PaymentGateway valueOf(final String name) {
		return getEnum(name, PaymentGateway.class);
	}
}
