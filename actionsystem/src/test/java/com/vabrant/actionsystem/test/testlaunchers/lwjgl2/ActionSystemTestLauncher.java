package com.vabrant.actionsystem.test.testlaunchers.lwjgl2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.vabrant.actionsystem.test.tests.ActionSystemTests;

public class ActionSystemTestLauncher extends ApplicationAdapter {
	
	public static int SELECTION_WIDTH = 400; 

	public static void main(String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(600, 600);
		config.setIdleFPS(0);
		config.useVsync(false);
		config.setTitle("ActionSystemTestLauncher");
		
		new Lwjgl3Application(new ActionSystemTestLauncher(), config);
	}
	
	private Stage stage;
	private Skin skin;
	
	@Override
	public void create() {
		stage = new Stage(new ScreenViewport());
//		stage.setDebugAll(true);
		
		Gdx.input.setInputProcessor(stage);
		
		skin = new Skin(Gdx.files.internal("orangepeelui/uiskin.json")); 
		
		Table root = new Table();
		stage.addActor(root);
		root.setFillParent(true);
		
		Table table = new Table();
		table.pad(4).defaults().expandX().space(4);
		
		ScrollPane scrollPane = new ScrollPane(table, skin);
		scrollPane.setFadeScrollBars(false);
		scrollPane.setSmoothScrolling(false);
		scrollPane.getStyle().background = null;
		stage.setScrollFocus(scrollPane);
		
		for(Class<? extends ApplicationListener> c : ActionSystemTests.TEST_CLASSES) {
			
			TextButton button = new TextButton(c.getSimpleName(), skin);
			
			button.addListener(new ClickListener() {
				public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
					final String name = c.getSimpleName();
					ApplicationListener listener = ActionSystemTests.createTestApplicationListener(name);
					
					if(listener == null) {
						System.err.println("No test found.");
						return;
					}
					
					Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
					config.setWindowedMode(600 + SELECTION_WIDTH, 600);
					config.setTitle(name);
					config.useVsync(false);
					config.setResizable(false);
					
					Lwjgl3Graphics mainGraphics = (Lwjgl3Graphics)Gdx.graphics;
					config.setWindowPosition(mainGraphics.getWindow().getPositionX() + 50, mainGraphics.getWindow().getPositionY() + 50);

					((Lwjgl3Application)Gdx.app).newWindow(listener, config);
				}
			});
			
			table.add(button).height(40).fillX();
//			table.add(button).fillX();
			table.row();
		}

		root.add(scrollPane).left().expand().fillX().top();
		
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}
	
}
