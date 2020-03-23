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
package com.vabrant.actionsystem.test.tests;

import com.badlogic.gdx.utils.Array;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.actions.MultiParentAction;
import com.vabrant.actionsystem.actions.SingleParentAction;

/**
 * @author John Barton
 *
 */
public class TestActions {
	
	public static class TestAction extends Action<TestAction> {
		
		public static TestAction obtain() {
			return obtain(TestAction.class);
		}
		
		@Override
		protected void startLogic() {
			logger.debug("Run Start Logic");
		}
		
		@Override
		protected void restartLogic() {
			logger.debug("Run Restart Logic");
		}
		
		@Override
		protected void endLogic() {
			logger.debug("Run End Logic");
		}
		
		@Override
		protected void killLogic() {
			logger.debug("Run Kill Logic");
		}
	}
	
	public static class SingleParentTestAction extends Action<SingleParentTestAction> implements SingleParentAction {

		public static SingleParentTestAction obtain() {
			return obtain(SingleParentTestAction.class);
		}
		
		private Action<?> action;
		
		public SingleParentTestAction set(Action<?> action) {
			this.action = action;
			return this;
		}
		
		@Override
		public Action<?> getAction() {
			return action;
		}

		@Override
		public void reset() {
			super.reset();
			action = null;
		}
		
	}
	
	public static class MultiParentTestAction extends Action<MultiParentTestAction> implements MultiParentAction {

		public static MultiParentTestAction obtain() {
			return obtain(MultiParentTestAction.class);
		}
		
		private Array<Action<?>> actions = new Array<>();
		
		public MultiParentTestAction add(Action<?> action) {
			actions.add(action);
			return this;
		}
		
		@Override
		public Array<Action<?>> getActions() {
			return actions;
		}
		
		@Override
		public void reset() {
			super.reset();
			System.out.println("Pool Children");
		}
		
	}
	
	public static class AlternativeSingleParentAction extends Action<AlternativeSingleParentAction> {

		public static AlternativeSingleParentAction obtain(){
			return obtain(AlternativeSingleParentAction.class);
		}

		private Action<?> action;

		public AlternativeSingleParentAction set(Action<?> action) {
			this.action = action;
			return this;
		}

		public Action<?> getAction(){
			return action;
		}

		@Override
		public void reset() {
			super.reset();
			ActionPools.alternativeFree(action);
			action = null;
		}
	}
	
	public static class AlternativeMultiParentAction extends Action<AlternativeMultiParentAction> {

		private Array<Action<?>> actions = new Array<>();
		
		public AlternativeMultiParentAction add(Action<?> action) {
			actions.add(action);
			return this;
		}
		
		public Array<Action<?>> getActions(){
			return actions;
		}

		@Override
		public void reset() {
			super.reset();
			ActionPools.freeAll(actions);
			actions.clear();
		}
	}

}
