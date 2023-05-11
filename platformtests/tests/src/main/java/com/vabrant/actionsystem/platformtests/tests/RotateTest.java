package com.vabrant.actionsystem.platformtests.tests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.actions.RotateAction;

public class RotateTest extends DefaultPlatformTest {

    @Override
    public void create() {
        super.create();

        RotateAction ra = RotateAction.rotateBy(actionable, 45, 0.5f, Interpolation.bounceOut)
                .startRotateByFromEnd();

        addAction(RepeatAction.continuous(GroupAction.sequence(DelayAction.delay(0.5f), ra)));
    }
}
