package com.vabrant.actionsystem.test.actiontester;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ActionTester extends ApplicationAdapter {
	
	 enum Bob {
		billy;
	}
	
	private static final int PADDING = 50;
	public static final int PANEL_WIDTH = 400;
	public static final int VIEW_WIDTH = 1024;
	public static final int VIEW_HEIGHT = 768;
	
	public static void main(String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(VIEW_WIDTH + PANEL_WIDTH + PADDING, VIEW_HEIGHT + PADDING);
		config.setIdleFPS(0);
		config.useVsync(false);
		config.setResizable(false);
		config.setTitle("ActionTester");
		
		new Lwjgl3Application(new ActionTester(), config);
	}

	private Stage stage;
	private Skin skin;
	private InformationPanel informationPanel;
	private RenderPanel renderPanel;
	
	@Override
	public void create() {
		super.create();
		
		skin = new Skin(Gdx.files.internal("orangepeelui/uiskin.json")); 
		stage = new Stage(new ScreenViewport());
		
		Table rootTable = new Table();
		rootTable.setFillParent(true);
		rootTable.defaults().pad(10f);
//		rootTable.setDebug(true);
		stage.addActor(rootTable);
		
		informationPanel = new InformationPanel(stage, rootTable, skin);
		renderPanel = new RenderPanel(stage, skin);
		
		rootTable.add(informationPanel).expand().left().minWidth(ActionTester.PANEL_WIDTH).minHeight(ActionTester.VIEW_HEIGHT).maxWidth(ActionTester.PANEL_WIDTH).maxHeight(ActionTester.VIEW_HEIGHT);
		rootTable.add(renderPanel).minWidth(VIEW_WIDTH).minHeight(VIEW_HEIGHT);
		
		stage.setKeyboardFocus(informationPanel);
		
		InputMultiplexer input = new InputMultiplexer();
		input.addProcessor(informationPanel.getActionableView().getActionableList().getInput());
		input.addProcessor(stage);
		Gdx.input.setInputProcessor(input);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

}
