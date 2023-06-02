
package com.vabrant.actionsystem.platformtests.tests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.*;

public class ShakeTest extends DefaultPlatformTest {

	@Override
	public void create () {
		super.create();

		addAction(RepeatAction.continuous(GroupAction.sequence(DelayAction.delay(0.5f),
			ShakeAction.shake(actionable, null, 4, 4, 2, 1, Interpolation.smooth2).setUsePercent(true))));
	}
}
