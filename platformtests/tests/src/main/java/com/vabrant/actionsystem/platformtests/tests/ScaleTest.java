
package com.vabrant.actionsystem.platformtests.tests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.actions.ScaleAction;

public class ScaleTest extends DefaultPlatformTest {

	@Override
	public void create () {
		super.create();

		ScaleAction sa = ScaleAction.scaleBy(actionable, 1, 1, 1, Interpolation.circleOut).reverseBackToStart(true);
		addAction(RepeatAction.continuous(GroupAction.sequence(DelayAction.delay(0.5f), sa)));
	}
}
