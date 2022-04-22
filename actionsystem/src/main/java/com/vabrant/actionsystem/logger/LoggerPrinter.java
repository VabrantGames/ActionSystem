package com.vabrant.actionsystem.logger;

public interface LoggerPrinter {
    public void print(ActionLogger logger, String message, String body, ActionLogger.LogLevel level);
}
