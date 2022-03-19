/**
 *	Copyright 2020 See AUTHORS file.
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package com.vabrant.actionsystem.test.unittests;

import com.badlogic.gdx.utils.Array;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.actions.MultiParentAction;
import com.vabrant.actionsystem.actions.SingleParentAction;

/**
 * @author John Barton
 *
 */
public class MockActions {
	
	public static class MockAction extends Action<MockAction> {

		private Runnable runnable;
		
		public static MockAction obtain() {
			return obtain(MockAction.class);
		}

		public MockAction setCustomUpdateCode(Runnable runnable) {
			this.runnable = runnable;
			return this;
		}
		
//		@Override
//		protected void startLogic() {
//			logger.debug("Run Start Logic");
//		}
//
//		@Override
//		protected void restartLogic() {
//			logger.debug("Run Restart Logic");
//		}
//
//		@Override
//		protected void endLogic() {
//			logger.debug("Run End Logic");
//		}
//
//		@Override
//		protected void killLogic() {
//			logger.debug("Run Kill Logic");
//		}
		
		@Override
		public boolean update(float delta) {
			if(!isRunning()) return false;
			if (runnable != null) runnable.run();
			return isRunning();
		}

		@Override
		public void reset() {
			super.reset();
			runnable = null;
		}
	}
	
	public static class MockSingleParentAction extends Action<MockSingleParentAction> implements SingleParentAction {

		public static MockSingleParentAction obtain() {
			return obtain(MockSingleParentAction.class);
		}

		private Action<?> action;

		public MockSingleParentAction set(Action<?> action) {
			this.action = action;
			return this;
		}

		@Override
		public void setRootAction(Action<?> root) {
			super.setRootAction(root);
			if(action != null) action.setRootAction(root);
		}

		@Override
		public Action<?> getAction() {
			return action;
		}

		@Override
		protected void startLogic() {
			super.startLogic();
			if (action != null) action.start();
		}

		@Override
		protected void restartLogic() {
			super.restartLogic();
			if (action != null) action.restart();
		}

		@Override
		protected void killLogic() {
			super.killLogic();
			if (action != null) action.kill();
		}

		@Override
		protected void endLogic() {
			super.endLogic();
			if (action != null) action.end();
		}

		@Override
		public void reset() {
			super.reset();
			action = null;
		}

		@Override
		public boolean update(float delta) {
			if(!isRunning()) return false;
			if (action != null) {
				if (!action.update(delta)) {
					end();
				}
			}
			return isRunning();
		}

	}
	
	public static class MockMultiParentAction extends Action<MockMultiParentAction> implements MultiParentAction {

		public static MockMultiParentAction obtain() {
			return obtain(MockMultiParentAction.class);
		}
		
		private Array<Action<?>> actions = new Array<>();
		
		public MockMultiParentAction add(Action<?> action) {
			actions.add(action);
			return this;
		}
		
		@Override
		public void setRootAction(Action<?> root) {
			super.setRootAction(root);
			for(int i = 0; i < actions.size; i++) {
				actions.get(i).setRootAction(root);
			}
		}
		
		@Override
		public Array<Action<?>> getActions() {
			return actions;
		}

		@Override
		protected void endLogic() {
			super.endLogic();

			for (int i = 0; i < actions.size; i++) {
				actions.get(i).end();
			}
		}

		@Override
		public boolean update(float delta) {
			if(!isRunning()) return false;
			return true;
		}
		
	}
	
//	public static class AlternativeSingleParentAction extends Action<AlternativeSingleParentAction> {
//
//		public static AlternativeSingleParentAction obtain(){
//			return obtain(AlternativeSingleParentAction.class);
//		}
//
//		private Action<?> action;
//
//		public AlternativeSingleParentAction set(Action<?> action) {
//			this.action = action;
//			return this;
//		}
//
//		public Action<?> getAction(){
//			return action;
//		}
//
//		@Override
//		public void reset() {
//			super.reset();
////			ActionPools.alternativeFree(action);
//			action = null;
//		}
//	}
//
//	public static class AlternativeMultiParentAction extends Action<AlternativeMultiParentAction> {
//
//		private Array<Action<?>> actions = new Array<>();
//
//		public AlternativeMultiParentAction add(Action<?> action) {
//			actions.add(action);
//			return this;
//		}
//
//		public Array<Action<?>> getActions(){
//			return actions;
//		}
//
//		@Override
//		public void reset() {
//			super.reset();
//			ActionPools.freeAll(actions);
//			actions.clear();
//		}
//	}

}
