package com.vabrant.actionsystem.actions;

public class ActionAdapter<T extends Action<?>> implements ActionListener<T> {

	@Override
	public void actionStart(T a) {
	}

	@Override
	public void actionEnd(T a) {
	}

	@Override
	public void actionKill(T a) {
	}

	@Override
	public void actionRestart(T a) {
	}

	@Override
	public void actionCycleStart(T a) {
	}

	@Override
	public void actionCycleEnd(T a) {
	}

	@Override
	public void actionCycleKill(T a) {
	}

	@Override
	public void actionCycleRestart(T a) {
	}

}
