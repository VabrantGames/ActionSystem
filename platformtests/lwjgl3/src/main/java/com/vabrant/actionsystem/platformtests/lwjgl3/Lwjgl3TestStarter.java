
package com.vabrant.actionsystem.platformtests.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowConfiguration;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.vabrant.actionsystem.platformtests.tests.PlatformTests;

public class Lwjgl3TestStarter {

	public static void main (String[] argv) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(640, 480);
		new Lwjgl3Application(new TestChooser(), config);
	}

	static class TestChooser extends ApplicationAdapter {
		private Stage stage;
		private Skin skin;

		public void create () {
			System.out.println("OpenGL renderer: " + Gdx.graphics.getGLVersion().getRendererString());
			System.out.println("OpenGL vendor: " + Gdx.graphics.getGLVersion().getVendorString());

			stage = new Stage(new ScreenViewport());
			Gdx.input.setInputProcessor(stage);
			skin = new Skin(Gdx.files.internal("orangepeelui/uiskin.json"));

			Table container = new Table();
			stage.addActor(container);
			container.setFillParent(true);

			Table table = new Table();

			ScrollPane scroll = new ScrollPane(table, skin);
			scroll.setSmoothScrolling(false);
			scroll.setFadeScrollBars(false);
			stage.setScrollFocus(scroll);

			int tableSpace = 4;
			table.pad(10).defaults().expandX().space(tableSpace);
			for (final String testName : PlatformTests.getNames()) {
				final TextButton testButton = new TextButton(testName, skin);
				testButton.setName(testName);
				table.add(testButton).fillX();
				table.row();
				testButton.addListener(new ChangeListener() {
					@Override
					public void changed (ChangeEvent event, Actor actor) {
						ApplicationListener test = PlatformTests.newTest(testName);
						Lwjgl3WindowConfiguration winConfig = new Lwjgl3WindowConfiguration();
						winConfig.setTitle(testName);
						winConfig.setWindowedMode(640, 480);
						winConfig.setWindowPosition(((Lwjgl3Graphics)Gdx.graphics).getWindow().getPositionX() + 40,
							((Lwjgl3Graphics)Gdx.graphics).getWindow().getPositionY() + 40);
						winConfig.useVsync(false);
						((Lwjgl3Application)Gdx.app).newWindow(test, winConfig);
						System.out.println("Started test: " + testName);
					}
				});
			}

			container.add(scroll).expand().fill();
			container.row();
		}

		@Override
		public void render () {
			ScreenUtils.clear(0, 0, 0, 1);
			stage.act();
			stage.draw();
		}

		@Override
		public void resize (int width, int height) {
			stage.getViewport().update(width, height, true);
		}

		@Override
		public void dispose () {
			skin.dispose();
			stage.dispose();
		}
	}
}
