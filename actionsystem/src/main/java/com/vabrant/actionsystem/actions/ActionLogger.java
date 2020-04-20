package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.StringBuilder;

public class ActionLogger {
	
	private static boolean USE_SYS_OUT = false;
	private static boolean RESTRICT_OUTPUT = false;
	
	public static final int NONE = 0;
	public static final int ERROR = 1;
	public static final int INFO = 2;
	public static final int DEBUG = 3;
	
	private static final String EMPTY_STRING = ""; 
	private static final StringBuilder STRING_BUILDER = new StringBuilder(50);	
	private static final ObjectSet<ActionLogger> SOLO_LOGGERS = new ObjectSet<>(10);

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
		this.actionName = STRING_BUILDER.toString();
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
		
		this.solo = solo;
		
		if(solo) {
			SOLO_LOGGERS.add(this);
		}
		else {
			SOLO_LOGGERS.remove(this);
		}
	}
	
	public void reset() {
		clearActionName();
		level = NONE;
		solo(false);
	}
	
	private void print(String message, String body, int level) {
		if(SOLO_LOGGERS.size > 0 && !solo) return;
		
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
