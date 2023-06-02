
package com.vabrant.actionsystem.test.unittests;

import static junit.framework.TestCase.*;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.vabrant.actionsystem.logger.ActionLogger;
import com.vabrant.actionsystem.logger.LoggerPrinter;
import com.vabrant.actionsystem.logger.SingleSoloStrategy;
import java.lang.reflect.Field;
import org.junit.BeforeClass;
import org.junit.Test;

public class SingleSoloStrategyTest {

	private static Application application;

	@BeforeClass
	public static void init () {
		application = new HeadlessApplication(new ApplicationAdapter() {});
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}

	@Test
	public void basicTest () {
		ActionLogger logger1 = ActionLogger.getLogger(SingleSoloStrategyTest.class, "Logger1", ActionLogger.LogLevel.DEBUG);
		ActionLogger logger2 = ActionLogger.getLogger(SingleSoloStrategyTest.class, "Logger2", ActionLogger.LogLevel.DEBUG);

		logger2.setPrinter(new LoggerPrinter() {
			@Override
			public void print (ActionLogger logger, String message, String body, ActionLogger.LogLevel level) {
				throw new RuntimeException("Should not print");
			}
		});

		logger1.solo(true);

		// Attempt to print when logger logger1 is solo'd;
		logger2.info("Hello");

		logger1.info("Hello", "World");
	}

	@Test
	public void switchLoggersTest () {
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
			tmp = (ActionLogger)field.get(strat);
		} catch (Exception e) {
			Gdx.app.exit();
		}

		assertSame(logger2, tmp);
	}
}
