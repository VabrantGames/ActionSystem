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
package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.utils.Array;

/**
 * Runs a group of {@link Action}'s as a {@link #sequence() sequence} or in {@link #parallel() parallel}. 
 * @author John Barton
 */
public class GroupAction extends Action<GroupAction> implements MultiParentAction {
	
	public static GroupAction obtain() {
		return obtain(GroupAction.class);
	}
	
	public static GroupAction parallel(Action<?> a1, Action<?> a2) {
		return obtain()
				.parallel()
				.add(a1)
				.add(a2);
	}
	
	public static GroupAction parallel(Action<?>... a) {
		return obtain()
				.parallel()
				.addAll(a);
	}
	
	public static GroupAction parallel(float startOffset, Action<?> a1, Action<?> a2) {
		return obtain()
				.parallel(startOffset)
				.add(a1)
				.add(a2);
	}
	
	public static GroupAction parallel(float startOffset, Action<?>... a) {
		return obtain()
				.parallel(startOffset)
				.addAll(a);
	}

	public static GroupAction sequence(Action<?> a1, Action<?> a2) {
		return obtain()
				.sequence()
				.add(a1)
				.add(a2);
	}
	
	public static GroupAction sequence(Action<?>... a) {
		return obtain()
				.sequence()
				.addAll(a);
	}

	private boolean parallel = true;
	private float timer;
	private float startOffset;
	private int index;
	private Array<Action<?>> actions;
	
	public GroupAction() {
		actions = new Array<>(4);
	}
	
	public GroupAction parallel() {
		parallel(0);
		return this;
	}
	
	public GroupAction parallel(float startOffset) {
		if(isRunning()) throw new RuntimeException("Group Action can't be changed to parallel while running.");
		this.startOffset = startOffset;
		parallel = true;
		return this;
	}
	
	/**
	 * Runs a group of actions as a sequence.
	 * @return This action for chaining.
	 */
	public GroupAction sequence() {
		if(isRunning()) throw new RuntimeException("Group Action can't be changed to sequence while running.");
		parallel = false;
		return this;
	}
	
	@Override
	public Array<Action<?>> getActions() {
		return actions;
	}
	
	public GroupAction add(Action<?> action) {
		actions.add(action);
		return this;
	}
	
	public GroupAction addAll(Action<?>[] actions) {
		this.actions.addAll(actions);
		return this;
	}
	
	public GroupAction addAll(Array<Action<?>> actions) {
		this.actions.addAll(actions);
		return this;
	}
	
	public GroupAction set(Action<?>[] actions) {
		ActionPools.freeAll(this.actions);
		addAll(actions);
		return this;
	}
	
	public GroupAction set(Array<Action<?>> actions) {
		ActionPools.freeAll(this.actions);
		addAll(actions);
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
	protected void startLogic() {
		index = parallel ? 0 : -1;
		timer =  0;
	}
	
	@Override
	public void endLogic() {
		for(int i = 0, size = actions.size; i < size; i++) {
			actions.get(i).end();
		}
	}
	
	@Override
	public void killLogic() {
		for(int i = 0, size = actions.size; i < size; i++) {
			actions.get(i).kill();
		}
	}
	
	private void updateSequence(float delta) {
		if(actions.size == 0) {
			end();
			return;
		}

		if(index < 0 || !actions.get(index).update(delta)) {
			if(++index == actions.size) {
				end();
			}
			else {
				actions.get(index).start();
			}
		}
	}
	
	private void updateParallel(float delta) {
		boolean finished = true;
		
		timer += delta;

		for(int i = 0, size = actions.size; i < size; i++) {
			
			//update the start offset
			float time = timer - (startOffset * i);
			if(time < 0) {
				finished = false;
				continue;
			}
			
			//Check if the action is still running
			if(i == index) {
				actions.get(index++).start();
				finished = false;
			}
			else {
				if(actions.get(i).update(delta)) finished = false;
			}
		}
		if(finished) end();
	}
	
	@Override
	public boolean update(float delta) {
		if(isDead() || !isRunning()) return false;
		if(isPaused()) return true;
		
		if(parallel) {
			updateParallel(delta);
		}
		else {
			updateSequence(delta);
		}
		return isRunning();
	}
	
	@Override
	public void restartLogic() {
		index = parallel ? 0 : -1;
		timer = 0;
	}
	
	@Override
	public boolean hasConflict(Action<GroupAction> action) {
		if(action instanceof GroupAction) {
			GroupAction conflictAction = (GroupAction)action;
			if(getName() == null || conflictAction.getName() == null) return false;
			if(getName().equals(conflictAction.getName())) return true;
		}
		return false;
	}
	
	@Override
	public void reset() {
		super.reset();
		parallel = true;
		startOffset = 0;
		index = 0;
		timer = 0;
	}

}
