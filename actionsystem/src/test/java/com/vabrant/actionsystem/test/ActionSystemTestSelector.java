package com.vabrant.actionsystem.test;

import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.vabrant.testbase.TestBaseApplicationListener;
import com.vabrant.testbase.TestSelector;

public class ActionSystemTestSelector extends TestSelector{

	static ExtendViewport viewport;

	static {
		viewport = new ExtendViewport(480, 320);
	}
	
	public ActionSystemTestSelector(TestBaseApplicationListener app) {
		super(app, viewport);
	}
}
