package com.vabrant.actionsystem.test;

import com.badlogic.gdx.utils.viewport.Viewport;

public class ActionSystemTestConstantsAndUtils {

	public static final int DEFAULT_WIDTH = 960;
	public static final int DEFAULT_HEIGHT = 640;
	
	public static final String SEPARATOR = System.getProperty("line.separator");

//	private static final String pattern = "//----------//";
	
	public static void printTestHeader(String name) {
		System.out.println();
    	System.out.println("//----------" + ' ' + name + ' ' + "----------//");
	}
	
	public static void centerTestObject(TestObject object, Viewport viewport) {
		float x = (viewport.getWorldWidth() - object.width) / 2;
		float y = (viewport.getWorldHeight() - object.height) / 2;
		object.setPosition(x, y);
	}
}
