package com.vabrant.actionsystem.logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.StringBuilder;
import com.vabrant.actionsystem.actions.Action;

public class LoggerManager {

    private static boolean bypass;
    private static int DEFAULT_LEVEL = Logger.NONE;
    private static Logger bypassLogger = new Logger(LoggerManager.class);
    private static final ObjectMap<Class, Logger> loggers = new ObjectMap<>();
    private static final StringBuilder STRING_BUILDER = new StringBuilder(300);
    private static Array<String> soloLoggers = new Array<>();
//    private static ObjectMap<String, Action> soloLoggers;

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

        if (soloLoggers.contains(action.getName(), false)) return;

        soloLoggers.add(action.getName());
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

    public static void print (int levelType, Action action, String header, String body, Exception exception) {
        print(levelType, action.getLogLevel(), action.getName(), action.getLogger(), header, body, exception);
    }

    public static void print (int levelType, int logLevel, String actionName, Logger logger, String header, String body, Exception exception) {
        if (bypass || levelType > logLevel) return;

        if (soloLoggers.size > 0) {
            if (actionName == null || !soloLoggers.contains(actionName, false)) return;
        }

//        if (soloLoggers.size > 0 && !soloLoggers.containsKey(action)) return;

        STRING_BUILDER.clear();

        String identifierString;

        if (actionName != null) {
            STRING_BUILDER.append(actionName);
            STRING_BUILDER.append("-");
            STRING_BUILDER.append(logger.getLoggerName());
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

        switch (levelType) {
            case Logger.INFO:
                if (exception == null) {
                    Gdx.app.log(identifierString, STRING_BUILDER.toString());
                } else {
                    Gdx.app.log(identifierString, STRING_BUILDER.toString(), exception);
                }
                break;
            case Logger.DEBUG:
                if (exception == null) {
                    Gdx.app.debug(identifierString, STRING_BUILDER.toString());
                } else {
                    Gdx.app.debug(identifierString, STRING_BUILDER.toString(), exception);
                }
                break;
            case Logger.ERROR:
                if (exception == null) {
                    Gdx.app.error(identifierString, STRING_BUILDER.toString());
                } else {
                    Gdx.app.error(identifierString, STRING_BUILDER.toString(), exception);
                }
        }
    }

}
