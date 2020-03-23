package io.gr1d.portal.billing.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.gr1d.portal.billing.model.enumerations.PaymentGateway;

@Entity
@Table(name = "payment_gateway_interaction")
public class PaymentGatewayInteraction implements Serializable {
	private static final long serialVersionUID = 6232967359695787297L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	@NotNull
	@Column(name = "payment_gateway_id", nullable = false)
	private Long gateway;
	
	@NotNull
	@Column(nullable = false)
	private LocalDateTime timestamp;
	
	@NotEmpty
	@Size(min = 1, max = 256)
	@Column(nullable = false, length = 256)
	private String operation;
	
	@Size(min = 1, max = 256)
	@Column(length = 256)
	private String destination;
	
	@Column(name = "request_id", length = 42)
	private String requestId;
	
	@Column(length = 42)
	private String invoice;
	
	@Min(100)
	@Max(999)
	@Column(name = "http_code")
	private Integer httpCode;
	
	@Column(name = "http_message", length = 256)
	private String httpMessage;
	
	@Column(name = "http_verb", length = 16)
	private String httpVerb;
	
	/* Expected: MySQL TEXT Column type */
	@Lob
	@Size(max = 65535)
	@Column(name = "request_parameters")
	private String requestParameters;
	
	/* Expected: MySQL MEDIUMTEXT Column type */
	@Lob
	@Size(max = 16777215)
	private String payload;
	
	/* Expected: MySQL MEDIUMTEXT Column type */
	@Lob
	@Size(max = 16777215)
	private String headers;
	
	@PrePersist
	public void beforeSave() {
		if (timestamp == null) {
			timestamp = LocalDateTime.now();
		}
	}

	public Long getId() {
		return id;
	}

	public PaymentGatewayInteraction setId(final Long id) {
		this.id = id;
		return this;
	}

	public String getInvoice() {
		return invoice;
	}

	public PaymentGatewayInteraction setInvoice(final String invoice) {
		this.invoice = invoice;
		return this;
	}

	public Long getGateway() {
		return gateway;
	}

	public PaymentGatewayInteraction setGateway(final Long gateway) {
		this.gateway = gateway;
		return this;
	}

	public PaymentGatewayInteraction setGateway(final PaymentGateway gateway) {
		return setGateway(gateway.getId());
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public PaymentGatewayInteraction setTimestamp(final LocalDateTime timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public String getOperation() {
		return operation;
	}

	public PaymentGatewayInteraction setOperation(final String operation) {
		this.operation = operation;
		return this;
	}

	public String getDestination() {
		return destination;
	}

	public PaymentGatewayInteraction setDestination(final String destination) {
		this.destination = destination;
		return this;
	}

	public Integer getHttpCode() {
		return httpCode;
	}

	public PaymentGatewayInteraction setHttpCode(final Integer httpCode) {
		this.httpCode = httpCode;
		return this;
	}

	public String getHttpMessage() {
		return httpMessage;
	}

	public PaymentGatewayInteraction setHttpMessage(final String httpMessage) {
		this.httpMessage = httpMessage;
		return this;
	}

	public String getHttpVerb() {
		return httpVerb;
	}

	public PaymentGatewayInteraction setHttpVerb(final String httpVerb) {
		this.httpVerb = httpVerb;
		return this;
	}

	public String getRequestParameters() {
		return requestParameters;
	}

	public PaymentGatewayInteraction setRequestParameters(final String requestParameters) {
		this.requestParameters = requestParameters;
		return this;
	}

	public String getPayload() {
		return payload;
	}

	public PaymentGatewayInteraction setPayload(final String payload) {
		this.payload = payload;
		return this;
	}

	public String getHeaders() {
		return headers;
	}

	public PaymentGatewayInteraction setHeaders(final String headers) {
		this.headers = headers;
		return this;
	}
	
	public String getRequestId() {
		return requestId;
	}

	public PaymentGatewayInteraction setRequestId(final String requestId) {
		this.requestId = requestId;
		return this;
	}

	@Override
	public String toString() {
		return String.format("PaymentGatewayInteraction {\"id\":%d,\"gateway\":\"%s\",\"operation\":\"%s\"}", id, gateway, operation);
	}
}
