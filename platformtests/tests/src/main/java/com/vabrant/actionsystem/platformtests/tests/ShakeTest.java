package com.vabrant.actionsystem.platformtests.tests;

import com.vabrant.actionsystem.actions.*;

public class ShakeTest extends DefaultPlatformTest {

    @Override
    public void create() {
        super.create();

        ShakeAction.GDQShakeLogic logic = new ShakeAction.GDQShakeLogic(0.5f);
        ShakeAction.GDQShakeLogic.GDQShakeLogicData data = new ShakeAction.GDQShakeLogic.GDQShakeLogicData();
        addAction(RepeatAction.continuous(GroupAction.sequence(DelayAction.delay(0.5f), RunnableAction.runnable(new Runnable() {
            @Override
            public void run() {
                ShakeAction sa = ShakeAction.shake(actionable, new ShakeAction.GDQShakeLogic(0.5f), new ShakeAction.GDQShakeLogic.GDQShakeLogicData(), 50, 50, 20, 1f, null);

            }
        }))));
    }
}
