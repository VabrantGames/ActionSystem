
package com.vabrant.actionsystem.test.unittests;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.vabrant.actionsystem.logger.ActionLogger;
import com.vabrant.actionsystem.logger.GdxPrinter;
import com.vabrant.actionsystem.logger.LoggerPrinter;
import org.junit.BeforeClass;
import org.junit.Test;

public class GdxPrinterTest {

	private static Application application;

	@BeforeClass
	public static void init () {
		application = new HeadlessApplication(new ApplicationAdapter() {});
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}

	@Test
	public void printTest () {
		ActionLogger logger = ActionLogger.getLogger(ActionLoggerTest.class, "Printer", ActionLogger.LogLevel.INFO);
		LoggerPrinter printer = new GdxPrinter();

		printer.print(logger, "Hello", null, ActionLogger.LogLevel.INFO);
		printer.print(logger, "Hello", "Info", ActionLogger.LogLevel.INFO);
		printer.print(logger, "Hello", "Error", ActionLogger.LogLevel.ERROR);
		printer.print(logger, "Hello", "Debug", ActionLogger.LogLevel.DEBUG);
	}
}
