package com.vabrant.actionsystem.test.grouptests;

import com.vabrant.actionsystem.test.ActionSystemTestSelector;
import com.vabrant.testbase.TestBaseApplicationListener;

public class GroupTestsSelectScreen extends ActionSystemTestSelector {

	Class[] tests = {
			SequenceTest.class,
			RepeatedSequenceTest.class,
			ParallelTest.class,
			RepeatedParallelTest.class,
			GroupActionEndTest.class
	};
	
	public GroupTestsSelectScreen(TestBaseApplicationListener app) {
		super(app);
		addTests(tests);
	}

}
