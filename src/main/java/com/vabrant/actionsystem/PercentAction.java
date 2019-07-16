package com.vabrant.actionsystem;

public class PercentAction extends TimeAction {

	private Percentable percentable;
	
	public void setPercentable(Percentable percentable) {
		this.percentable = percentable;
	}
	
	@Override
	protected void percent(float percent) {
		percentable.setPercent(percent);
	}
	
	@Override
	public void reset() {
		super.reset();
		percentable = null;
	}

}
