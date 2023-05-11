package com.vabrant.actionsystem.platformtests.tests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.actions.ZoomAction;

public class ZoomTest extends DefaultPlatformTest {

    @Override
    public void create() {
        super.create();

        ZoomAction za = ZoomAction.zoomBy(actionable, 1, 2f, Interpolation.smooth2)
                .reverseBackToStart(true);
        addAction(RepeatAction.continuous(GroupAction.sequence(DelayAction.delay(0.5f), za)));
    }
}
