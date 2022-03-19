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
package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.StringBuilder;

public class ActionLogger {

	private static boolean USE_SYS_OUT = false;
	private static boolean RESTRICT_OUTPUT = false;

	public static final int NONE = 0;
	public static final int ERROR = 1;
	public static final int INFO = 2;
	public static final int DEBUG = 3;

    private static boolean IS_SOLO;
	private static final String EMPTY_STRING = "";
	private static final StringBuilder STRING_BUILDER = new StringBuilder(50);
	private static ActionLogger SOLO_LOGGER;
	private static ObjectSet<ActionLogger> SOLO_LOGGERS;
//	private static final ObjectSet<ActionLogger> SOLO_LOGGERS = new ObjectSet<>(10);

	public static ActionLogger getLogger(Class<?> c) {
		return getLogger(c, NONE);
	}

	public static ActionLogger getLogger(Class<?> c, int level) {
		return getLogger(c, null, level);
	}

	public static ActionLogger getLogger(Class<?> c, String name, int level) {
		return new ActionLogger(c, name, level);
	}

	public static void restrictOutput(boolean restrict) {
		RESTRICT_OUTPUT = restrict;
	}

	public static void useSysOut() {
		USE_SYS_OUT = true;
	}

    private static void resetSoloLoggers() {
        if (SOLO_LOGGER != null) {
            SOLO_LOGGER.solo = false;
            SOLO_LOGGER = null;
        }
        else if (SOLO_LOGGERS != null) {
            for (ActionLogger l : SOLO_LOGGERS) {
                l.solo = false;
            }
            SOLO_LOGGERS = null;
        }
        IS_SOLO = false;
    }

    private static void setSoloLoggers(ActionLogger logger, ObjectSet<ActionLogger> loggers) {
        if (logger != null) {
            resetSoloLoggers();
            logger.solo = true;
            SOLO_LOGGER = logger;
        }
        else {
            resetSoloLoggers();

            for (ActionLogger l : loggers) {
                l.solo = true;
            }

            SOLO_LOGGERS = loggers;
        }

        IS_SOLO = true;
    }

	private boolean solo;
	private int level;
	private String actionName;
	private String className;

	private ActionLogger() {}

	private ActionLogger(Class<?> c, String name, int level) {
		className = c.getSimpleName();
		setName(name);
		this.level = level;
	}

	public void setName(String actionName) {
		if(RESTRICT_OUTPUT || actionName == null || actionName.isEmpty()) return;
		STRING_BUILDER.clear();
		STRING_BUILDER.append('(');
		STRING_BUILDER.append(actionName);
		STRING_BUILDER.append(')');
		actionName = STRING_BUILDER.toString();
	}

	public String getClassName() {
		return className;
	}

	public String getActionName() {
		return actionName == null ? EMPTY_STRING : actionName;
	}

	public void clearActionName() {
		actionName = null;
	}

	public void setLevel(int level) {
		this.level = MathUtils.clamp(level, NONE, DEBUG);
	}

	public int getLevel() {
		return level;
	}

	public void info(String message) {
		info(message, null);
	}

	public void info(String message, String body) {
		if(RESTRICT_OUTPUT || level < INFO) return;
		print(message, body, INFO);
	}

	public void debug(String message) {
		debug(message, null);
	}

	public void debug(String message, String body) {
		if(RESTRICT_OUTPUT || level < DEBUG) return;
		print(message, body, DEBUG);
	}

	public void error(String message) {
		error(message, null);
	}

	public void error(String message, String body) {
		if(RESTRICT_OUTPUT || level < ERROR) return;
		print(message, body, ERROR);
	}

	public void solo(boolean solo) {
		if(RESTRICT_OUTPUT) return;

        if (solo) {
            setSoloLoggers(this, null);
        }
        else {
            resetSoloLoggers();
        }


//        resetSoloLoggers();
//
//        this.solo = solo;
//        SOLO_LOGGER = this;


//		if(solo) {
//			SOLO_LOGGERS.add(this);
//		}
//		else {
//			SOLO_LOGGERS.remove(this);
//		}
	}

	public void solo(ObjectSet<ActionLogger> loggers) {
		if (RESTRICT_OUTPUT) return;

        if (loggers != null) {
            setSoloLoggers(null, loggers);
        }
        else {
            resetSoloLoggers();
        }

//		resetSoloLoggers();
//
//        for (ActionLogger l : loggers) {
//            l.solo = true;
//        }
//
//		SOLO_LOGGERS = loggers;
	}

	public void reset() {
		clearActionName();
		level = NONE;
		solo(false);
	}

	private void print(String message, String body, int level) {
//		if(SOLO_LOGGERS.size > 0 && !solo) return;
		if(IS_SOLO && !solo) return;

		STRING_BUILDER.clear();

		if(actionName != null) {
			STRING_BUILDER.append(actionName);
			STRING_BUILDER.append(' ');
		}

		STRING_BUILDER.append(message);

		if(body != null) {
			STRING_BUILDER.append(" : ");
			STRING_BUILDER.append(body);
		}

		if(USE_SYS_OUT) {
			STRING_BUILDER.insert(0, "[");
			STRING_BUILDER.insert(1, className);
			STRING_BUILDER.insert(1 + className.length(), "] ");
		}

		switch(level) {
			case ERROR:
				if(!USE_SYS_OUT) {
					Gdx.app.error(className, STRING_BUILDER.toString());
				}
				else {
					System.out.println(STRING_BUILDER.toString());
				}
				break;
			case INFO:
				if(!USE_SYS_OUT) {
					Gdx.app.log(className, STRING_BUILDER.toString());
				}
				else {
					System.out.println(STRING_BUILDER.toString());
				}
				break;
			case DEBUG:
				if(!USE_SYS_OUT) {
					Gdx.app.debug(className, STRING_BUILDER.toString());
				}
				else {
					System.out.println(STRING_BUILDER.toString());
				}
				break;
		}
	}

}
