package com.vabrant.actionsystem.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.testbase.Test;
import com.vabrant.testbase.ActionSystemTestSelector;

public class ActionPoolsTestScreenOld extends ActionSystemTestScreen {
	
	private Class<?>[] tests = {
			FillTest.class
			};
	
	public ActionPoolsTestScreenOld(ActionSystemTestSelector screen) {
		super(screen);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
//		ActionPools.logger.setLevel(Logger.DEBUG);
	}
	
	@Override
	public void runTest() {
		
	}
	
	public class FillTest implements Test{
		 
		@Override
		public void runTest1() {
			ActionPools.fill(MoveAction.class, 50);
		}
		
		@Override
		public void check1() {
			Gdx.app.log(this.getClass().getSimpleName(), "Amount: " + ActionPools.get(MoveAction.class).getFree());
		}
	}

}
