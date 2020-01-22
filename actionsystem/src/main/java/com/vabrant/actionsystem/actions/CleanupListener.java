package com.vabrant.actionsystem.actions;

public interface CleanupListener<T extends Action> {
	public void cleanup(T a);
}
