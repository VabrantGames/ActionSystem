package com.vabrant.actionsystem.test.tests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.events.ActionEvent;
import com.vabrant.actionsystem.events.ActionListener;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.test.TestObject;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class MoveActionTest extends ActionSystemTestListener {
	
	private boolean reverseBackToStart = false;
	private boolean reverse = false;
	private TestObject testObject;
	private LabelTextFieldFloatWidget xStartWidget;
	private LabelTextFieldFloatWidget xEndWidget;
	private LabelTextFieldFloatWidget yStartWidget;
	private LabelTextFieldFloatWidget yEndWidget;
	private LabelTextFieldFloatWidget xAmountWidget;
	private LabelTextFieldFloatWidget yAmountWidget;
	private LabelTextFieldFloatWidget angleWidget;
	private LabelTextFieldFloatWidget durationWidget;
	private DoubleLabelWidget testEndXWidget;
	private DoubleLabelWidget testEndYWidget;
	private DoubleLabelWidget currentXWidget;
	private DoubleLabelWidget currentYWidget;
	
//	private ActionListener<MoveAction> listener = new ActionAdapter<MoveAction>() {
//		public void actionEnd(MoveAction a) {
//			currentXWidget.setValue(testObject.getX());
//			currentYWidget.setValue(testObject.getY());
//		}
//	};

	private ActionListener listener = new ActionListener() {
		@Override
		public void onEvent(ActionEvent e) {
			currentXWidget.setValue(testObject.getX());
			currentYWidget.setValue(testObject.getY());
		}
	};

	@Override
	public void create() {
		super.create();
		testObject = new TestObject();
		testObject.setSize(50, 50);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		float x = (hudViewport.getWorldWidth() - testObject.width) / 2;
		float y = (hudViewport.getWorldHeight() - testObject.height) / 2;
		testObject.setX(x);
		testObject.setY(y);
		xStartWidget.textField.setText(String.valueOf(x));
		yStartWidget.textField.setText(String.valueOf(y));
	}

	@Override
	public void createHud(Table root, Skin skin) {
		Label label = new Label("Set Values", new LabelStyle(skin.get(LabelStyle.class)));
		label.getStyle().fontColor = Color.BLACK;
		root.add(label).left();
		root.row();
		
		xStartWidget = new LabelTextFieldFloatWidget("xStart: ", skin, root, 0);
		xEndWidget = new LabelTextFieldFloatWidget("xEnd: ", skin, root, 0);
		root.row();
		
		yStartWidget = new LabelTextFieldFloatWidget("yStart: ", skin, root, 0);
		yEndWidget = new LabelTextFieldFloatWidget("yEnd: ", skin, root, 0);
		root.row();
		
		xAmountWidget = new LabelTextFieldFloatWidget("xAmount: ", skin, root, 50);
		yAmountWidget = new LabelTextFieldFloatWidget("yAmount: ", skin, root, 50);
		root.row();
		
		durationWidget = new LabelTextFieldFloatWidget("duration: ", skin, root, 1);
		root.row();
		
		angleWidget = new LabelTextFieldFloatWidget("angle: ", skin, root, 0);
		root.row();

		//Metrics
		Label metricsLabel = new Label("Metrics", new LabelStyle(skin.get(LabelStyle.class)));
		metricsLabel.getStyle().fontColor = Color.BLACK;
		root.add(metricsLabel).left();
		root.row();
		
		testEndXWidget = new DoubleLabelWidget("TestEndX: ", skin, root);
		root.row();
		
		testEndYWidget = new DoubleLabelWidget("TestEndY: ", skin, root);
		root.row();
		
		currentXWidget = new DoubleLabelWidget("CurrentX: ", skin, root);
		currentYWidget = new DoubleLabelWidget("CurrentY: ", skin, root);
	}
	
	private void setupTest(float endX, float endY) {
		testObject.setX(xStartWidget.getValue());
		testObject.setY(yStartWidget.getValue());
		testEndXWidget.setValue(endX);
		testEndYWidget.setValue(endY);
	}
	
	@Override
	public void createTests() {
		addTest(new ActionTest("MoveXBy") {
			@Override
			public Action<?> run() {
				setupTest(xStartWidget.getValue() + xAmountWidget.getValue(), yStartWidget.getValue());
				MoveAction action = MoveAction.moveXBy(testObject, xAmountWidget.getValue(), durationWidget.getValue(), Interpolation.exp5Out)
//							.addListener(listener)
						.subscribeToEvent(ActionEvent.END_EVENT, listener)
						.setName("MoveXto")
						.reverseBackToStart(reverseBackToStart)
						.setReverse(reverse);
				return action;
			}
		});
		
		addTest(new ActionTest("MoveYBy") {
			@Override
			public Action<?> run() {
				setupTest(testObject.getX(), yStartWidget.getValue() + yAmountWidget.getValue());
				MoveAction action = MoveAction.moveYBy(testObject, yAmountWidget.getValue(), durationWidget.getValue(), Interpolation.exp5Out)
//						.addListener(listener)
						.subscribeToEvent(ActionEvent.END_EVENT, listener)
						.setName("MoveYBy")
						.reverseBackToStart(reverseBackToStart)
						.setReverse(reverse);
				return action;
			}
		});
		
		addTest(new ActionTest("MoveBy") {
			@Override
			public Action<?> run() {
				setupTest(xStartWidget.getValue() + xAmountWidget.getValue(), yStartWidget.getValue() + yAmountWidget.getValue());
				MoveAction action = MoveAction.moveBy(testObject, xAmountWidget.getValue(), yAmountWidget.getValue(), durationWidget.getValue(), Interpolation.exp5Out)
//							.addListener(listener)
						.subscribeToEvent(ActionEvent.END_EVENT, listener)
						.setName("MoveBy")
						.reverseBackToStart(reverseBackToStart)
						.setReverse(reverse);
				return action;
			}
		});
		
		addTest(new ActionTest("MoveByAngle") {
			@Override
			public Action<?> run() {
				final float angle = angleWidget.getValue();
				final float endX = xStartWidget.getValue() + (xAmountWidget.getValue() * MathUtils.cosDeg(angle));
				final float endY = yStartWidget.getValue() + (xAmountWidget.getValue() * MathUtils.sinDeg(angle));
				setupTest(endX, endY);
				MoveAction action = MoveAction.moveByAngleDeg(testObject, angle, xAmountWidget.getValue(), durationWidget.getValue(), Interpolation.exp5Out)
//							.addListener(listener)
						.subscribeToEvent(ActionEvent.END_EVENT, listener)
						.setName("MoveByAngle")
						.reverseBackToStart(reverseBackToStart)
						.setReverse(reverse);
				return action;
			}
		});
		
		addTest(new ActionTest("MoveByAngleRad") {
			@Override
			public Action<?> run() {
				final float angleDeg = angleWidget.getValue() * MathUtils.radiansToDegrees;
				final float endX = xStartWidget.getValue() + (xAmountWidget.getValue() * MathUtils.cosDeg(angleDeg));
				final float endY = yStartWidget.getValue() + (xAmountWidget.getValue() * MathUtils.sinDeg(angleDeg));
				setupTest(endX, endY);
				MoveAction action = MoveAction.moveByAngleRad(testObject, angleWidget.getValue(), xAmountWidget.getValue(), durationWidget.getValue(), Interpolation.exp5Out)
//							.addListener(listener)
						.subscribeToEvent(ActionEvent.END_EVENT, listener)
						.setName("MoveByAngleRadians")
						.reverseBackToStart(reverseBackToStart)
						.setReverse(reverse);
				return action;
			}
		});
		
		addTest(new ActionTest("MoveXTo") {
			@Override
			public Action<?> run() {
				setupTest(xEndWidget.getValue(), yStartWidget.getValue());
				MoveAction action = MoveAction.moveXTo(testObject, xEndWidget.getValue(), durationWidget.getValue(), Interpolation.exp5Out)
//							.addListener(listener)
						.subscribeToEvent(ActionEvent.END_EVENT, listener)
						.setName("MoveXto")
						.reverseBackToStart(reverseBackToStart)
						.setReverse(reverse);
				return action;
			}
		});
		
		addTest(new ActionTest("MoveYTo") {
			@Override
			public Action<?> run() {
				setupTest(xStartWidget.getValue(), yEndWidget.getValue());
				MoveAction action = MoveAction.moveYTo(testObject, yEndWidget.getValue(), durationWidget.getValue(), Interpolation.exp5Out)
//							.addListener(listener)
						.subscribeToEvent(ActionEvent.END_EVENT, listener)
						.setName("MoveYTo")
						.reverseBackToStart(reverseBackToStart)
						.setReverse(reverse);
				return action;
			}
		});
		
		addTest(new ActionTest("PingPong") {
			@Override
			public Action<?> run() {
				setupTest(xStartWidget.getValue(), yStartWidget.getValue());
				RepeatAction action = RepeatAction.repeat(
						MoveAction.moveXBy(testObject, 50, durationWidget.getValue(), Interpolation.linear), 3)
						.pingPong(true);
				return action;
			}
		});
		
	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		testObject.draw(shapeDrawer);
	}

}
