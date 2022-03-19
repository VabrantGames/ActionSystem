package com.vabrant.actionsystem.test.unittests;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.ActionManager;
import com.vabrant.actionsystem.actions.ActionWatcher;
import com.vabrant.actionsystem.test.TestUtils;
import com.vabrant.actionsystem.test.unittests.MockActions.MockAction;
import com.vabrant.actionsystem.test.unittests.MockActions.MockSingleParentAction;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ActionWatcherTest {

    @Rule
    public TestName testName = new TestName();
    private static ActionWatcher watcher;
    private static Application application;
    private static ActionManager actionManager;

    @BeforeClass
    public static void init() {
        application = new HeadlessApplication(new ApplicationAdapter() {
        });
        actionManager = new ActionManager();
        watcher = new ActionWatcher(10);
        watcher.getLogger().setLevel(ActionLogger.DEBUG);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    @Test
    public void basicTest() {
        TestUtils.printTestHeader(testName.getMethodName());

        final String tag = "child";

        //Create parent and child actions
        MockSingleParentAction parent = MockSingleParentAction.obtain();
//		parent.setLogLevel(ActionLogger.DEBUG);
        MockAction child = MockAction.obtain()
                .setName(tag)
                .setLogLevel(ActionLogger.DEBUG)
                .watchAction(watcher);
        child.setCustomUpdateCode(new Runnable() {
            @Override
            public void run() {
                child.end();
            }
        });
        parent.set(child);

        actionManager.addAction(parent);

        //Get action from watcher
        Action<?> action = watcher.get(tag);

        assertFalse(action == null);

        actionManager.update(Integer.MAX_VALUE);

        assertFalse(watcher.contains(tag));
    }

    /**
     * Creates a test action, starts the action then removes the action from the watcher while the action is running.
     */
    @Test
    public void explicitRemoveTest() {
        TestUtils.printTestHeader(testName.getMethodName());

        final String tag = "action";
        MockAction action = MockAction.obtain()
                .setName(tag)
                .setLogLevel(ActionLogger.DEBUG)
                .watchAction(watcher);
        action.setCustomUpdateCode(new Runnable() {
            @Override
            public void run() {
                action.end();
            }
        });

        actionManager.addAction(action);

        assertTrue(watcher.remove(tag));

        actionManager.update(Integer.MAX_VALUE);
    }

    @Test
    public void compareTest() {
        TestUtils.printTestHeader(testName.getMethodName());

        actionManager.getLogger().setLevel(ActionLogger.DEBUG);

        final int amount = 10;
        for (int i = 0; i < amount; i++) {
            MockAction a = MockAction.obtain()
                    .setName(Integer.toString(i))
//                    .setLogLevel(ActionLogger.DEBUG)
                    .watchAction(watcher);
            actionManager.addAction(a);
        }

        for (int i = 0; i < amount; i++) {
            Action<?> a = watcher.get(Integer.toString(i));
            assertFalse(a == null);
            assertTrue(actionManager.getActions().get(i).equals(a));
        }

        actionManager.freeAll();
    }
}
