
package com.vabrant.actionsystem.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

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

	public static Object executePrivateMethod (String name, Class klass, Class[] parameterTypes, Object object, Object[] args) {
		try {
			Method m = ClassReflection.getDeclaredMethod(klass, name, parameterTypes);
			m.setAccessible(true);
			return m.invoke(object, args);
		} catch (ReflectionException e) {
			e.printStackTrace();
			Gdx.app.exit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
