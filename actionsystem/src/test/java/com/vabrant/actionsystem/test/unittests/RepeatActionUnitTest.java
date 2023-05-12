package com.vabrant.actionsystem.test.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionManager;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.test.TestUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public class RepeatActionUnitTest {

    private static Application application;

    @BeforeClass
    public static void init() {
        application = new HeadlessApplication(new ApplicationAdapter() {});
    }

    @Test
    public void resetTest() {
        MockActions.MockAction a = MockActions.MockAction.obtain();
        RepeatAction repeat = RepeatAction.repeat(a);
        ActionManager manager = new ActionManager();

        // Start action
        manager.addAction(repeat);

        repeat.end();

        // Remove and pool action
        manager.update(Float.MAX_VALUE);

        assertNull(repeat.getAction());
    }

    @Test
    public void poolTest() {
        MockActions.MockAction a = MockActions.MockAction.obtain();
        RepeatAction repeat = RepeatAction.repeat(a);
        ActionManager manager = new ActionManager();

        // Start action
        manager.addAction(repeat);
        repeat.end();

        // Remove and pool action
        manager.update(Float.MAX_VALUE);

        assertEquals(Boolean.TRUE, TestUtils.executePrivateMethod("hasBeenPooled", Action.class, null, a, null));
    }
}
