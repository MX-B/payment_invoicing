package io.gr1d.portal.billing.test.fixtures.cardAuthorization;

import static io.gr1d.portal.billing.test.fixtures.FixtureUtils.number;
import static io.gr1d.portal.billing.test.fixtures.FixtureUtils.randomLocalDate;

import java.time.LocalDate;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.function.impl.CpfFunction;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import io.gr1d.portal.billing.request.CardAuthorizationRequest;

public class CardAuthorizationFixtures implements TemplateLoader {

	@Override
	public void load() {
		Fixture.of(CardAuthorizationRequest.class).addTemplate("fixedCard_randomPersonalInfo", new Rule() {
			{
				add("userId", "TEST-USER");
				
				// Card
				add("cardNumber", "5130202065415212");
				add("cardExpirationDate", "0220");
				add("cardCvv", "261");
				add("cardHolderName", "TEST USER");
				
				// PersonalInfo
				add("fullName", "Test User");
				add("email", "test@user.com");
				add("documentType", 1);
				add("document", new CpfFunction(false));
				add("phone", "+5511" + number(9));
				add("dateOfBirth", randomLocalDate("1980-01-01", "2000-12-31"));
			}
		});
		
		Fixture.of(CardAuthorizationRequest.class).addTemplate("fixedCard_fixedPersonalInfo", new Rule() {
			{
				add("userId", "TEST-USER");
				
				// Card
				add("cardNumber", "5130202065415212");
				add("cardExpirationDate", "0220");
				add("cardCvv", "261");
				add("cardHolderName", "TEST USER");
				
				// PersonalInfo
				add("fullName", "Test User");
				add("email", "test@user.com");
				add("documentType", 1);
				add("document", "12345678901");
				add("phone", "+5511999998888");
				add("dateOfBirth", LocalDate.parse("1991-09-04"));
			}
		});
		
		Fixture.of(CardAuthorizationRequest.class).addTemplate("randomCard_randomPersonalInfo", new Rule() {
			{
				add("userId", "TEST-USER");
				
				// Card
				add("cardNumber", number(16));
				add("cardExpirationDate", number(4));
				add("cardCvv", number(3));
				add("cardHolderName", "TEST USER");
				
				// PersonalInfo
				add("fullName", "Test User");
				add("email", "test@user.com");
				add("documentType", 1);
				add("document", new CpfFunction(false));
				add("phone", "+5511" + number(9));
				add("dateOfBirth", randomLocalDate("1980-01-01", "2000-12-31"));
			}
		});
		
		Fixture.of(CardAuthorizationRequest.class).addTemplate("randomCard_fixedPersonalInfo", new Rule() {
			{
				add("userId", "TEST-USER");
				
				// Card
				add("cardNumber", number(16));
				add("cardExpirationDate", number(4));
				add("cardCvv", number(3));
				add("cardHolderName", "TEST USER");
				
				// PersonalInfo
				add("fullName", "Test User");
				add("email", "test@user.com");
				add("documentType", 1);
				add("document", "12345678901");
				add("phone", "+5511999998888");
				add("dateOfBirth", LocalDate.parse("1991-09-04"));
			}
		});
	}
}
