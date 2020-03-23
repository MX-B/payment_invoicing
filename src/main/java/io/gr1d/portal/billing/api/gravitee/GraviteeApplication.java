package io.gr1d.portal.billing.api.gravitee;

public class GraviteeApplication {
	
	private String id;
	
	private String status;
	
	private GraviteeUser owner;
	
	public GraviteeUser getOwner() {
		return owner;
	}
	
	public void setOwner(final GraviteeUser owner) {
		this.owner = owner;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(final String status) {
		this.status = status;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(final String id) {
		this.id = id;
	}
}
