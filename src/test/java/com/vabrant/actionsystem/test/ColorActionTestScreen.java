package com.vabrant.actionsystem.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.ColorAction;
import com.vabrant.actionsystem.RepeatAction;
import com.vabrant.testbase.Test;
import com.vabrant.testbase.TestSelectScreen;

public class ColorActionTestScreen extends ActionSystemTestScreen {
	
	private final Class[] tests = {
			ChangeAlphaTest.class,
			ChangeHueTest.class,
			ChangeColorRGBATest.class,
			ChangeColorHSBATest.class,
			SetColorTest.class
			};
	ActionSystemTestObject testObject;
	
	public ColorActionTestScreen(TestSelectScreen screen) {
		super(screen);
		
		testObject = new ActionSystemTestObject();
		testObject.width = 100;
		testObject.height = 100;
		testObject.setColor(Color.BLACK);
		
		addTests(tests);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		float centerX = (viewport.getWorldWidth() - testObject.width) / 2;
		float centerY = (viewport.getWorldHeight() - testObject.height) / 2;
		testObject.setX(centerX);
		testObject.setY(centerY);
	}
	
	@Override
	public void debug(ShapeRenderer renderer) {
		super.debug(renderer);
		renderer.set(ShapeType.Filled);
		renderer.setColor(testObject.getColor());
		renderer.rect(testObject.getX(), testObject.getY(), testObject.width, testObject.height);
	}
	
	public class ChangeColorRGBATest implements Test{
		
		private Color startColor = new Color(Color.BLACK);
		private Color endColor = new Color(Color.RED);
		
		public ChangeColorRGBATest() {
			reset();
		}

		@Override
		public void runTest1() {
			actionManager.addAction(ColorAction.changeColor(testObject, Color.RED, 1f, false, Interpolation.linear));
		}

		@Override
		public void check1() {
			System.out.println("EndR: " + endColor.r);
			System.out.println("EndG: " + endColor.g);
			System.out.println("EndB: " + endColor.b);
			System.out.println("EndA: " + endColor.a);
			System.out.println("currentR: " + testObject.getColor().r);
			System.out.println("currentG: " + testObject.getColor().g);
			System.out.println("currentB: " + testObject.getColor().b);
			System.out.println("currentA: " + testObject.getColor().a);
		}
		
		@Override
		public void runTest2() {
			actionManager.addAction(RepeatAction.repeat(ColorAction.changeColor(testObject, Color.RED, 1, false, Interpolation.linear), 3));
		}
		
		@Override
		public void reset() {
			testObject.setColor(Color.BLACK);
		}
	}
	
	public class ChangeColorHSBATest implements Test{
		
		private final float[] startHSB = new float[] {130f, 0.25f, 0.9f};
		private final float[] endHSB = new float[] {0f, 0.25f, 0.59f};
		
		public ChangeColorHSBATest() {
			reset();
		}
		
		@Override
		public void runTest1() {
			actionManager.addAction(ColorAction.changeColor(testObject, endHSB[0], endHSB[1], endHSB[2], 1, 2f, false, Interpolation.linear));
		}
		
		@Override
		public void runTest2() {
			actionManager.addAction(RepeatAction.repeat(ColorAction.changeColor(testObject, endHSB[0], endHSB[1], endHSB[2], 1, 2f, true, Interpolation.linear), 3));
		}
		
		@Override
		public void check1() {
			Gdx.app.log(this.getClass().getSimpleName(), "hue: " + ColorAction.getHue(testObject.getColor()));
			Gdx.app.log(this.getClass().getSimpleName(), "saturation: " + ColorAction.getSaturation(testObject.getColor()));
			Gdx.app.log(this.getClass().getSimpleName(), "brightness: " + ColorAction.getBrightness(testObject.getColor()));
		}
		
		@Override
		public void reset() {
			ColorAction.HSBToRGB(testObject.getColor(), startHSB[0], startHSB[1], startHSB[2], 1f);
		}
	}
	
	public class ChangeAlphaTest implements Test{
		
		public ChangeAlphaTest() {
			reset();
		}
		
		@Override
		public void runTest1() {
			actionManager.addAction(ColorAction.changeAlpha(testObject, 0, 1f, false, Interpolation.linear));
		}
		
		@Override
		public void runTest2() {
			actionManager.addAction(RepeatAction.repeat(ColorAction.changeAlpha(testObject, 0, 2f, false, Interpolation.linear), 3));
		}
		
		@Override
		public void reset() {
			testObject.getColor().a = 1;
		}
	}
	
	public class ChangeHueTest implements Test{

		public ChangeHueTest() {
			reset();
		}
		
		@Override
		public void runTest1() {
			actionManager.addAction(ColorAction.changeHue(testObject, 360f, 2f, true, Interpolation.linear));
		}
		
		@Override
		public void runTest2() {
			actionManager.addAction(RepeatAction.repeat(ColorAction.changeHue(testObject, 150, 2f, true, Interpolation.fade), 3));
		}
		
		@Override
		public void reset() {
			ColorAction.HSBToRGB(testObject.getColor(), 0, 1f, 1f, 1);
		}
	}
	
	public class SetColorTest implements Test{
		
		private final Color endColor = new Color(254f/255f, 136f/255f, 172f/255f, 1f);
		private final float hue = 342;
		private final float saturation = 0.46f;
		private final float brightness = 1f;
		
		public SetColorTest() {
			reset();
		}
		
		@Override
		public void reset() {
			testObject.setColor(Color.BLACK);
		}
		
		@Override
		public void runTest1() {
			actionManager.addAction(ColorAction.setColor(testObject, endColor));
		}
		
		@Override
		public void runTest2() {
			actionManager.addAction(ColorAction.setColor(testObject, hue, saturation, brightness, 1f));
		}
	}
}
