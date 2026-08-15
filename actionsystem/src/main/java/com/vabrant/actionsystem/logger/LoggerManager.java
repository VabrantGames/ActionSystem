package com.vabrant.actionsystem.logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.StringBuilder;
import com.vabrant.actionsystem.actions.Action;

public class LoggerManager {

    private static boolean bypass;
    private static int DEFAULT_LEVEL = Logger.LOGGER_NONE;
    private static Logger bypassLogger = new Logger(LoggerManager.class);
    private static final ObjectMap<Class<?>, Logger> loggers = new ObjectMap<>();
    private static final StringBuilder STRING_BUILDER = new StringBuilder(300);
    private static final ObjectSet<String> soloIDS = new ObjectSet<>();

    public static void setBypass (boolean bypass) {
        LoggerManager.bypass = bypass;
    }

    public static boolean canLog () {
        return !bypass;
    }

    public static void solo (Action<?> action) {
        if (action.getName() == null) {
            Gdx.app.error("Can't solo action. Action name is null", "");
            return;
        }

        solo(action.getName());
    }

    public static void solo (String soloID) {
        if (soloIDS.contains(soloID)) return;

        soloIDS.add(soloID);
    }

    public static Logger getLogger (Class<?> klass) {
        if (bypass) {
            return bypassLogger;
        } else {
            Logger logger = loggers.get(klass);

            if (logger != null) {
                return logger;
            } else {
                logger = new Logger(klass);
                loggers.put(klass, logger);
                return logger;
            }
        }
    }

    public static void print (int levelType, int logLevel, String actionName, Logger logger, String header, String body, Exception exception) {
        if (bypass || levelType > logLevel) return;

        if (soloIDS.size > 0 && (actionName == null || !soloIDS.contains(actionName))) return;

        STRING_BUILDER.clear();

        String identifierString;

        if (actionName != null) {
            STRING_BUILDER.append(logger.getLoggerName());
            STRING_BUILDER.append("-");
            STRING_BUILDER.append(actionName);
            identifierString = STRING_BUILDER.toString();
            STRING_BUILDER.clear();
        } else {
            identifierString = logger.getLoggerName();
        }

        STRING_BUILDER.append(header);

        if (body != null) {
            STRING_BUILDER.append(" : ");
            STRING_BUILDER.append(body);
        }

        boolean hasException = exception != null;

        switch (levelType) {
            case Logger.LOGGER_INFO:
                if (!hasException) {
                    Gdx.app.log(identifierString, STRING_BUILDER.toString());
                } else {
                    Gdx.app.log(identifierString, STRING_BUILDER.toString(), exception);
                }
                break;
            case Logger.LOGGER_DEBUG:
                if (!hasException) {
                    Gdx.app.debug(identifierString, STRING_BUILDER.toString());
                } else {
                    Gdx.app.debug(identifierString, STRING_BUILDER.toString(), exception);
                }
                break;
            case Logger.LOGGER_ERROR:
                if (!hasException) {
                    Gdx.app.error(identifierString, STRING_BUILDER.toString());
                } else {
                    Gdx.app.error(identifierString, STRING_BUILDER.toString(), exception);
                }
        }
    }

}
