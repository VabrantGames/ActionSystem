package com.vabrant.actionsystem.actions;

public interface ActionListener<T extends Action<?>> {
	public void actionStart(T a);
	public void actionEnd(T a);
	public void actionKill(T a);
	public void actionRestart(T a);
}
