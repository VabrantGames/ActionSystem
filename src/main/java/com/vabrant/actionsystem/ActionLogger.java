package com.vabrant.actionsystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.StringBuilder;

public class ActionLogger {
	
	private static boolean isRelease = false;
	
	public static final int NONE = 0;
	public static final int ERROR = 1;
	public static final int INFO = 2;
	public static final int DEBUG = 3;
	
	private static final StringBuilder STRING_BUILDER;	
	private static ActionLogger soloLogger;
	
	static {
		if(!isRelease) {
			STRING_BUILDER = new StringBuilder(50);
		}
		else {
			STRING_BUILDER = null;
		}
	}
	
	public static ActionLogger getLogger(Class<?> c, int level) {
		return isRelease ? null : new ActionLogger(c, level);
	}
	
	public static void setReleaseMode(boolean releaseMode) {
		ActionLogger.isRelease = releaseMode;
	}
	
	public static void solo(ActionLogger logger) {
		if(logger == null) return;
		soloLogger = logger;
	}
	
	private static final String EMPTY_STRING = ""; 
	
	private String actionName;
	private String className;
	private int level;
	
	private ActionLogger(Class<?> c) {
		this(c, NONE);
	}
	
	private ActionLogger(Class<?> c, int level) {
		if(!isRelease) {
			className = c.getSimpleName();
			this.level = level;
		}
	}
	
	public void setActionName(String actionName) {
		if(isRelease || actionName == null) return;
		STRING_BUILDER.clear();
		STRING_BUILDER.append(' ');
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
		if(isRelease || level < INFO) return;
		print(message, body, INFO);
	}
	
	public void debug(String message) {
		debug(message, null);
	}
	
	public void debug(String message, String body) {
		if(isRelease || level < DEBUG) return;
		print(message, body, DEBUG);
	}
	
	public void error(String message) {
		error(message, null);
	}
	
	public void error(String message, String body) {
		if(isRelease || level < ERROR) return;
		print(message, body, ERROR);
	}
	
	public void solo() {
		if(isRelease) return;
		ActionLogger.solo(this);
	}
	
	private void print(String message, String body, int level) {
		if(soloLogger != null && !soloLogger.equals(this)) return;
		
		STRING_BUILDER.clear();
		STRING_BUILDER.append(message);
		if(actionName != null) STRING_BUILDER.append(actionName);
		if(body != null) {
			STRING_BUILDER.append(" : ");
			STRING_BUILDER.append(body);
		}
		
		switch(level) {
			case ERROR:
				break;
			case INFO:
				Gdx.app.log(className, STRING_BUILDER.toString());
				break;
			case DEBUG:
				Gdx.app.debug(className, STRING_BUILDER.toString());
				break;
		}
	}

}
