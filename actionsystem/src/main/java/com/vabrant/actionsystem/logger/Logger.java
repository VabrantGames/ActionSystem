package com.vabrant.actionsystem.logger;

import com.vabrant.actionsystem.actions.Action;

public class Logger {

    public static final int NONE = 0;
    public static final int ERROR = 1;
    public static final int INFO = 2;
    public static final int DEBUG = 3;

    private final String loggerName;

    public Logger (String loggerName) {
        this.loggerName = loggerName;
    }

    public Logger (Class<?> klass) {
        loggerName = klass.getSimpleName();
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void info (Action<?> action, String header) {
        info(action, header, null);
    }

    public void info (Action<?> action, String header, String body) {
        info(action, header, body, null);
    }

    public void info (Action<?> action, String header, String body, Exception exception) {
        print(INFO, action, header, body, exception);
    }

    public void debug (Action<?> action, String header) {
        debug(action, header, null);
    }

    public void debug (Action<?> action, String header, String body) {
        debug(action, header, body, null);
    }

    public void debug (Action<?> action, String header, String body, Exception exception) {
        print(DEBUG, action, header, body, exception);
    }

    public void error (Action<?> action, String header) {
        error(action, header, null);
    }

    public void error (Action<?> action, String header, String body) {
        error(action, header, body, null);
    }

    public void error (Action<?> action, String header, String body, Exception exception) {
        print(ERROR, action, header, body, exception);
    }

    public void print (int level, Action action, String header, String body, Exception exception) {
        LoggerManager.print(level, action,  header, body, null);
    }
}
