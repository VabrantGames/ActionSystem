package com.vabrant.actionsystem.test;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.actions.PauseCondition;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.actions.RunnableAction;
import com.vabrant.testbase.TestSelectScreen;

public class PauseConditionTestOld extends ActionSystemTestScreen implements PauseCondition{
	
	private boolean isGamePaused;
	private boolean isCharacterStunned;
	private ActionSystemTestObject testObject;
	
	public PauseConditionTestOld(TestSelectScreen screen) {
		super(screen);
		
		debug = true;
		testObject = new ActionSystemTestObject();
	}
	
	@Override
	public void runTest() {
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.NUM_1) {
			isGamePaused = true;
		}
		return false;
	}
	
	//Scenario
	//If the game is paused all the actions are paused to display the pause menu.
	//If the game is not paused and the character is stunned
	@Override
	public boolean shouldPause() {
		boolean shouldPause = true;
		if(isGamePaused) {
			shouldPause = true;
		}
		else {
			if(isCharacterStunned) {
				shouldPause = false;
			}
		}
		return shouldPause;
	}
	
	@Override
	public boolean shouldResume() {
		boolean shouldResume = true;
		if(isCharacterStunned) {
			shouldResume = false;
		}
		else {
			shouldResume = true;
		}
		return shouldResume;
	}
	
	@Override
	public void debug(ShapeRenderer renderer) {
		renderer.set(ShapeType.Filled);
		renderer.setColor(Color.RED);
		renderer.rect(testObject.getX(), testObject.getY(), testObject.width, testObject.height);
	}
	
	private void playAnimation() {
//		MoveAction moveAction = ActionPools.obtain(MoveAction.class);
//		moveAction.moveXTo(testObject, 0, 100);
//		moveAction.set(0.5f, false, Interpolation.linear);
//		
//		RepeatAction repeatMoveAction = ActionPools.obtain(RepeatAction.class);
//		repeatMoveAction.set(moveAction);
//		repeatMoveAction.setAmount(2);
//		
//		RunnableAction pauseRunnable = ActionPools.obtain(RunnableAction.class);
//		pauseRunnable.set(new Runnable() {
//			@Override
//			public void run() {
//				actionManager.pauseAllActions();
//			}
//		});
//		
//		MoveAction lastMove = ActionPools.obtain(MoveAction.class);
//		lastMove.moveYTo(150);
//		lastMove.set(testObject, 0.5f, true, Interpolation.linear);
//		
//		GroupAction group = ActionPools.obtain(GroupAction.class);
//		group.setPauseCondition(this);
//		group.sequence();
//		group.add(repeatMoveAction);
//		group.add(pauseRunnable);
//		group.add(lastMove);
//		
//		actionManager.addAction(group);
	}

}
