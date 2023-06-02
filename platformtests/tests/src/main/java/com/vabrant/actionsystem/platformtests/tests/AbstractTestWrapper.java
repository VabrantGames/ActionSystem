
package com.vabrant.actionsystem.platformtests.tests;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import java.util.Arrays;
import java.util.Comparator;

public abstract class AbstractTestWrapper extends PlatformTest {

	Stage stage;
	Skin skin;
	PlatformTest test;
	boolean dispose = false;

	@Override
	public void create () {
		Instancer[] tests = getTestList();
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.app.log("PlatformTest", "Setting up for " + tests.length + " tests.");

		stage = new Stage(new ExtendViewport(480, 320));
		skin = new Skin(Gdx.files.internal("orangepeelui/uiskin.json"));

		Table root = new Table();
		stage.addActor(root);
		root.setFillParent(true);

		Table table = new Table();
		table.pad(10).defaults().expandX().space(4);

		ScrollPane scroll = new ScrollPane(table, skin);
		scroll.getStyle().background = null;
		root.add(scroll).expand().fill();

		Arrays.sort(tests, new Comparator<Instancer>() {
			@Override
			public int compare (Instancer o1, Instancer o2) {
				return o1.getSimpleName().compareTo(o2.getSimpleName());
			}
		});

		for (final Instancer instancer : tests) {
			table.row();
			TextButton button = new TextButton(instancer.getSimpleName(), skin);
			button.addListener(new ChangeListener() {
				@Override
				public void changed (ChangeEvent event, Actor actor) {
					((InputWrapper)Gdx.input).multiplexer.removeProcessor(stage);
					test = instancer.instance();
					Gdx.app.log("ActionSystemTest", "Clicked test - " + test.getClass().getName());
					test.create();
					test.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				}
			});
			table.add(button).expandX().fillX();
		}

		Gdx.input = new InputWrapper(Gdx.input) {
			@Override
			public boolean keyUp (int keycode) {
				if (keycode == Keys.ESCAPE) {
					if (test != null) {
						Gdx.app.log("ActionSystemTest", "Exiting current test.");
						dispose = true;
					}
				}
				return false;
			}
		};
		((InputWrapper)Gdx.input).multiplexer.addProcessor(stage);
	}

	public void render () {
		if (test == null) {
			ScreenUtils.clear(1, 1, 1, 1);
			stage.act(Gdx.graphics.getDeltaTime());
			stage.draw();
		} else {
			if (dispose) {
				test.pause();
				test.dispose();
				test = null;
				stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
				Gdx.graphics.setVSync(true);
				InputWrapper wrapper = ((InputWrapper)Gdx.input);
				wrapper.multiplexer.addProcessor(stage);
				wrapper.multiplexer.removeProcessor(wrapper.lastProcessor);
				wrapper.lastProcessor = null;
				dispose = false;
			} else {
				test.render();
			}
		}
	}

	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
		if (test != null) {
			test.resize(width, height);
		}
	}

	class InputWrapper extends InputAdapter implements Input {
		Input input;
		InputProcessor lastProcessor;
		InputMultiplexer multiplexer;

		public InputWrapper (Input input) {
			this.input = input;
			this.multiplexer = new InputMultiplexer();
			this.multiplexer.addProcessor(this);
			input.setInputProcessor(multiplexer);
		}

		@Override
		public float getAccelerometerX () {
			return input.getAccelerometerX();
		}

		@Override
		public float getAccelerometerY () {
			return input.getAccelerometerY();
		}

		@Override
		public float getAccelerometerZ () {
			return input.getAccelerometerZ();
		}

		@Override
		public float getGyroscopeX () {
			return input.getGyroscopeX();
		}

		@Override
		public float getGyroscopeY () {
			return input.getGyroscopeY();
		}

		@Override
		public float getGyroscopeZ () {
			return input.getGyroscopeZ();
		}

		@Override
		public int getMaxPointers () {
			return input.getMaxPointers();
		}

		@Override
		public int getX () {
			return input.getX();
		}

		@Override
		public int getX (int pointer) {
			return input.getX(pointer);
		}

		@Override
		public int getDeltaX () {
			return input.getDeltaX();
		}

		@Override
		public int getDeltaX (int pointer) {
			return input.getDeltaX(pointer);
		}

		@Override
		public int getY () {
			return input.getY();
		}

		@Override
		public int getY (int pointer) {
			return input.getY(pointer);
		}

		@Override
		public int getDeltaY () {
			return input.getDeltaY();
		}

		@Override
		public int getDeltaY (int pointer) {
			return input.getDeltaY(pointer);
		}

		@Override
		public boolean isTouched () {
			return input.isTouched();
		}

		@Override
		public boolean justTouched () {
			return input.justTouched();
		}

		@Override
		public boolean isTouched (int pointer) {
			return input.isTouched(pointer);
		}

		@Override
		public float getPressure () {
			return input.getPressure();
		}

		@Override
		public float getPressure (int pointer) {
			return input.getPressure(pointer);
		}

		@Override
		public boolean isButtonPressed (int button) {
			return input.isButtonPressed(button);
		}

		@Override
		public boolean isKeyPressed (int key) {
			return input.isKeyPressed(key);
		}

		@Override
		public boolean isKeyJustPressed (int key) {
			return input.isKeyJustPressed(key);
		}

		@Override
		public boolean isButtonJustPressed (int button) {
			return input.isButtonJustPressed(button);
		}

		@Override
		public void getTextInput (TextInputListener listener, String title, String text, String hint) {
			input.getTextInput(listener, title, text, hint);
		}

		@Override
		public void getTextInput (TextInputListener listener, String title, String text, String hint, OnscreenKeyboardType type) {
			input.getTextInput(listener, title, text, hint, type);
		}

		@Override
		public void setOnscreenKeyboardVisible (boolean visible) {
			input.setOnscreenKeyboardVisible(visible);
		}

		@Override
		public void setOnscreenKeyboardVisible (boolean visible, OnscreenKeyboardType type) {
			input.setOnscreenKeyboardVisible(visible, type);
		}

		@Override
		public void vibrate (int milliseconds) {
			input.vibrate(milliseconds);
		}

		@Override
		public void vibrate (long[] pattern, int repeat) {
			input.vibrate(pattern, repeat);
		}

		@Override
		public void cancelVibrate () {
			input.cancelVibrate();
		}

		@Override
		public float getAzimuth () {
			return input.getAzimuth();
		}

		@Override
		public float getPitch () {
			return input.getPitch();
		}

		@Override
		public float getRoll () {
			return input.getRoll();
		}

		@Override
		public void getRotationMatrix (float[] matrix) {
			input.getRotationMatrix(matrix);
		}

		@Override
		public long getCurrentEventTime () {
			return input.getCurrentEventTime();
		}

		@Override
		public void setCatchBackKey (boolean catchBack) {
			input.setCatchBackKey(catchBack);
		}

		@Override
		public boolean isCatchBackKey () {
			return input.isCatchBackKey();
		}

		@Override
		public void setCatchMenuKey (boolean catchMenu) {
			input.setCatchMenuKey(catchMenu);
		}

		@Override
		public boolean isCatchMenuKey () {
			return input.isCatchMenuKey();
		}

		@Override
		public void setCatchKey (int keycode, boolean catchKey) {
			input.setCatchKey(keycode, catchKey);
		}

		@Override
		public boolean isCatchKey (int keycode) {
			return input.isCatchKey(keycode);
		}

		@Override
		public void setInputProcessor (InputProcessor processor) {
			multiplexer.removeProcessor(lastProcessor);
			multiplexer.addProcessor(processor);
			lastProcessor = processor;
		}

		@Override
		public InputProcessor getInputProcessor () {
			return input.getInputProcessor();
		}

		@Override
		public boolean isPeripheralAvailable (Peripheral peripheral) {
			return input.isPeripheralAvailable(peripheral);
		}

		@Override
		public int getRotation () {
			return input.getRotation();
		}

		@Override
		public Orientation getNativeOrientation () {
			return input.getNativeOrientation();
		}

		@Override
		public void setCursorCatched (boolean catched) {
			input.setCursorCatched(catched);
		}

		@Override
		public boolean isCursorCatched () {
			return input.isCursorCatched();
		}

		@Override
		public void setCursorPosition (int x, int y) {
			input.setCursorPosition(x, y);
		}
	}

	protected interface Instancer {
		PlatformTest instance ();

		String getSimpleName ();
	}

	protected abstract Instancer[] getTestList ();
}
