package com.vabrant.actionsystem.logger;

import com.vabrant.actionsystem.actions.Action;

public class ActionLogger {

    public static final int LOGGER_NONE = 0;
    public static final int LOGGER_ERROR = 1;
    public static final int LOGGER_INFO = 2;
    public static final int LOGGER_DEBUG = 3;

    private final String loggerName;

    public ActionLogger(String loggerName) {
        this.loggerName = loggerName;
    }

    public ActionLogger(Class<?> klass) {
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
        print(LOGGER_INFO, action, header, body, exception);
    }

    public void info (int logLevel, String soloID, String header, String body, Exception exception) {
        ActionLoggerManager.print(LOGGER_INFO, logLevel, soloID, this, header, body, exception);
    }

    public void debug (Action<?> action, String header) {
        debug(action, header, null);
    }

    public void debug (Action<?> action, String header, String body) {
        debug(action, header, body, null);
    }

    public void debug (Action<?> action, String header, String body, Exception exception) {
        print(LOGGER_DEBUG, action, header, body, exception);
    }

    public void debug (int logLevel, String soloID, String header, String body, Exception exception) {
        ActionLoggerManager.print(LOGGER_DEBUG, logLevel, soloID, this, header, body, exception);
    }

    public void error (Action<?> action, String header) {
        error(action, header, null);
    }

    public void error (Action<?> action, String header, String body) {
        error(action, header, body, null);
    }

    public void error (Action<?> action, String header, String body, Exception exception) {
        print(LOGGER_ERROR, action, header, body, exception);
    }

    public void error (int logLevel, String soloID, String header, String body, Exception exception) {
        ActionLoggerManager.print(LOGGER_ERROR, logLevel, soloID, this, header, body, exception);
    }

    private void print (int levelType, Action action, String header, String body, Exception exception) {
        ActionLoggerManager.print(levelType, action.getLogLevel(), action.getName(), this, header, body, exception);
    }

}
