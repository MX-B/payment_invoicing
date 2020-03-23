package io.gr1d.portal.billing.test.fixtures;

import java.util.UUID;

import br.com.six2six.fixturefactory.function.AtomicFunction;

public class RandomUuid implements AtomicFunction {
	
	@Override
	@SuppressWarnings("unchecked")
	public String generateValue() {
		return UUID.randomUUID().toString();
	}
}
