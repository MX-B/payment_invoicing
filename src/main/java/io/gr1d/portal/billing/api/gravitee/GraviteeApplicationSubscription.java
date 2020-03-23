package io.gr1d.portal.billing.api.gravitee;

public class GraviteeApplicationSubscription {
	
	private String api;
	
	private String application;
	
	private String status;
	
	public String getApi() {
		return api;
	}
	
	public void setApi(final String api) {
		this.api = api;
	}
	
	public String getApplication() {
		return application;
	}
	
	public void setApplication(final String application) {
		this.application = application;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(final String status) {
		this.status = status;
	}
	
	@Override
	public int hashCode() {
		return application.hashCode();
	}
}
