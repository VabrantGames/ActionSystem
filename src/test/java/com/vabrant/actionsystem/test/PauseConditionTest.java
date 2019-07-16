package com.vabrant.actionsystem.test;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.GroupAction;
import com.vabrant.actionsystem.MoveAction;
import com.vabrant.actionsystem.PauseCondition;
import com.vabrant.actionsystem.Pools;
import com.vabrant.actionsystem.RepeatAction;
import com.vabrant.actionsystem.RunnableAction;

public class PauseConditionTest extends ActionSystemTestScreen implements PauseCondition{
	
	private boolean isGamePaused;
	private boolean isCharacterStunned;
	private ActionSystemTestObject testObject;
	
	public PauseConditionTest() {
		debug = true;
		testObject = new ActionSystemTestObject();
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
		MoveAction moveAction = Pools.obtain(MoveAction.class);
//		moveAction.moveXTo(testObject, 0, 100);
		moveAction.set(0.5f, false, Interpolation.linear);
		
		RepeatAction repeatMoveAction = Pools.obtain(RepeatAction.class);
		repeatMoveAction.set(moveAction);
		repeatMoveAction.setRepeatAmount(2);
		
		RunnableAction pauseRunnable = Pools.obtain(RunnableAction.class);
		pauseRunnable.set(new Runnable() {
			@Override
			public void run() {
				actionManager.pauseAllActions();
			}
		});
		
		MoveAction lastMove = Pools.obtain(MoveAction.class);
		lastMove.moveYTo(testObject, 150);
		lastMove.set(0.5f, true, Interpolation.linear);
		
		GroupAction group = Pools.obtain(GroupAction.class);
		group.setPauseCondition(this);
		group.sequence();
		group.add(repeatMoveAction);
		group.add(pauseRunnable);
		group.add(lastMove);
		
		actionManager.addAction(group);
	}

}
