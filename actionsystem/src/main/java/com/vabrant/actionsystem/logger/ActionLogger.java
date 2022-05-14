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

	public static LoggerPrinter DEFAULT_PRINTER = new GdxPrinter();
	public static SoloStrategy DEFAULT_SOLO_STRATEGY = new SingleSoloStrategy();
	public static SoloStrategy SOLO_STRATEGY = DEFAULT_SOLO_STRATEGY;

	public static ActionLogger getLogger(Class<?> c) {
		return getLogger(c, LogLevel.NONE);
	}

	public static ActionLogger getLogger(Class<?> c, LogLevel level) {
		return getLogger(c, null, level);
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

	public static String formatOutput(StringBuilder b, String actionName, String message, String body) {
		StringBuilder builder = null;

        if (b == null) {
            int length = (actionName != null ? actionName.length() : 0) + (body != null ? body.length() : 0) + message.length();
            builder = new StringBuilder(length);
        }
        else {
            builder = b;
            builder.clear();
        }

		if (actionName != null && !actionName.isEmpty()) {
			builder.append('(');
			builder.append(actionName);
			builder.append(')');
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

    private LogLevel logLevel;
	private String actionName;
	private String className;
	private LoggerPrinter printer;

	private ActionLogger() {}

    private ActionLogger(Class<?> c, String name, LogLevel level) {
        className = c.getSimpleName();
        setName(name);
        logLevel = level;
		printer = DEFAULT_PRINTER;
    }

	public void setName(String actionName) {
		if(actionName == null) return;
		this.actionName = actionName;
	}

	public String getClassName() {
		return className;
	}

	public String getActionName() {
		return actionName == null ? "" : actionName;
	}

	public void setPrinter(LoggerPrinter printer) {
		if (printer != null) {
			this.printer = printer;
		}
		else {
			this.printer = DEFAULT_PRINTER;
		}
	}

	public void clearActionName() {
		actionName = null;
	}

	public void reset() {
		clearActionName();
		logLevel = LogLevel.NONE;
		printer = DEFAULT_PRINTER;
		solo(false);
	}

	public void setLevel(LogLevel level) {
		logLevel = level;
	}

	public LogLevel getLevel() {
		return logLevel;
	}

	public void solo(boolean solo) {
		SOLO_STRATEGY.solo(this, solo);
	}

	public void print(String message, String body, LogLevel level) {
		if (RESTRICT_OUTPUT || !logLevel.canPrint(level)) return;
		printer.print(this, message, body, level);
	}

	void print0(String message, String body, LogLevel level) {
		if (RESTRICT_OUTPUT || !logLevel.canPrint(level)) return;

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
		print0(message, body, LogLevel.INFO);
	}

	public void debug(String message) {
		debug(message, null);
	}

	public void debug(String message, String body) {
		print0(message, body, LogLevel.DEBUG);
	}

	public void error(String message) {
		error(message, null);
	}

	public void error(String message, String body) {
		print0(message, body, LogLevel.ERROR);
	}

}
