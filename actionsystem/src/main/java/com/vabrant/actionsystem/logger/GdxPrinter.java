package com.vabrant.actionsystem.logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.StringBuilder;

public class GdxPrinter implements LoggerPrinter {

    private StringBuilder builder = new StringBuilder(75);

    @Override
    public void print(ActionLogger logger, String message, String body, ActionLogger.LogLevel level) {
        switch (level) {
            case ERROR:
                Gdx.app.error(logger.getClassName(), ActionLogger.formatOutput(builder, logger.getActionName(), message, body));
                break;
            case INFO:
                Gdx.app.log(logger.getClassName(), ActionLogger.formatOutput(builder, logger.getActionName(), message, body));
                break;
            case DEBUG:
                Gdx.app.debug(logger.getClassName(), ActionLogger.formatOutput(builder, logger.getActionName(), message, body));
                break;
        }
    }
}
