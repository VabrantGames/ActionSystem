package com.vabrant.actionsystem.test.unittests;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.vabrant.actionsystem.logger.ActionLogger;
import com.vabrant.actionsystem.logger.SingleSoloStrategy;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static junit.framework.TestCase.*;

public class SingleSoloStrategyTest {

    private static Application application;

    @BeforeClass
    public static void init() {
        application = new HeadlessApplication(new ApplicationAdapter() {
        });
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    @Test
    public void basicTest() {
        ActionLogger logger = ActionLogger.getLogger(SingleSoloStrategyTest.class, "Logger", ActionLogger.LogLevel.DEBUG);
        SingleSoloStrategy strat = new SingleSoloStrategy();

        strat.solo(logger, true);

        assertTrue(strat.isActive());

        strat.print(logger,"Hello", "Strategy", ActionLogger.LogLevel.INFO);

        strat.solo(logger, false);

        assertFalse(strat.isActive());
    }

    @Test
    public void switchLoggersTest() {
        ActionLogger logger1 = ActionLogger.getLogger(SingleSoloStrategyTest.class, "Logger1", ActionLogger.LogLevel.INFO);
        ActionLogger logger2 = ActionLogger.getLogger(SingleSoloStrategyTest.class, "Logger2", ActionLogger.LogLevel.INFO);
        ActionLogger tmp = null;
        SingleSoloStrategy strat = new SingleSoloStrategy();

        strat.solo(logger1, true);
        strat.solo(logger2, true);

        Field field = null;
        try {
            field = strat.getClass().getDeclaredField("logger");
            field.setAccessible(true);
            tmp = (ActionLogger) field.get(strat);
        }
        catch (Exception e) {
            Gdx.app.exit();
        }

        assertSame(logger2, tmp);

    }
}
