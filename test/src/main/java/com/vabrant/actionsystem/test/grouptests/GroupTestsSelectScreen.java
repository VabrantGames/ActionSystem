package com.vabrant.actionsystem.test.grouptests;

import com.vabrant.testbase.TestBaseApplicationListener;
import com.vabrant.testbase.ActionSystemTestSelector;

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
