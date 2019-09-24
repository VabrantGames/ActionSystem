package com.vabrant.actionsystem.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Logger;
import com.vabrant.actionsystem.ActionPools;
import com.vabrant.actionsystem.MoveAction;
import com.vabrant.testbase.Test;
import com.vabrant.testbase.TestSelectScreen;

public class ActionPoolsTestScreenOld extends ActionSystemBaseTestScreen {
	
	private Class<?>[] tests = {
			FillTest.class
			};
	
	public ActionPoolsTestScreenOld(TestSelectScreen screen) {
		super(screen);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		ActionPools.logger.setLevel(Logger.DEBUG);
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
