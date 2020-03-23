package io.gr1d.portal.billing.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.springframework.test.web.servlet.ResultActions;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

/**
 * This class have some basic, universal tests. Simple stuff like "this class
 * have an annotation".
 * 
 * @author Rafael M. Lins
 *
 */
public enum TestUtils {
	;
	
	private static final String HEADER_XSS_NAME = "X-XSS-Protection";
	private static final String HEADER_XSS_VALUE = "1; mode=block";
	
	private static final String HEADER_OPTIONS_NAME = "X-Content-Type-Options";
	private static final String HEADER_OPTIONS_VALUE = "nosniff";
	
	private static final String HEADER_FRAME_NAME = "X-Frame-Options";
	private static final String HEADER_FRAME_VALUE = "DENY";
	
	public static ResultActions checkXss(final ResultActions resultActions) throws Exception {
		return resultActions.andExpect(header().string(HEADER_XSS_NAME, HEADER_XSS_VALUE))
			.andExpect(header().string(HEADER_OPTIONS_NAME, HEADER_OPTIONS_VALUE))
			.andExpect(header().string(HEADER_FRAME_NAME, HEADER_FRAME_VALUE));
	}
	
	private static final Gson gson = new GsonBuilder()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.registerTypeAdapter(LocalDate.class, (JsonSerializer) (src, typeOfSrc, context) ->
				new JsonPrimitive(((LocalDate) src).format(DateTimeFormatter.ISO_DATE)))
			.create();
	
	public static String json(final Object object) {
		return gson.toJson(object);
	}
	
	/**
	 * Checks if {@code klass} implements or extends {@code parent}
	 */
	public static void testImplements(final Class<?> klass, final Class<?> parent) {
		assertThat(parent.isAssignableFrom(klass), is(true));
	}
	
	/**
	 * Checks if {@code klass} have the specified annotations
	 */
	@SafeVarargs
	public static void testAnnotations(final Class<?> klass, final Class<? extends Annotation>... annotations) {
		Stream.of(annotations).filter(Objects::nonNull).forEach(annotation -> {
			assertThat(klass.getAnnotation(annotation), is(notNullValue()));
		});
	}
	
	/**
	 * Tests the
	 * {@code private static final Logger LOG = LoggerFactory.getLogger(ThisClass.class)}
	 * thing
	 */
	public static void testLogger(final Class<?> klass)
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// Field exists?
		final Field logField = klass.getDeclaredField("LOG");
		
		// Field must be a org.slf4j.Logger
		final Class<?> logFieldClass = logField.getType();
		assertThat(logFieldClass, is(equalTo(org.slf4j.Logger.class)));
		
		// Field must be "private static final"
		final int logFieldModifiers = logField.getModifiers();
		assertThat(Modifier.isPrivate(logFieldModifiers), is(true));
		assertThat(Modifier.isStatic(logFieldModifiers), is(true));
		assertThat(Modifier.isFinal(logFieldModifiers), is(true));
		
		// The value set on the field must be a Logger in which its name is the same as
		// the class name
		try {
			logField.setAccessible(true);
			
			final Logger logFieldValue = (Logger) logField.get(null);
			assertThat(logFieldValue, notNullValue());
			assertThat(logFieldValue.getName(), is(klass.getName()));
			
			logField.setAccessible(false);
		} catch (final SecurityException ioe) {
			System.out.println("Could not test the actual Logger object (reflection access to the field was denied)");
		}
	}
}
