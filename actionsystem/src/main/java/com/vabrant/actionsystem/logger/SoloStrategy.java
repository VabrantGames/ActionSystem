package com.vabrant.actionsystem.logger;

public interface SoloStrategy {
    public void solo(ActionLogger action, boolean solo);
    public void print(ActionLogger logger, String message, String body, ActionLogger.LogLevel level);
    public boolean isActive();
}
