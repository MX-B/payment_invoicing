package io.gr1d.portal.billing.api.gravitee;

public class GraviteePageOptions {

	private int current;
	
	private int size;
	
	private int perPage;
	
	private int totalPages;
	
	private int totalElements;
	
	public int getCurrent() {
		return current;
	}
	
	public void setCurrent(final int current) {
		this.current = current;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(final int size) {
		this.size = size;
	}
	
	public int getPerPage() {
		return perPage;
	}
	
	public void setPerPage(final int perPage) {
		this.perPage = perPage;
	}
	
	public int getTotalPages() {
		return totalPages;
	}
	
	public void setTotalPages(final int totalPages) {
		this.totalPages = totalPages;
	}
	
	public int getTotalElements() {
		return totalElements;
	}
	
	public void setTotalElements(final int totalElements) {
		this.totalElements = totalElements;
	}
}
