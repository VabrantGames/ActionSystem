package com.vabrant.actionsystem.test;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.ActionManager;
import com.vabrant.actionsystem.ActionPools;
import com.vabrant.actionsystem.DelayAction;
import com.vabrant.actionsystem.GroupAction;
import com.vabrant.actionsystem.MoveAction;
import com.vabrant.actionsystem.RepeatAction;
import com.vabrant.testbase.BaseScreen;
import com.vabrant.testbase.TestApplication;

public class TestingPlayground extends BaseScreen{
	
	private ActionManager actionManager;
	private ActionSystemTestObject testObject;
	
	public TestingPlayground(TestApplication app) {
		super(app);
		
		debug = true;
		actionManager = new ActionManager(20);
		testObject = new ActionSystemTestObject();
		testObject.setX(100);
		testObject.setY(100);
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
		actionManager.addAction(MoveAction.moveXBy(testObject, 50, 0.5f, false, Interpolation.linear));
	}
	
	public void repeatTest() {
		
		DelayAction delay = ActionPools.obtain(DelayAction.class);
		delay.set(0.25f);
		
		GroupAction group = ActionPools.obtain(GroupAction.class);
		group.sequence();
		group.add(MoveAction.moveXBy(testObject, 50, 0.5f, false, Interpolation.linear).restartMoveXByFromEnd());
		group.add(delay);
		
		RepeatAction repeat = ActionPools.obtain(RepeatAction.class);
		repeat.set(group);
		repeat.setAmount(2);
		
		actionManager.addAction(repeat);
	}
	
	public void actions() {
//		MoveAction first = Pools.obtain(MoveAction.class);
//		first.moveYBy(testObject, 50, true);
//		first.set(0.5f, false, Interpolation.linear);
//		
//		MoveAction second = Pools.obtain(MoveAction.class);
//		second.moveXBy(testObject, 50, true);
//		second.set(0.5f, false, Interpolation.linear);
//		
//		MoveAction third = Pools.obtain(MoveAction.class);
//		third.moveYBy(testObject, 50, true);
//		third.set(0.5f, false, Interpolation.linear);
//		
//		GroupAction group = Pools.obtain(GroupAction.class);
//		group.sequence();
//		group.add(first);
//		group.add(second);
//		group.add(third);
//		
//		RepeatAction repeat = Pools.obtain(RepeatAction.class);
//		repeat.set(group);
//		repeat.setRepeatAmount(1);
//		
//		actionsController.addAction(repeat);
		
	}

	@Override
	public void debug(ShapeRenderer renderer) {
		renderer.set(ShapeType.Filled);
		renderer.setColor(Color.BLACK);
		renderer.rect(testObject.getX(), testObject.getY(), 10, 10);
		
		renderer.rect(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 20, 20);
	}
	
}
