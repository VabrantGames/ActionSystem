package com.vabrant.actionsystem.logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.StringBuilder;

public class GdxPrinter implements LoggerPrinter {

    private StringBuilder builder = new StringBuilder(75);

    @Override
    public void print(ActionLogger logger, String message, String body, ActionLogger.LogLevel level) {

        builder.clear();

        if (logger.getActionName() != null) {
            builder.append(logger.getActionName());
            builder.append(' ');
        }

        builder.append(message);

        if (body != null) {
            builder.append(" : ");
            builder.append(body);
        }

        switch (level) {
            case ERROR:
                Gdx.app.error(logger.getClassName(), builder.toString());
                break;
            case INFO:
                Gdx.app.log(logger.getClassName(), builder.toString());
                break;
            case DEBUG:
                Gdx.app.debug(logger.getClassName(), builder.toString());
                break;
        }
    }
}
