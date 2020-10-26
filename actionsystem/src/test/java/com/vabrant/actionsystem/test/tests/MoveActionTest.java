package com.vabrant.actionsystem.test.tests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.test.TestObject;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class MoveActionTest extends ActionSystemTestListener {
	
	private TestObject testObject;
	
	private boolean reverseBackToStart = false;
	private boolean reverse = false;
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
	
	private ActionListener<MoveAction> metricsListener = new ActionAdapter<MoveAction>() {
		public void actionEnd(MoveAction a) {
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
		testObject.setPosition(x, y);
		xStartWidget.textField.setText(String.valueOf(x));
		yStartWidget.textField.setText(String.valueOf(y));
	}

	@Override
	public void createHud(Table root, Skin skin) {
		LabelStyle labelHeaderStyle = new LabelStyle(skin.get(LabelStyle.class));
		labelHeaderStyle.fontColor = Color.BLACK;
		
		Label label = new Label("Set Values", labelHeaderStyle);
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
		Label metricsLabel = new Label("Metrics", labelHeaderStyle);
		root.add(metricsLabel).left();
		root.row();
		
		testEndXWidget = new DoubleLabelWidget("TestEndX: ", skin, root);
		root.row();
		
		testEndYWidget = new DoubleLabelWidget("TestEndY: ", skin, root);
		root.row();
		
		currentXWidget = new DoubleLabelWidget("CurrentX: ", skin, root);
		root.row();
		
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
				return MoveAction.moveXBy(testObject, xAmountWidget.getValue(), durationWidget.getValue(), Interpolation.exp5Out)
							.addListener(metricsListener)
							.setName("MoveXto")
							.reverseBackToStart(reverseBackToStart)
							.setReverse(reverse);
			}
		});
		
		addTest(new ActionTest("MoveYBy") {
			@Override
			public Action<?> run() {
				setupTest(testObject.getX(), yStartWidget.getValue() + yAmountWidget.getValue());
				return MoveAction.moveYBy(testObject, yAmountWidget.getValue(), durationWidget.getValue(), Interpolation.exp5Out)
						.addListener(metricsListener)
						.setName("MoveYBy")
						.reverseBackToStart(reverseBackToStart)
						.setReverse(reverse);
			}
		});
		
		addTest(new ActionTest("MoveBy") {
			@Override
			public Action<?> run() {
				setupTest(xStartWidget.getValue() + xAmountWidget.getValue(), yStartWidget.getValue() + yAmountWidget.getValue());
				return MoveAction.moveBy(testObject, xAmountWidget.getValue(), yAmountWidget.getValue(), durationWidget.getValue(), Interpolation.exp5Out)
							.addListener(metricsListener)
							.setName("MoveBy")
							.reverseBackToStart(reverseBackToStart)
							.setReverse(reverse);
			}
		});
		
		addTest(new ActionTest("MoveByAngle") {
			@Override
			public Action<?> run() {
				final float angle = angleWidget.getValue();
				final float endX = xStartWidget.getValue() + (xAmountWidget.getValue() * MathUtils.cosDeg(angle));
				final float endY = yStartWidget.getValue() + (xAmountWidget.getValue() * MathUtils.sinDeg(angle));
				setupTest(endX, endY);
				return MoveAction.moveByAngleDeg(testObject, angle, xAmountWidget.getValue(), durationWidget.getValue(), Interpolation.exp5Out)
							.addListener(metricsListener)
							.setName("MoveByAngle")
							.reverseBackToStart(reverseBackToStart)
							.setReverse(reverse);
			}
		});
		
		addTest(new ActionTest("MoveByAngleRad") {
			@Override
			public Action<?> run() {
				final float angleDeg = angleWidget.getValue() * MathUtils.radiansToDegrees;
				final float endX = xStartWidget.getValue() + (xAmountWidget.getValue() * MathUtils.cosDeg(angleDeg));
				final float endY = yStartWidget.getValue() + (xAmountWidget.getValue() * MathUtils.sinDeg(angleDeg));
				setupTest(endX, endY);
				return MoveAction.moveByAngleRad(testObject, angleWidget.getValue(), xAmountWidget.getValue(), durationWidget.getValue(), Interpolation.exp5Out)
							.addListener(metricsListener)
							.setName("MoveByAngleRadians")
							.reverseBackToStart(reverseBackToStart)
							.setReverse(reverse);
			}
		});
		
		addTest(new ActionTest("MoveXTo") {
			@Override
			public Action<?> run() {
				setupTest(xEndWidget.getValue(), yStartWidget.getValue());
				return MoveAction.moveXTo(testObject, xEndWidget.getValue(), durationWidget.getValue(), Interpolation.exp5Out)
							.addListener(metricsListener)
							.setName("MoveXto")
							.reverseBackToStart(reverseBackToStart)
							.setReverse(reverse);
			}
		});
		
		addTest(new ActionTest("MoveYTo") {
			@Override
			public Action<?> run() {
				setupTest(xStartWidget.getValue(), yEndWidget.getValue());
				return MoveAction.moveYTo(testObject, yEndWidget.getValue(), durationWidget.getValue(), Interpolation.exp5Out)
							.addListener(metricsListener)
							.setName("MoveYTo")
							.reverseBackToStart(reverseBackToStart)
							.setReverse(reverse);
			}
		});
		
		addTest(new ActionTest("PingPong") {
			@Override
			public Action<?> run() {
				setupTest(xStartWidget.getValue(), yStartWidget.getValue());
				return RepeatAction.repeat(
						MoveAction.moveXBy(testObject, 50, durationWidget.getValue(), Interpolation.linear), 3)
						.pingPong(true);
			}
		});
		
	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		testObject.draw(shapeDrawer);
	}

}
