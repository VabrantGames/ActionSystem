/**
 *	Copyright 2019 John Barton
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package com.vabrant.actionsystem.logger;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.OrderedSet;
import com.badlogic.gdx.utils.StringBuilder;

import java.util.Formatter;

public class ActionLogger {

	private static boolean RESTRICT_OUTPUT = false;

	public static final int NONE = 0;
	public static final int ERROR = 1;
	public static final int INFO = 2;
	public static final int DEBUG = 3;

	public enum LogLevel {
		NONE(0),
		ERROR(1),
		INFO(2),
		DEBUG(3);

        private int level;

		private LogLevel(int level) {
			this.level = level;
		}

        private boolean canPrint(LogLevel type) {
            return type.level <= this.level;
        }
	}

//    private static boolean IS_SOLO;
	private static final String EMPTY_STRING = "";
//	private static final StringBuilder STRING_BUILDER = new StringBuilder(50);
//	private static ActionLogger SOLO_LOGGER;
//	private static OrderedSet<ActionLogger> SOLO_LOGGERS;

	public static LoggerPrinter DEFAULT_PRINTER = new GdxPrinter();
	private static SoloStrategy DEFAULT_SOLO_STRATEGY = new SingleSoloStrategy();
	private static SoloStrategy SOLO_STRATEGY = DEFAULT_SOLO_STRATEGY;

	public static ActionLogger getLogger(Class<?> c) {
		return getLogger(c, LogLevel.NONE);
	}

	public static ActionLogger getLogger(Class<?> c, LogLevel level) {
		return getLogger(c, null, level);
	}

	public static ActionLogger getLogger(Class<?> c, String name, int level) {
		return new ActionLogger(c, name, level);
	}

    public static ActionLogger getLogger(Class<?> c, String name, LogLevel level) {
        return new ActionLogger(c, name, level);
    }

	public static void restrictOutput(boolean restrict) {
		RESTRICT_OUTPUT = restrict;
	}

	public static void setSoloStrategy(SoloStrategy strategy) {
		SOLO_STRATEGY = strategy;
		if (SOLO_STRATEGY == null) SOLO_STRATEGY = DEFAULT_SOLO_STRATEGY;
	}

	public static String formatOutput(String actionName, String message, String body) {
		StringBuilder builder = new StringBuilder(75);

		if (actionName != null) {
			builder.append(actionName);
			builder.append(' ');
		}

		if (message == null) throw new NullPointerException("Message can't be null");

		builder.append(message);

		if (body != null) {
			builder.append(" : ");
			builder.append(body);
		}

		return builder.toString();
	}

//    public static void resetSoloLoggers() {
//        if (SOLO_LOGGER != null) {
//            SOLO_LOGGER.solo = false;
//            SOLO_LOGGER = null;
//        }
//        else if (SOLO_LOGGERS != null) {
//            for (ActionLogger l : SOLO_LOGGERS) {
//                l.solo = false;
//            }
//            SOLO_LOGGERS = null;
//        }
//        IS_SOLO = false;
//    }

//	public static void setSoloLogger(ActionLogger logger) {
////		setSoloLoggers(logger, null);
//		resetSoloLoggers();
//		logger.solo = true;
//		SOLO_LOGGER = logger;
//		IS_SOLO = true;
//	}
//
//	public static void setSoloLoggers(OrderedSet<ActionLogger> loggers) {
//		resetSoloLoggers();
//
//        for (ActionLogger l : loggers) {
//            l.solo = true;
//        }
//
//        SOLO_LOGGERS = loggers;
//        IS_SOLO = true;
////		setSoloLoggers(null, loggers);
//	}

//    private static void setSoloLoggers(ActionLogger logger, OrderedSet<ActionLogger> loggers) {
//        if (logger != null) {
//            resetSoloLoggers();
//            logger.solo = true;
//            SOLO_LOGGER = logger;
//        }
//        else {
//            resetSoloLoggers();
//
//            for (ActionLogger l : loggers) {
//                l.solo = true;
//            }
//
//            SOLO_LOGGERS = loggers;
//        }
//
//        IS_SOLO = true;
//    }

	private boolean solo;
	private int level;
    private LogLevel logLevel;
	private String actionName;
	private String className;

//	private boolean ownsPrinter;
	private LoggerPrinter printer = DEFAULT_PRINTER;

	private ActionLogger() {}

    private ActionLogger(Class<?> c, String name, LogLevel level) {
        className = c.getSimpleName();
        setName(name);
        logLevel = level;
    }

	private ActionLogger(Class<?> c, String name, int level) {
		className = c.getSimpleName();
		setName(name);
		this.level = level;
	}

	public void setName(String actionName) {
		if(actionName == null) return;

		this.actionName = '(' + actionName + ')';

//		STRING_BUILDER.clear();
//		STRING_BUILDER.append('(');
//		STRING_BUILDER.append(actionName);
//		STRING_BUILDER.append(')');
//		this.actionName = STRING_BUILDER.toString();
	}

	public String getClassName() {
		return className;
	}

	public String getActionName() {
		return actionName == null ? EMPTY_STRING : actionName;
	}

	public void setPrinter(LoggerPrinter printer) {
		this.printer = printer;
		if (this.printer == null) printer = DEFAULT_PRINTER;
	}

	public void clearActionName() {
		actionName = null;
	}

	public void reset() {
		clearActionName();
//		level = NONE;
		logLevel = LogLevel.NONE;
		printer = DEFAULT_PRINTER;
		solo(false);
	}

	public void setLevel(int l) {

	}

	public void setLevel(LogLevel level) {
//		this.level = MathUtils.clamp(level, NONE, DEBUG);
		logLevel = level;
	}

	public int getLevel() {
		return level;
	}

	public void print(String message, String body, LogLevel level) {
		if (RESTRICT_OUTPUT || !logLevel.canPrint(level)) return;
		printer.print(this, message, body, level);
//		if (SOLO_STRATEGY.isActive()) {
//			SOLO_STRATEGY.print(message, body, level);
//		}
//		else {
//
//		}
	}

	void print0(String message, String body, LogLevel level) {
		if (SOLO_STRATEGY.isActive()) {
			SOLO_STRATEGY.print(this, message, body, level);
		}
		else {
			printer.print(this, message, body, level);
		}
	}

	public void info(String message) {
		info(message, null);
	}

	public void info(String message, String body) {
//		if(RESTRICT_OUTPUT || level < INFO) return;
//		print(message, body, INFO);
//        if (RESTRICT_OUTPUT || !logLevel.canPrint(LogLevel.INFO)) return;
//        printer.print(this, message, body, LogLevel.INFO);
		print0(message, body, LogLevel.INFO);
	}

	public void debug(String message) {
		debug(message, null);
	}

	public void debug(String message, String body) {
//		if(RESTRICT_OUTPUT || level < DEBUG) return;
//		print(message, body, DEBUG);
//        if (RESTRICT_OUTPUT || !logLevel.canPrint(LogLevel.DEBUG)) return;
//        printer.print(this, message, body, LogLevel.DEBUG);
		print0(message, body, LogLevel.DEBUG);
	}

	public void error(String message) {
		error(message, null);
	}

	public void error(String message, String body) {
//		if(RESTRICT_OUTPUT || level < ERROR) return;
//		print(message, body, ERROR);
//        if (RESTRICT_OUTPUT || !logLevel.canPrint(LogLevel.ERROR)) return;
//        printer.print(this, message, body, LogLevel.ERROR);
		print0(message, body, LogLevel.ERROR);
	}

	public void solo(boolean solo) {
		SOLO_STRATEGY.solo(this, solo);
	}

//	public void solo(OrderedSet<ActionLogger> loggers) {
//		if (RESTRICT_OUTPUT) return;
//
//        if (loggers != null) {
//            setSoloLoggers(loggers);
//        }
//        else {
//            resetSoloLoggers();
//        }
//	}

//	private void print(String message, String body, int level) {
//		if(IS_SOLO && !solo) return;
//
//		STRING_BUILDER.clear();
//
//		if(actionName != null) {
//			STRING_BUILDER.append(actionName);
//			STRING_BUILDER.append(' ');
//		}
//
//		STRING_BUILDER.append(message);
//
//		if(body != null) {
//			STRING_BUILDER.append(" : ");
//			STRING_BUILDER.append(body);
//		}
//
//		switch(level) {
//			case ERROR:
//				Gdx.app.error(className, STRING_BUILDER.toString());
//				break;
//			case INFO:
//				Gdx.app.log(className, STRING_BUILDER.toString());
//				break;
//			case DEBUG:
//				Gdx.app.debug(className, STRING_BUILDER.toString());
//				break;
//		}
//	}

}
