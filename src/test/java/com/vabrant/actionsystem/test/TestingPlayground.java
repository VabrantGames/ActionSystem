package com.vabrant.actionsystem.test;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.actions.ColorAction;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.testbase.TestSelectScreen;

public class TestingPlayground extends ActionSystemTestScreen{
	
	public TestingPlayground(TestSelectScreen app) {
		super(app);
		createTestObject();
		testObject.setX(100);
		testObject.setY(100);
		testObject.setSize(100, 40);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(screenCenterX - (testObject.width / 2));
		testObject.setY(screenCenterY - (testObject.height / 2));
	}
	
	@Override
	public void runTest() {
		
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.NUMPAD_1:
				singleMoveTest();
				break;
			case Keys.NUMPAD_2:
				repeatTest();
				break;
		}
		return super.keyDown(keycode);
	}
	
	int x = 100;
	boolean first = true;
	public Runnable getRandomizeXRunnable() {
		return new Runnable() {
			@Override
			public void run() {
				if(first) {
					first = false;
//					testObject.setX(100);
				}
				else {
//					testObject.setX(400);
				}
//				testObject.setX(MathUtils.random(50, 400));
//				testObject.setX(x += 50);
			}
		};
	}
	
	public void singleMoveTest() {
		actionManager.addAction(MoveAction.moveXBy(testObject, 50, 0.5f, Interpolation.linear));
	}
	
	public void repeatTest() {
		
		DelayAction delay = ActionPools.obtain(DelayAction.class);
		delay.set(0.25f);
		
		GroupAction group = ActionPools.obtain(GroupAction.class);
		group.sequence();
		group.add(MoveAction.moveXBy(testObject, 50, 0.5f, Interpolation.linear).restartMoveXByFromEnd());
		group.add(delay);
		
		RepeatAction repeat = ActionPools.obtain(RepeatAction.class);
		repeat.set(group);
		repeat.setAmount(2);
		
		actionManager.addAction(repeat);
	}

}
