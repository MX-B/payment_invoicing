package io.gr1d.portal.billing.messaging.listener;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.gr1d.portal.billing.charging.ChargingNotificator;
import io.gr1d.portal.billing.messaging.model.UpdateBillStatusMessage;
import io.gr1d.portal.billing.model.Invoice;
import io.gr1d.portal.billing.model.enumerations.PaymentStatus;
import io.gr1d.portal.billing.repository.InvoiceRepository;

public class UpdateBillStatusListenerTest {

	@Mock private ObjectMapper objectMapper;
	@Mock private ChargingNotificator notificator;
	@Mock private InvoiceRepository billRepository;

	private UpdateBillStatusListener listener;

	@Before
	public void startUp() {
		MockitoAnnotations.initMocks(this);
		listener = new UpdateBillStatusListener(objectMapper, billRepository, notificator);
	}

	@Test
	public void shouldNotifyWhenNoCardIsFound() throws IOException {
		final String billUid = "1278912y7381231278";
		final UpdateBillStatusMessage message = new UpdateBillStatusMessage();
		message.setGatewayStatus("refused");
		message.setBillId(billUid);
		message.setTimestamp(LocalDateTime.now());
		message.setStatus(PaymentStatus.FAILED);

		when(objectMapper.readValue(anyString(), same(UpdateBillStatusMessage.class))).thenReturn(message);
		when(billRepository.findByUuid(anyString())).thenReturn(null);
		
		final String payload = "1ey89d71y";
		listener.onMessage(payload);

		verify(objectMapper, times(1)).readValue(eq(payload), same(UpdateBillStatusMessage.class));
		verify(billRepository, times(1)).findByUuid(eq(billUid));
		verify(notificator, times(1)).notifyBillNotFound(same(message));
	}

	@Test
	public void shouldNotifyPaymentIsRefused() throws IOException {
		final String billUid = "1278912y7381231278";
		final UpdateBillStatusMessage message = new UpdateBillStatusMessage();
		message.setGatewayStatus("refused");
		message.setBillId(billUid);
		message.setTimestamp(LocalDateTime.now());
		message.setStatus(PaymentStatus.FAILED);

		final Invoice bill = Mockito.mock(Invoice.class);
		when(bill.getUuid()).thenReturn(billUid);

		when(objectMapper.readValue(anyString(), same(UpdateBillStatusMessage.class))).thenReturn(message);
		when(billRepository.findByUuid(anyString())).thenReturn(bill);

		final String payload = "1ey89d71y";
		listener.onMessage(payload);

		verify(objectMapper, times(1)).readValue(eq(payload), same(UpdateBillStatusMessage.class));
		verify(billRepository, times(1)).findByUuid(eq(billUid));
		verify(notificator, times(1)).notifyChargeFailed(same(bill), same(message.getTimestamp()));
		verify(billRepository, times(1)).save(same(bill));
		verify(bill, times(1)).setStatus(same(message.getStatus()), same(message.getGatewayStatus()), same(message.getTimestamp()));

		verify(notificator, times(0)).notifyBillNotFound(any());
		verify(notificator, times(0)).notifyChargeRefunded(any(), any());
	}

	@Test
	public void shouldNotifyPaymentIsRefunded() throws IOException {
		final String billUid = "1278912y7381231278";
		final UpdateBillStatusMessage message = new UpdateBillStatusMessage();
		message.setGatewayStatus("refunded");
		message.setBillId(billUid);
		message.setTimestamp(LocalDateTime.now());
		message.setStatus(PaymentStatus.REFUNDED);

		final Invoice bill = Mockito.mock(Invoice.class);
		when(bill.getUuid()).thenReturn(billUid);

		when(objectMapper.readValue(anyString(), same(UpdateBillStatusMessage.class))).thenReturn(message);
		when(billRepository.findByUuid(anyString())).thenReturn(bill);

		final String payload = "1ey89d71y";
		listener.onMessage(payload);

		verify(objectMapper, times(1)).readValue(eq(payload), same(UpdateBillStatusMessage.class));
		verify(billRepository, times(1)).findByUuid(eq(billUid));
		verify(notificator, times(1)).notifyChargeRefunded(same(bill), same(message.getTimestamp()));
		verify(billRepository, times(1)).save(same(bill));
		verify(bill, times(1)).setStatus(same(message.getStatus()), same(message.getGatewayStatus()), same(message.getTimestamp()));

		verify(notificator, times(0)).notifyBillNotFound(any());
		verify(notificator, times(0)).notifyChargeFailed(any(), any());
	}

	@Test
	public void shouldChangeStatusOK() throws IOException {
		final String billUid = "1278912y7381231278";
		final UpdateBillStatusMessage message = new UpdateBillStatusMessage();
		message.setGatewayStatus("paid");
		message.setBillId(billUid);
		message.setTimestamp(LocalDateTime.now());
		message.setStatus(PaymentStatus.SUCCESS);

		final Invoice bill = Mockito.mock(Invoice.class);
		when(bill.getUuid()).thenReturn(billUid);

		when(objectMapper.readValue(anyString(), same(UpdateBillStatusMessage.class))).thenReturn(message);
		when(billRepository.findByUuid(anyString())).thenReturn(bill);

		final String payload = "1ey89d71y";
		listener.onMessage(payload);

		verify(objectMapper, times(1)).readValue(eq(payload), same(UpdateBillStatusMessage.class));
		verify(billRepository, times(1)).findByUuid(eq(billUid));
		verify(billRepository, times(1)).save(same(bill));
		verify(bill, times(1)).setStatus(same(message.getStatus()), same(message.getGatewayStatus()), same(message.getTimestamp()));

		verify(notificator, times(0)).notifyBillNotFound(any());
		verify(notificator, times(0)).notifyChargeFailed(any(), any());
		verify(notificator, times(0)).notifyChargeRefunded(any(), any());
	}

}