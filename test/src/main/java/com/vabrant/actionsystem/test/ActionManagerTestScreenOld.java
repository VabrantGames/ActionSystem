package com.vabrant.actionsystem.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Logger;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionManager;
import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.actions.RotateAction;
import com.vabrant.actionsystem.actions.ZoomAction;
import com.vabrant.testbase.Test;
import com.vabrant.testbase.TestSelectScreen;

public class ActionManagerTestScreenOld extends ActionSystemTestScreen {

	private final Class<?>[] tests = {
			DisposeTest.class
			};
	ActionSystemTestObject testObject;
	
	public ActionManagerTestScreenOld(TestSelectScreen screen) {
		super(screen);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
//		ActionPools.logger.setLevel(Logger.DEBUG);
		testObject = new ActionSystemTestObject();
	}
	
	@Override
	public void runTest() {
		
	}
	
	public class DisposeTest implements Test{
		
		private final String unmanagedRunningActionName = "unmanagedRunningAction";
		private final String unmanagedNonRunningActionName = "unmanagedNonRunningAction";
		private final String managedRunningActionName = "managedAction";
		TestClass testClass;
		
		@Override
		public void runTest1() {
			testClass = new TestClass();
		}
		
		@Override
		public void check1() {
			if(testClass == null) return;
//			Action unmanagedRunningAction = testClass.actionManager.getActionByName(unmanagedRunningActionName);
//			Action unmanagedNonRunningAction = testClass.actionManager.getActionByName(unmanagedNonRunningActionName);
//			Action managedRunningAction = testClass.actionManager.getActionByName(managedRunningActionName);
//			printRunning(unmanagedRunningAction);
//			printRunning(unmanagedNonRunningAction);
//			printRunning(managedRunningAction);
		}
		
		@Override
		public void runTest2() {
			testClass.actionManager.freeAll();
			testClass = null;
		}
		
		@Override
		public void check2() {
			if(testClass == null) return;
			for(int i = 0; i < testClass.actionManager.getActions().size; i++) {
				Gdx.app.log(this.getClass().getSimpleName(), "Normal Action");
			}
			
			for(int i = 0; i < testClass.actionManager.getUnmanagedActions().size; i++) {
				Gdx.app.log(this.getClass().getSimpleName(), "Unmanaged Action");
			}
		}
		
		void printRunning(Action action) {
			if(action != null) {
				Gdx.app.log(this.getClass().getSimpleName(), action.getName() + "-isRunning: " + action.isRunning());
			}
			else {
				Gdx.app.log(this.getClass().getSimpleName(), "Action is not running");
			}
		}
		
		public class TestClass{
			
			ZoomAction unmanagedRunningAction;
			MoveAction unmanagedNonRunningAction;
			ActionManager actionManager;
			
			public TestClass() {
				actionManager = new ActionManager();

				//normal running action
				RotateAction runningAction = RotateAction.rotateBy(testObject, 45f, 0.5f, Interpolation.linear);
				RepeatAction managedContinuousAction = RepeatAction.continuous(runningAction);
				managedContinuousAction.setName(managedRunningActionName);
				
				//unmanagedAction that is not running
				unmanagedNonRunningAction = MoveAction.moveXBy(testObject, 5, 0.5f, Interpolation.linear);
				unmanagedNonRunningAction.unmanage();
				unmanagedNonRunningAction.setName(unmanagedNonRunningActionName);
//				actionManager.addUnmanagedAction(unmanagedNonRunningAction);
				
				//unmanagedAction that is running
				unmanagedRunningAction = ZoomAction.zoomTo(testObject, 1.5f, 0.5f, Interpolation.linear);
				unmanagedRunningAction.unmanage();
//				actionManager.addUnmanagedAction(unmanagedRunningAction);
				RepeatAction unmanagedContinuousAction = RepeatAction.continuous(unmanagedRunningAction);
				unmanagedContinuousAction.setName(unmanagedRunningActionName);
				
				//start continuous actions
				actionManager.addAction(managedContinuousAction);
				actionManager.addAction(unmanagedContinuousAction);
				
				actionManager.update(Gdx.graphics.getDeltaTime());
			}
		}
	}
}
