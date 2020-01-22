package com.vabrant.actionsystem.test.percentactiontests;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.ActionWatcher;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.actionsystem.test.ActionSystemTestSelector;

public class KillTest extends ActionSystemTestScreen implements ActionListener{

	private final String actionName = "Kill Test";
	private float start;
	private final float duration = 2f;
	private final float killTime = 1f;
	private float percentKilled;
	private final float amount = 50;
	
	public KillTest(ActionSystemTestSelector screen) {
		super(screen);
		createTestObject();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(20);
		testObject.setY(screenCenterY - (testObject.height / 2));
	}
	
	@Override
	public void reset() {
		testObject.setX(20);
	}

	@Override
	public void runTest() {
		start = testObject.getX();
		actionManager.addAction(
				MoveAction.moveXBy(testObject, amount, duration, Interpolation.linear)
					.setName(actionName)
					.watchAction()
					.addListener(this)
				);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		MoveAction action = (MoveAction)ActionWatcher.get(actionName);
		if(action != null) {
			if(action.getCurrentTime() > killTime) {
				percentKilled = action.getPercent(); 
				action.kill();
			}
		}
		
	}
	
	@Override
	public void runCheck1() {
		float end = MathUtils.lerp(start, start + amount, percentKilled);
		log("Current: ", Float.toString(testObject.getX()));
		log("End: ", Float.toString(end));
	}

	@Override
	public void actionStart(Action a) {
	}

	@Override
	public void actionEnd(Action a) {
	}

	@Override
	public void actionKill(Action a) {
		log("Kill Action", "");
	}

	@Override
	public void actionRestart(Action a) {
	}
	
	@Override
	public void actionComplete(Action a) {
	}

}
