package com.vabrant.actionsystem;

public interface ActionListener {
	public void actionStart(Action a);
	public void actionEnd(Action a);
	public void actionKill(Action a);
	public void actionRestart(Action a);
}
