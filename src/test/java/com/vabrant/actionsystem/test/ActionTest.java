package com.vabrant.actionsystem.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.vabrant.actionsystem.ActionAdapter;
import com.vabrant.actionsystem.ActionListener;
import com.vabrant.actionsystem.ActionPools;
import com.vabrant.actionsystem.DelayAction;

public class ActionTest extends ActionSystemTestScreen {
	
	private int currentTest = 0;
	private int maxTest = 9;
	ActionSystemTestObject testObject;
	Test test;
	
	public ActionTest() {
		ActionPools.create(DelayAction.class, 1, 3);
		
		testObject = new ActionSystemTestObject();
		testObject.setX(100);
		testObject.setY(100);
		
		test = new HasBeenPooledUnmanagedTest();
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.SPACE:
				if(test != null) test.runTest();
				break;
			case Keys.NUMPAD_1:
				if(test != null) test.check1();
				break;
			case Keys.NUMPAD_2:
				if(test != null) test.check2();
				break;
		}
		return super.keyDown(keycode);
	}

	private class HasBeenPooledUnmanagedTest implements Test{
		
		DelayAction unmanagedAction;
		
		public HasBeenPooledUnmanagedTest() {
			unmanagedAction = DelayAction.delay(2);
			unmanagedAction.unmanage();
			unmanagedAction.addListener(getUnmanagedActionListener());
			actionManager.addUnmanagedAction(unmanagedAction);
		}
		
		private ActionAdapter getUnmanagedActionListener() {
			return new ActionAdapter() {
				@Override
				public void actionEnd() {
					System.out.println("Delay Over");
				}
			};
		}

		@Override
		public void runTest() {
			unmanagedAction.restart();
			unmanagedAction.start();
		}
		
		@Override
		public void check1() {
			Gdx.app.log(getClass().getSimpleName(), "hasBeenPooled: " + unmanagedAction.hasBeenPooled());
		}
		
	}
	
}
