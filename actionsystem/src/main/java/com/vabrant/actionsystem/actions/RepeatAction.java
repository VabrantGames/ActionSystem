/**
 *	Copyright 2019 John Barton
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */

package com.vabrant.actionsystem.actions;

/** Repeats an {@link Action} a specified number of times or indefinitely.
 * @author John Barton */
public class RepeatAction extends Action<RepeatAction> {

	public static RepeatAction obtain () {
		return obtain(RepeatAction.class);
	}

	/** Repeats an action one time.
	 * @param repeatAction Action to repeat.
	 * @return An action that runs once. */
	public static RepeatAction repeat (Action<?> repeatAction) {
		return obtain().set(repeatAction).setAmount(1);
	}

	/** Repeats an action a specified number of times.
	 * @param repeatAction Action to repeat.
	 * @param amount Times to repeat.
	 * @return An action that runs a specified number of times. */
	public static RepeatAction repeat (Action<?> repeatAction, int amount) {
		return obtain().set(repeatAction).setAmount(amount);
	}

	/** Repeats an action indefinitely.
	 * @param repeatAction Action to repeat.
	 * @return An action that runs indefinitely. */
	public static RepeatAction continuous (Action<?> repeatAction) {
		return obtain().set(repeatAction).setContinuous(true);
	}

	private boolean originalReverseStateSet;
	private boolean originalReverseState;
	private boolean pingPong;
	private boolean reverseState;

	// How many times the action has been played
	private int count = 0;

	// How many times to play the action. Includes the initial non repeat play.
	private int amount = 0;
	private boolean isContinuous;
	private Action<?> action;
	private Reversible<?> reversible;

	/** @param continuous
	 * @return This action for chaining. */
	public RepeatAction setContinuous (boolean continuous) {
		count = 0;
		amount = 0;
		isContinuous = continuous;
		return this;
	}

	/** Times to repeat.
	 * @return This action for chaining. */
	public RepeatAction setAmount (int amount) {
		// times to repeat + initial play
		this.amount = amount + 1;
		return this;
	}

	public RepeatAction set (Action<?> action) {
		// Can't change actions while the action is running
		if (isRunning()) return this;

		if (action == null) throw new IllegalArgumentException("Action is null.");

		// Check if we are holding an action. If so pool it.
		if (this.action != null) {
			ActionPools.free(action);
			action = null;
		}

		this.action = action;
		if (action instanceof Reversible) reversible = (Reversible<?>)action;
		return this;
	}

	public int getCount () {
		return count <= 0 ? 0 : count - 1;
	}

	public boolean isContinuous () {
		return isContinuous;
	}

	public Action<?> getAction () {
		return action;
	}

	public RepeatAction pingPong (boolean pingPong) {
		if (reversible != null) {
			this.pingPong = pingPong;

			// Restore the original reverse state
			if (!pingPong && originalReverseStateSet) {
				originalReverseStateSet = false;
				reversible.setReverse(originalReverseState);
			}
		}
		return this;
	}

	@Override
	public void setRootAction (Action<?> root) {
		super.setRootAction(root);
		if (action != null) action.setRootAction(root);
	}

	@Override
	protected void startLogic () {
		count = 0;

		if (pingPong) {
			if (!originalReverseStateSet) {
				originalReverseStateSet = true;
				originalReverseState = reversible.isReversed();
			} else {
				reversible.setReverse(originalReverseState);
			}

			reverseState = isContinuous != originalReverseState;
		}
	}

	@Override
	protected void endLogic () {
		if (action != null) action.end();

		// Reset the reverse state
		if (pingPong) {
			reversible.setReverse(originalReverseState);
			originalReverseStateSet = false;
		}
	}

	@Override
	protected void killLogic () {
		if (action != null) action.kill();
	}

	@Override
	public void updateLogic (float delta) {
		if (!action.update(delta)) {
			if (isContinuous || count < amount) {
				if (!isContinuous) count++;
				if (logger != null && !isContinuous) logger.debug("Repeat", Integer.toString(getCount()));
				if (pingPong && count > 1 || pingPong && isContinuous) reversible.setReverse(reverseState = !reverseState);
				action.start();
			} else {
				end();
			}
		}
	}

	@Override
	public void clear () {
		super.clear();
		pingPong = false;
		reverseState = false;
		amount = 0;
		count = 0;
		isContinuous = false;
		originalReverseStateSet = false;
		originalReverseState = false;
	}

	@Override
	public void reset () {
		super.reset();
		if (action != null) {
			ActionPools.free(action);
			action = null;
		}
		reversible = null;
	}
}
