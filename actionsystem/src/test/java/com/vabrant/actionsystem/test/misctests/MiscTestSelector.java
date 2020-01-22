package com.vabrant.actionsystem.test.misctests;

import com.vabrant.actionsystem.test.ActionSystemTestSelector;
import com.vabrant.actionsystem.test.scaletests.ScaleConflictTest;
import com.vabrant.testbase.TestBaseApplicationListener;

public class MiscTestSelector extends ActionSystemTestSelector {

	Class[] tests = {
			ActionWatcherTestOld.class,
			CountDownActionTest.class,
			SoundActionTest.class,
			ScaleConflictTest.class,
			MusicActionTest.class
	};
	
	public MiscTestSelector(TestBaseApplicationListener app) {
		super(app);
		addTests(tests);
	}

}
