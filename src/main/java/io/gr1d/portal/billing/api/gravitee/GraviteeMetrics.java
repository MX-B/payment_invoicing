package io.gr1d.portal.billing.api.gravitee;

import java.util.Map;

public class GraviteeMetrics {

	private Map<String, Long> values;
	
	public Map<String, Long> getValues() {
		return values;
	}
	
	public void setValues(final Map<String, Long> values) {
		this.values = values;
	}
	
	long sum2xxHits() {
		return values.keySet().stream()
				.filter(status -> status.startsWith("2"))
				.mapToLong(values::get).sum();
	}
}
