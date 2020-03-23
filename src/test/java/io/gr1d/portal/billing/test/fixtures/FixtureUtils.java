package io.gr1d.portal.billing.test.fixtures;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import br.com.six2six.fixturefactory.function.AtomicFunction;

/**
 * Generates static random stuff, like a static date or some number.
 * 
 * @author Rafael M. Lins
 *
 */
public class FixtureUtils {
	public static final String date(final String from, final String to, final String format) {
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat(format);
			final long start = sdf.parse(from).getTime();
			final long end = sdf.parse(to).getTime();
			final long diff = end - start + 1;
			final long rand = Math.abs(new Random().nextLong()) % diff;
			
			return sdf.format(new Date(start + rand));
		} catch (final ParseException e) {
			return null;
		}
	}
	
	public static final AtomicFunction randomLocalDate(final String from, final String to) {
		return new AtomicFunction() {
			@Override
			public <T> T generateValue() {
				final LocalDate start = LocalDate.parse(from);
				final LocalDate end = LocalDate.parse(to);
				final long startDay = start.get(ChronoField.EPOCH_DAY);
				final long diff = end.get(ChronoField.EPOCH_DAY) - startDay;
				final long rand = Math.abs(new Random().nextLong()) % diff;
				return (T) LocalDate.ofEpochDay(startDay + rand);
			}
		};
	}
	
	public static final AtomicFunction randomLocalDateTime(final String from, final String to) {
		return new AtomicFunction() {
			@Override
			public <T> T generateValue() {
				final ZoneOffset localTime = ZoneOffset.of(ZoneOffset.systemDefault().getId());
				final LocalDateTime start = LocalDateTime.parse(from);
				final LocalDateTime end = LocalDateTime.parse(to);
				final long startSeconds = start.get(ChronoField.INSTANT_SECONDS);
				final long diff = end.get(ChronoField.INSTANT_SECONDS) - startSeconds;
				final long rand = Math.abs(new Random().nextLong()) % diff;
				return (T) LocalDateTime.ofEpochSecond(startSeconds + rand, 0, localTime);
			}
		};
	}
	
	public static final String number(final int digits) {
		final StringBuilder str = new StringBuilder();
		final Random rand = new Random();
		
		for (int i = 0; i < digits; i++) {
			str.append(rand.nextInt(10));
		}
		
		return str.toString();
	}
	
	public static final String json(final Object fixtureObject) {
		final ObjectMapper objMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
				.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		return json(fixtureObject, objMapper);
	}
	
	public static final String json(final Object fixtureObject, final ObjectMapper objMapper) {
		try {
			return objMapper.writeValueAsString(fixtureObject);
		} catch (final JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
