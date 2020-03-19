package com.vabrant.actionsystem.actions;

public interface PauseCondition {

	public Condition shouldPause();
//	public boolean shouldPause();
//	public boolean shouldResume();
	
	class Bob implements PauseCondition{

		@Override
		public Condition shouldPause() {
			return null;
		}
		
	}
}
