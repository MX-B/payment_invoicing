package io.gr1d.portal.billing.test.factory;

import static io.gr1d.portal.billing.model.enumerations.PaymentGateway.PAGARME;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.gr1d.portal.billing.strategy.card.authorization.CardAuthorizationFactory;
import io.gr1d.portal.billing.strategy.card.authorization.CardAuthorizationStrategy;
import io.gr1d.portal.billing.test.SpringTestApplication;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringTestApplication.class)
public class CardAuthorizationFactoryTest {

	@Autowired private CardAuthorizationFactory factory;

	@Test
	public void testCardAuthorizationFactory() {
		final CardAuthorizationStrategy strategy = factory.getImplementation(PAGARME.getName());
		assertThat(strategy).isNotNull();
	}

}
