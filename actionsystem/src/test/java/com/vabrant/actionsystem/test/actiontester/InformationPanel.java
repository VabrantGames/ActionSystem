package com.vabrant.actionsystem.test.actiontester;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class InformationPanel extends Table {
	
	private ActionableView actionableView;
	
	public InformationPanel(Stage stage, Table rootTable, Skin skin) {
		super(skin);
		background("panel-orange");
//		setDebug(true);
		
		top();
		
		Table buttonsTable = new Table(skin);
		buttonsTable.background("panel-orange");
//		buttonsTable.defaults().minWidth(100f);
		TextButton actionablesButton = new TextButton("Actionables", skin);
		buttonsTable.add(actionablesButton).padRight(5f);
		TextButton actionsButton = new TextButton("Actions", skin);
		buttonsTable.add(actionsButton).padRight(5f);
		TextButton attributesButton = new TextButton("Attributes", skin);
		buttonsTable.add(attributesButton).padRight(5f);
		TextButton queueButton = new TextButton("Queue", skin);
		buttonsTable.add(queueButton);
		add(buttonsTable).padBottom(5f);
		
		row();
		actionableView = new ActionableView(stage, skin);
		add(actionableView).grow().left().top();

		addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if(keycode == Keys.UP) {
					System.out.println("set input");
//					Gdx.input.setInputProcessor(actionableView.getActionableList().getInput());
				}

				return false;
			}
		});

	}
	
	public ActionableView getActionableView() {
		return actionableView;
	}
	

}
