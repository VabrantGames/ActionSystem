package com.vabrant.actionsystem.test.performancetests;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.actionsystem.test.ActionSystemTestSelector;

public class ActionUpdateTest extends ActionSystemTestScreen {
	
	private final int maxActions = 5000;
	private final PerformanceTimer timer = new PerformanceTimer(2);
	private final TestActionManager testActionManager = new TestActionManager();
	
	public ActionUpdateTest(ActionSystemTestSelector screen) {
		super(screen);
		
		Pool<TestAction> pool = new Pool<TestAction>(maxActions) {
			@Override
			protected TestAction newObject() {
				return new TestAction();
			}
		};
		
		Pool<TestGroupAction> pool2 = new Pool<TestGroupAction>(5) {
			@Override
			protected TestGroupAction newObject() {
				return new TestGroupAction();
			}
		};
		
		for(int i = 0; i < maxActions; i++) {
			pool.free(pool.obtain());
		}
		
		for(int i = 0; i < 5; i++) {
			pool2.free(pool2.obtain());
		}
		
		ActionPools.add(TestAction.class, pool);
		ActionPools.add(TestGroupAction.class, pool2);
	}

	@Override
	public void runTest() {
		timer.clear();
		startOldTest();
//		newTest();
	}
	
	private void startOldTest() {
		timer.start();
		testActionManager.oldManager();
		for(int i = 0; i < 5; i++) {
			TestGroupAction group = ActionPools.obtain(TestGroupAction.class);
			for(int j = 0; j < 1000; j++) {
				group.add(ActionPools.obtain(TestAction.class).setDuration(2));
			}
			testActionManager.add(group);
		}
	}
	
	private void endOldTest() {
		timer.end();
		System.out.println(timer.toString());
	}
	
	private void startNewTest() {
		timer.start();
		testActionManager.newManager();
		
		for(int i = 0; i < 5; i++) {
			TestGroupAction group = ActionPools.obtain(TestGroupAction.class);
			for(int j = 0; j < 1000; j++) {
				group.add(ActionPools.obtain(TestAction.class).setDuration(2));
			}
			testActionManager.add(group);
		}
	}
	
	private void endNewTest() {
		timer.end();
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		testActionManager.update(delta);
	}
	
	private class TestGroupAction extends Action<TestGroupAction> {
		
		private int index;
		private Array<Action> actions = new Array<>();
		
		public void add(Action action) {
			actions.add(action);
		}
		
		@Override
		public void DEBUG_setRootAction(Action root) {
			super.DEBUG_setRootAction(root);
			for(int i = 0; i < actions.size; i++) {
				actions.get(i).DEBUG_setRootAction(root);
			}
		}
		
		public boolean updateOld(float delta) {
			if(isFinished) return true;
			if(isPaused) return false;
			if(!isRunning) start();
			
			boolean finished = true;
			for(int i = 0; i < actions.size; i++) {
				TestAction action = (TestAction)actions.get(i);
				if(!action.updateOld(delta)) {
					finished = false;
				}
			}
			
			if(finished) end();
			
			return isFinished;
		}
		
//		@Override
//		public TestGroupAction start() {
//			super.start();
//			index = 0;
//			return this;
//		}
		
		boolean over = true;
		public Action updateNew(float delta) {
			if(isFinished) return null;
			if(isPaused) return null;
			if(!isRunning) start();

//			boolean finished = true;
			Action a = null;
			if(index < actions.size) {
				a = actions.get(index);
				if(!a.isFinished()) over = false;
			}
			else {
				index = 0;
			}
			
			index++;
			
			return a;
		}
		
		@Override
		public void reset() {
			for(int i = actions.size - 1; i >= 0; i--) {
				ActionPools.free(actions.pop());
			}
			index = 0;
		}
	}

	private class TestAction extends Action<TestAction> {
		
		private float timer;
		private float duration;
		
		public TestAction setDuration(float duration) {
			this.duration = duration;
			return this;
		}
		
		public boolean updateOld(float delta) {
			if(isFinished) return true;
			if(isPaused) return false;
			if(!isRunning) start();
			if((timer += delta) >= duration) {
				end();
			}
			return isFinished;
		}
		
		public Action updateNew(float delta) {
			if(isFinished) return null;
			if(isPaused) return null;
			if(!isRunning) start();
			if((timer += delta) >= duration) {
				end();
			}
			return null;
		}
		
		@Override
		public void reset() {
			timer = 0;
			duration = 0;
		}
	}
	
	private class TestActionManager {
		
		private boolean dontUpdate;
		private boolean isOld;
		private final Array<Action> actions;
		
		public TestActionManager() {
			actions = new Array<>();
		}
		
		public void dontUpdate() {
			dontUpdate = true;
		}
		
		public void oldManager() {
			isOld = true;
			dontUpdate = false;
		}
		
		public void newManager() {
			isOld = false;
			dontUpdate = false;
		}
		
		public void add(Action action) {
			action.DEBUG_setRoot();
			action.DEBUG_setRootAction(action);
			actions.add(action);
		}

		public void update(float delta) {
			if(dontUpdate) return;
			
			if(isOld) {
				updateOld(delta);
			}
			else {
				updateNew(delta);
			}
		}
		
		public void updateOld(float delta) {
			for(int i = actions.size - 1; i >= 0; i--) {
				TestGroupAction action = (TestGroupAction)actions.get(i);
				if(action.updateOld(delta)) {
					ActionPools.free(actions.removeIndex(i));
				}
			}
			
			if(actions.size == 0) {
				endOldTest();
				dontUpdate();
//				startNewTest();
			}
		}
		
		public void updateNew(float delta) {
			
		}
		
	}
	
}
