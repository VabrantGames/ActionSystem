package com.vabrant.actionsystem.logger;

public class SingleSoloStrategy implements SoloStrategy {

    private ActionLogger logger;

    @Override
    public void solo(ActionLogger logger, boolean solo) {
        if (this.logger != null && !solo) {
            if (this.logger.equals(logger)) {
                this.logger = null;
            }
        }
        else if (solo) {
            this.logger = logger;
        }
    }

    @Override
    public boolean isActive() {
        return logger != null;
    }

    @Override
    public void print(ActionLogger logger, String message, String body, ActionLogger.LogLevel level) {
        if (!this.logger.equals(logger)) return;

        logger.print(message, body, level);
    }
}
