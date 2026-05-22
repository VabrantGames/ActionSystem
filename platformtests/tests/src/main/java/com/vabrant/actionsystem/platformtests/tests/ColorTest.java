
package com.vabrant.actionsystem.platformtests.tests;

import com.badlogic.gdx.graphics.Color;
import com.vabrant.actionsystem.actions.*;
import com.vabrant.actionsystem.actions.coloraction.ColorAction;
import com.vabrant.actionsystem.actions.coloraction.RGBColorLogic;

public class ColorTest extends DefaultPlatformTest {

	@Override
	public void create () {
		super.create();

		ColorAction ca = RGBColorLogic.changeColor(actionable, Color.CYAN, 1, null).reverseBackToStart(true);
		addAction(RepeatAction.continuous(GroupAction.sequence(DelayAction.delay(0.5f), ca)));
	}
}
