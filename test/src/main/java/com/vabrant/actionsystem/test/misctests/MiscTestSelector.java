package com.vabrant.actionsystem.test.misctests;

import com.vabrant.actionsystem.test.scaletests.ScaleConflictTest;
import com.vabrant.testbase.TestBaseApplicationListener;
import com.vabrant.testbase.ActionSystemTestSelector;

public class MiscTestSelector extends ActionSystemTestSelector {

	Class[] tests = {
			ActionWatcherTest.class,
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
