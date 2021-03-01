package com.vabrant.actionsystem.test.actiontester;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ActionableView extends Table {
	
	static class ActionableList extends Table {
		
		private class Input extends InputAdapter {
			
			private boolean bypass;
			private Vector2 temp;
			private ActionableList list;
			private Stage stage;
			
			private Input(ActionableList list, Stage stage) {
				this.list = list;
				this.stage = stage;
				temp = new Vector2();
			}
			
			void bypass(boolean bypass) {
				this.bypass = bypass;
			}
			
			@Override
			public boolean keyDown(int keycode) {
				if(bypass) return false;
				
				switch(keycode) {
					case Keys.ESCAPE:
						list.unselect();
						return true;
				}
				
				return false;
			}
			
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				if(bypass) return false;
				
				stage.getViewport().unproject(temp.set(screenX, screenY));
				
				float touchX = temp.x;
				float touchY = temp.y;
				
				list.localToStageCoordinates(temp.set(0, 0));

				float listX = temp.x;
				float listY = temp.y;
				float listWidth = list.getWidth();
				float listHeight = list.getHeight();
				
				if(touchX >= listX && touchX <= (listX + listWidth) && touchY >= listY && touchY <= (listY + listHeight)) {
					Array<Cell> cells = getCells();
					for(int i = 0; i < cells.size; i++) {
						Actor actor = cells.get(i).getActor();
						
						actor.localToStageCoordinates(temp.set(0, 0));
						                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
						float x = temp.x;
						float y = temp.y;
						float width = actor.getWidth();
						float height = actor.getHeight();
						
						if(touchX >= x && touchX <= (x + width) && touchY >= y && touchY <= (y + height)) {
							list.setSelectedActionableEntry(x, y, (ActionableEntry)actor);
							return true;
						}
					}
				}
				else {
					list.unselect();
				}
				
				return false;
			}
		}
		
		private Array<ActionableEntry> entries;
		private Pool<ActionableEntry> pool;
		private Input input;
		private TextureRegionDrawable selectedDrawable;
		private Vector2 selectedPosition;
		private ActionableEntry selectedEntry;
		
		ActionableList(Skin skin, Stage stage) {
			setDebug(true);
			top();
			entries = new Array<>(20);
			input = new Input(this, stage);
			selectedDrawable = ((TextureRegionDrawable)skin.getDrawable("maroon"));
			selectedPosition = new Vector2();
			
			pool = new Pool<ActionableEntry>() {
				@Override
				protected ActionableEntry newObject() {
					return new ActionableEntry(skin);
				}
			};
		}
		
		void setSelectedActionableEntry(float x, float y, ActionableEntry entry) {
			selectedEntry = entry;
			selectedPosition.set(x, y);
		}
		
		void unselect() {
			selectedEntry = null;
		}
		
		void bypassInput(boolean bypass) {
			input.bypass(bypass);
		}
		
		public InputAdapter getInput() {
			return input;
		}
		
		ActionableEntry obtainActionableEntry() {
			ActionableEntry entry = pool.obtain();
			entry.setActionableList(this);
			return entry;
		}
		
		boolean containsName(String name) {
			for(ActionableEntry e : entries) {
				if(e.getName().equals(name)) return true;
			}
			return false;
		}

		boolean addEntry(ActionableEntry entry) {
			entries.add(entry);
			add(entry).growX();
			row();
			return true;
		}
		
		void removeEntry(ActionableEntry entry) {
			pool.free(entries.removeIndex(entries.indexOf(entry, false)));
			recreateTable();
		}
		
		private void recreateTable() {
			clearChildren();
			
			for(int i = 0; i < entries.size; i++) {
				add(entries.get(i)).growX();
				row();
			}
			
			layout();
		}
		
		@Override
		public void draw(Batch batch, float parentAlpha) {
			if(selectedEntry != null) {
				selectedDrawable.draw(batch, selectedPosition.x, selectedPosition.y, selectedEntry.getWidth(), selectedEntry.getHeight());
			}
			super.draw(batch, parentAlpha);
		}
	}
	
	private static class ActionableEntry extends Table implements Poolable {
		
		private static ImageButtonStyle DELETE_BUTTON_STYLE;
		
		public static void setupImageButtonStyle(Skin skin) {
			DELETE_BUTTON_STYLE = new ImageButtonStyle();
			DELETE_BUTTON_STYLE.imageUp = ((TextureRegionDrawable)skin.getDrawable("image-delete")).tint(Color.RED);
			DELETE_BUTTON_STYLE.imageDown = skin.getDrawable("image-delete-down");
		}

		private Label label;
		private ImageButton deleteButton;
		private ActionableList actionableList;
		
		ActionableEntry(Skin skin) {
//			setDebug(true);
			pad(10);
			
			add(label = new Label("", skin)).growX().fillY();
			add(deleteButton = new ImageButton(DELETE_BUTTON_STYLE)).expandX().right();

			final ActionableEntry entry = this;
			deleteButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					actionableList.removeEntry(entry);
				}
			});
		}
		
		void setActionableList(ActionableList actionableList) {
			this.actionableList = actionableList;
		}
		
		ActionableEntry setup(String name) {
			setName(name);
			label.setText(name);
			return this;
		}
		
		@Override
		public void reset() {
			label.setText("");
			setName("");
			actionableList = null;
		}
	}
	
	private class CreateActionableDialog extends Window {
		
		private ActionableView actionableView;
		private Actor previousKeyboardFocus;
		private Actor previousScrollFocus;
		private Skin skin;
		private TextField nameField;
		private Stage stage;
		
		public CreateActionableDialog(ActionableView actionableView, Stage stage, Skin skin) {
			super("Create Actionable", new WindowStyle(skin.get(WindowStyle.class)));
			this.actionableView = actionableView;
			this.stage = stage;
			this.skin = skin;
			
			setModal(true);
			setResizable(false);
			clearListeners();
			getStyle().stageBackground = ((TextureRegionDrawable)skin.getDrawable("bleached-peach")).tint(new Color(1, 1, 1, 0.5f));
			
			Table contentTable = new Table(skin);
//			contentTable.setDebug(true);
			contentTable.center();
			
			//Name stuff
			Table nameTable = new Table();
			Label nameLabel = new Label("Name", skin);
			nameTable.add(nameLabel).padRight(5f);
			
			nameField = new TextField("", skin);
			nameTable.add(nameField);
			
			contentTable.add(nameTable).padBottom(5f);
			
			contentTable.row();
			TextButton createButton = new TextButton("Create", skin);
			createButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					submit();
				}
			});
			contentTable.add(createButton);
			
			addListener(new FocusListener() {
				@Override
				public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
					if(focused) {
						stage.setKeyboardFocus(nameField);
					}
				}
				
				@Override
				public void scrollFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				}
			});
			
			addListener(new InputListener() {
				@Override
				public boolean keyDown(InputEvent event, int keycode) {
					switch(keycode) {
						case Keys.ESCAPE:
							hide();
							return true;
						case Keys.ENTER:
							submit();
							return true;
					}
					return false;
				}
				
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					if(x < 0 || x > getWidth() || y < 0 || y > getHeight()) hide();
					return false;
				}
			});
			
			add(contentTable).pad(5f);
		}
		
		private void submit() {
			ActionableList actionableList = actionableView.actionableList;
			
			if(actionableList.containsName(nameField.getText())) return;
			
			ActionableEntry entry = actionableList.obtainActionableEntry();
			entry.setup(nameField.getText());
			
			actionableList.addEntry(entry);
			
			hide();
			nameField.setText("");
		}
		
		public void show() {
			previousKeyboardFocus = stage.getKeyboardFocus();
			previousScrollFocus = stage.getScrollFocus();
			
			actionableView.getActionableList().bypassInput(true);
			
			stage.addActor(this);
			pack();
			stage.cancelTouchFocus();
			stage.setKeyboardFocus(this);
			stage.setScrollFocus(this);
			
			setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
		}
		
		public void hide() {
			actionableView.getActionableList().bypassInput(false);
			if(previousKeyboardFocus != null) stage.setKeyboardFocus(previousKeyboardFocus);
			if(previousScrollFocus != null) stage.setScrollFocus(previousScrollFocus);
			remove();
		}
		
	}
	
	private Skin skin;
	private CreateActionableDialog createActionableDialog;
	private ActionableList actionableList;
	
	public ActionableView(Stage stage, Skin skin) {
		super(skin);
//		setDebug(true);
		
		ActionableEntry.setupImageButtonStyle(skin);
		
		top();
		this.skin = skin;
		setName("ActionableView");
		setBackground("panel-orange");
		
		actionableList = new ActionableList(skin, stage);
		createActionableDialog = new CreateActionableDialog(this, stage, skin);
		
		TextButton createActionableButton = new TextButton("Create", skin);
		createActionableButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				createActionableDialog.show();
			}
		});
		add(createActionableButton);
		
		row();
		add(actionableList).grow().padTop(5f);

	}
	
	public ActionableList getActionableList() {
		return actionableList;
	}

}
