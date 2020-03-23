package io.gr1d.portal.billing.test.factory;

import static io.gr1d.portal.billing.model.enumerations.PaymentGateway.PAGARME;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.gr1d.portal.billing.strategy.charge.ChargeFactory;
import io.gr1d.portal.billing.strategy.charge.ChargeStrategy;
import io.gr1d.portal.billing.test.SpringTestApplication;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringTestApplication.class)
public class ChargeFactoryTest {

	@Autowired private ChargeFactory chargeFactory;

	@Test
	public void testChargeFactory() {
		final ChargeStrategy strategy = chargeFactory.getImplementation(PAGARME.getName());
		assertThat(strategy).isNotNull();
	}

}
