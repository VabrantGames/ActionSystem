package com.vabrant.actionsystem.test.actiontests;

import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.ActionSystemTestSelector;

public class PostActionsTest extends ActionSystemTestScreen {

	private final String actionName = "PostAction";
	
	public PostActionsTest(ActionSystemTestSelector screen) {
		super(screen);
		createTestObject();
//		ActionPools.logger.solo();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(screenCenterX - (testObject.width / 2));
		testObject.setY(screenCenterY - (testObject.height / 2));
	}
	
	@Override
	public void reset() {
		super.reset();
	}
	
	private ActionListener createPostActionListener() {
		return new ActionAdapter() {
			@Override
			public void actionEnd(Action a) {
				super.actionEnd(a);
				log("Action has ended", "");
			}
		};
	}

	@Override
	public void runTest() {
//		actionManager.addAction(
//			GroupAction.getAction()
//				.sequence()
//				.setName(actionName)
//				.add(
//					RotateAction.rotateBy(testObject, 180f, 1f, Interpolation.linear)
//						.addPostAction(ColorAction.changeColor(testObject, Color.RED, 0.5f, Interpolation.linear))
//					)
//				.add(
//					GroupAction.getAction()
//						.add(DelayAction.delay(5f))
//						.addPostAction(DelayAction.delay(5f))
//					)
//			);
		
		actionManager.addAction(
				RepeatAction.repeat(
							GroupAction.getAction()
								.sequence()
								.add(DelayAction.delay(0.5f))
								.add(DelayAction.delay(0.5f)
										.setName("LastGroupAction")
										.addPostAction(DelayAction.delay(0.5f)
														.setName("PostAction")
														.addListener(createPostActionListener())
														))
						, 2)
					.setName("Hello World")
				);
	}

}
