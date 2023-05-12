package com.vabrant.actionsystem.test.unittests;

import static org.junit.Assert.*;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionManager;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.test.TestUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public class GroupActionUnitTest {

    private static Application application;

    @BeforeClass
    public static void init() {
        application = new HeadlessApplication(new ApplicationAdapter() {});
    }

    @Test
    public void resetTest() {
        MockActions.MockAction m1 = MockActions.MockAction.obtain();
        MockActions.MockAction m2 = MockActions.MockAction.obtain();
        ActionManager manager = new ActionManager();
        GroupAction group = GroupAction.obtain().add(m1).add(m2);

        // Start and add the action
        manager.addAction(group);

        // End the action
        group.end();

        // Remove and pool the action
        manager.update(Float.MAX_VALUE);

        assertEquals(0, group.getActions().size);
    }

    @Test
    public void poolTest() {
        MockActions.MockAction m1 = MockActions.MockAction.obtain();
        MockActions.MockAction m2 = MockActions.MockAction.obtain();
        ActionManager manager = new ActionManager();
        GroupAction group = GroupAction.obtain().add(m1).add(m2);

        // Start and adds the action
        manager.addAction(group);

        // Ends the action
        group.end();

        // Remove and pool the action
        manager.update(Float.MAX_VALUE);

        assertEquals(Boolean.TRUE, TestUtils.executePrivateMethod("hasBeenPooled", Action.class, null, m1, null));
        assertEquals(Boolean.TRUE, TestUtils.executePrivateMethod("hasBeenPooled", Action.class, null, m2, null));
    }
}
