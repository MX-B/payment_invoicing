package io.gr1d.portal.billing.api.gravitee;

import java.util.List;

public class GraviteePage<T> {
	
	private List<T> data;
	
	private GraviteePageOptions page;
	
	public List<T> getData() {
		return data;
	}
	
	public void setData(final List<T> data) {
		this.data = data;
	}
	
	public GraviteePageOptions getPage() {
		return page;
	}
	
	public boolean hasMorePages() {
		return getPage().getCurrent() < getPage().getTotalPages();
	}
	
	public void setPage(final GraviteePageOptions page) {
		this.page = page;
	}
}
