
package com.vabrant.actionsystem.platformtests.tests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.actions.RepeatAction;

public class MoveTest extends DefaultPlatformTest {

	@Override
	public void create () {
		super.create();

		MoveAction ma = MoveAction.moveYBy(actionable, 50, 1f, Interpolation.exp5Out).reverseBackToStart(true);
		addAction(RepeatAction.continuous(GroupAction.sequence(DelayAction.delay(0.5f), ma)));
	}
}
