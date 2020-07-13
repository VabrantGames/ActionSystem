package com.vabrant.actionsystem.test.tests;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.utils.ObjectMap;

public class ActionSystemTests {
	
	public static final List<Class<? extends ApplicationListener>> TEST_CLASSES = Arrays.<Class<? extends ApplicationListener>>asList(
			MoveActionTest.class
	);
	
	public static final ObjectMap<String, Class<? extends ApplicationListener>> TESTS;
	
	static {
		TESTS = new ObjectMap<>(TEST_CLASSES.size());
		for(Class<? extends ApplicationListener> c : TEST_CLASSES) {
			TESTS.put(c.getSimpleName(), c);
		}
	}
	
	public static ApplicationListener createTestApplicationListener(String name) {
		Class<? extends ApplicationListener> c = TESTS.get(name);
		
		try {
			return c.newInstance();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
