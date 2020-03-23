package io.gr1d.portal.billing.util;

import static java.util.Optional.ofNullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public final class ToString {
	private static final String[] EXCLUDED_FIELDS = { "pass", "auth", "card", "key" };
	private static final FilterProvider FILTER;
	
	public static ToString INSTANCE;
	
	static {
		FILTER = new SimpleFilterProvider().addFilter("removeSensitiveProperties", new SerializeFilter(EXCLUDED_FIELDS));
	}
	
	public static void init(final ObjectMapper objectMapper) {
		INSTANCE = new ToString(objectMapper);
	}
	
	private ToString(final ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	public static String toString(final Object obj) {
		return ofNullable(INSTANCE).map(i -> i.asString(obj)).orElse("[ToString not initialized]");
	}
	
	private final ObjectMapper objectMapper;
	
	public String asString(final Object obj) {
		final String objectClassName = obj.getClass().getName();
		
		try {
			final String objectAsJson = asJson(obj);
			return String.format("%s %s", objectClassName, objectAsJson);
		} catch (final Throwable e) {
			return String.format(
				"%s [error creating json] {\"json_exception\":\"%s\",\"json_exception_message\":\"%s\"}",
				objectClassName, (e.getClass().equals(RuntimeException.class) ? e.getCause().getClass() : e.getClass()).getName(), e.getLocalizedMessage());
		}
	}
	
	private String asJson(final Object obj) {
		return ofNullable(objectMapper).map(mapper -> {
			try {
				return mapper.writer(FILTER).writeValueAsString(obj);
			} catch (final JsonProcessingException e) {
				throw new RuntimeException("[ToString.toString() json processing exception]", e);
			}
		}).orElse("[ToString not initialized]");
	}
	
	private static class SerializeFilter extends SimpleBeanPropertyFilter implements java.io.Serializable {
		private static final long serialVersionUID = 1L;
		
		/**
		 * Set of property names to filter out.
		 */
		private final Set<String> propertiesToExclude;
		
		public SerializeFilter(final String[] properties) {
			propertiesToExclude = new HashSet<>(Arrays.asList(properties));
		}
		
		private boolean include(final String fieldName) {
			for (final String prop : propertiesToExclude) {
				if (fieldName.contains(prop)) {
					return false;
				}
			}
			
			return true;
		}
		
		@Override
		protected boolean include(final BeanPropertyWriter writer) {
			return include(writer.getName());
		}
		
		@Override
		protected boolean include(final PropertyWriter writer) {
			return include(writer.getName());
		}
	}
}
