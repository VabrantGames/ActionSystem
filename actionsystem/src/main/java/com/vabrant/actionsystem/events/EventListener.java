package com.vabrant.actionsystem.events;

public interface EventListener<T extends Event> {
    public void onEvent(T e);
}
