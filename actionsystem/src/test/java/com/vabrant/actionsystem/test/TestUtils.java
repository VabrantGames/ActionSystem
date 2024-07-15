
package com.vabrant.actionsystem.test;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;

public class TestUtils {

	public static final String SEPARATOR = System.getProperty("line.separator");

	public static void printTestHeader (String name) {
		System.out.println();
		System.out.println("//----------" + ' ' + name + ' ' + "----------//");
	}

	@SafeVarargs
	public static <T> T[] toArray (T... ar) {
		return ar;
	}

	public static <T> Object executePrivateMethod (String name, Class[] parameterTypes, T object, Object[] args) {
		try {
			Method m = ClassReflection.getDeclaredMethod(((T)object).getClass(), name, parameterTypes);
			m.setAccessible(true);
			return m.invoke(object, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> Object executePrivateMethod (String name, Class[] parameterTypes, Class klass, T object, Object[] args) {
		try {
			Method m = ClassReflection.getDeclaredMethod(klass, name, parameterTypes);
			m.setAccessible(true);
			return m.invoke(object, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
