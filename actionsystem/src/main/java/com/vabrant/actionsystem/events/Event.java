package com.vabrant.actionsystem.events;

import com.badlogic.gdx.utils.Pool;

public class Event implements Pool.Poolable {

    protected String type;

    public Event(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public void reset() {
    }
}
