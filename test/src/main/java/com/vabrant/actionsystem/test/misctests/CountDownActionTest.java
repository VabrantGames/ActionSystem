package com.vabrant.actionsystem.test.misctests;

import com.vabrant.actionsystem.actions.CountDownAction;
import com.vabrant.actionsystem.actions.CountDownAction.CountDownActionListener;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class CountDownActionTest extends ActionSystemTestScreen {

	public CountDownActionTest(TestSelectScreen screen) {
		super(screen);
	}
	
	private CountDownActionListener createCountDownListener() {
		return new CountDownActionListener() {
			@Override
			public void currentCount(int count) {
				log("CurrentCount", Integer.toString(count));
			}
		};
	}

	@Override
	public void runTest() {
		actionManager.addAction(CountDownAction.CountDown(3)
				.addCountDownListener(createCountDownListener()));
	}

}