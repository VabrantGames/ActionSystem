package com.vabrant.actionsystem.test;

public interface Test {
	public void runTest();
	public default void check1() {};
	public default void check2() {};
	public default void check3() {};
	public default void check4() {};
}
