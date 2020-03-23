package io.gr1d.portal.billing.test.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import io.gr1d.portal.billing.api.gravitee.GraviteeApplication;
import io.gr1d.portal.billing.api.gravitee.GraviteeApplicationSubscription;
import io.gr1d.portal.billing.api.gravitee.GraviteeUser;

public class GraviteeFixtures implements TemplateLoader {
	
	@Override
	public void load() {
		final RandomUuid randomUuid = new RandomUuid();
		
		Fixture.of(GraviteeUser.class).addTemplate("valid", new Rule() {
			{
				add("id", randomUuid);
			}
		});
		
		Fixture.of(GraviteeApplication.class).addTemplate("active", new Rule() {
			{
				add("id", randomUuid);
				add("status", "ACTIVE");
				add("owner", one(GraviteeUser.class, "valid"));
			}
		});
		
		Fixture.of(GraviteeApplication.class).addTemplate("inactive").inherits("active", new Rule() {
			{
				add("status", "INACTIVE");
			}
		});
		
		Fixture.of(GraviteeApplicationSubscription.class).addTemplate("accepted", new Rule() {
			{
				add("api", randomUuid);
				add("application", randomUuid);
				add("status", "accepted");
			}
		});
		
		Fixture.of(GraviteeApplicationSubscription.class).addTemplate("pending").inherits("accepted", new Rule() {
			{
				add("status", "pending");
			}
		});
	}
	
}
