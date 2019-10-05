package com.vabrant.actionsystem.test.grouptests;

import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class GroupTestsSelectScreen extends TestSelectScreen {

	Class[] tests = {
			SequenceTest.class,
			RepeatedSequenceTest.class,
			ParallelTest.class,
			RepeatedParallelTest.class,
			GroupActionEndTest.class
	};
	
	public GroupTestsSelectScreen(TestApplication app) {
		super(app);
		addTests(tests);
	}

}
