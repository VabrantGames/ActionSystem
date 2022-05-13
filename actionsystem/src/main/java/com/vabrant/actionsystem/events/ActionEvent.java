package com.vabrant.actionsystem.events;

import com.vabrant.actionsystem.actions.Action;

public class ActionEvent extends Event {

    public static final String START_EVENT = "StartEvent";
    public static final String END_EVENT = "EndEvent";
    public static final String KILL_EVENT = "KillEvent";
    public static final String RESTART_EVENT = "RestartEvent";

    private Action action;

    public ActionEvent() {
        super(null);
    }

    public ActionEvent setAsStart() {
        setEventType(START_EVENT);
        return this;
    }

    public ActionEvent setAsEnd() {
        setEventType(END_EVENT);
        return this;
    }

    public ActionEvent setAsKill() {
        setEventType(KILL_EVENT);
        return this;
    }

    public ActionEvent setAsRestart() {
        setEventType(RESTART_EVENT);
        return this;
    }

    private void setEventType(String type) {
        this.type = type;
    }

    public ActionEvent setAction(Action action) {
        this.action = action;
        return null;
    }

    @Override
    public void reset() {
        super.reset();
        type = null;
        action = null;
    }
}
