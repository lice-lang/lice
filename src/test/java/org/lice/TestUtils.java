package org.lice;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Glavo on 17-9-22.
 *
 * @author Glavo
 * @since 4.0.0
 */
public class TestUtils {
	@SuppressWarnings("unchecked")
	public static  <T> T newInstance(Class<T> c) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Constructor<?> declaredConstructor = c.getDeclaredConstructor();
		declaredConstructor.setAccessible(true);
		return (T) declaredConstructor.newInstance();
	}

	@Test
	public void testNewInstance() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		assertEquals(newInstance(String.class), "");
	}
}
