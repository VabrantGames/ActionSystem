package com.vabrant.actionsystem.test.tests;

import java.util.Iterator;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionManager;
import com.vabrant.actionsystem.actions.ActionPools;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class ActionSystemTestListener extends ApplicationAdapter implements InputProcessor {
	
	private boolean isTestRunning;
	private float testDelayTimer = 0;
	private final float testDelayDuration = 0.5f;
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	public AssetManager assetManager;
	public ActionManager actionManager;
	public ShapeDrawer shapeDrawer;
	public Texture pixelTexture;
	public Viewport hudViewport;
	private Stage stage;
	private Skin skin;
	private ObjectMap<String, ActionTest> tests;
	private Action<?> actionToRun;
	private String selectedTest;
	private Label isRunningLabel;
	private Label fpsLabel;
	
	private ActionAdapter testOverListener = new ActionAdapter() {
		@Override
		public void actionEnd(Action a) {
			resetCurrentTest();
		}
	};
	
	@Override
	public void create() {	
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		tests = new ObjectMap<>();
		
		hudViewport = new ScreenViewport();
		batch = new SpriteBatch();
		
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.drawPixel(0, 0);
		
		pixelTexture = new Texture(pixmap);
		
		shapeDrawer = new ShapeDrawer(batch, new TextureRegion(pixelTexture));
		shapeDrawer.setDefaultSnap(false);
		
		actionManager = new ActionManager();
		assetManager = new AssetManager();
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		
		createTests();
		
		stage = new Stage(new ScreenViewport(), batch);
//		stage.setDebugAll(true);
		skin = new Skin(Gdx.files.internal("orangepeelui/uiskin.json"));

		Table selectBoxTable = new Table();
		selectBoxTable.setFillParent(true);
		selectBoxTable.center().top();
		SelectBox<String> selectBox = new SelectBox<>(new SelectBoxStyle(skin.get(SelectBoxStyle.class)));
		Iterator<String> i = tests.keys();
		Array<String> items = new Array<>();
		while(i.hasNext()) {
			items.add(i.next());
		}
		
		selectBoxTable.add(selectBox);
		selectBox.setItems(items);
		selectBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				selectedTest = selectBox.getSelected();
			}
		});
		
//		if(selectBox.getItems().size > 0) selectBox.setSelectedIndex(0);
//		selectBox.setSelected(null);
		stage.addActor(selectBoxTable);
		
		Table defaultStatsTable = new Table();
		defaultStatsTable.setFillParent(true);
		defaultStatsTable.right().top();
		
		isRunningLabel = new Label("Not Running", new LabelStyle(skin.get(LabelStyle.class)));
		isRunningLabel.getStyle().fontColor = Color.BLACK;
		defaultStatsTable.add(isRunningLabel).width(100);
		
		fpsLabel = new Label("", skin);
		
		stage.addActor(defaultStatsTable);
		
		Table statsRoot = new Table();
		statsRoot.setFillParent(true);
		statsRoot.pad(4).left().top();
		createHud(statsRoot, skin);
		stage.addActor(statsRoot);
		stage.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				switch(keycode) {
					case Keys.ESCAPE:
						stage.setKeyboardFocus(null);
						break;
					case Keys.SPACE:
						if(selectedTest == null) return false;
						
						if(isTestRunning) resetCurrentTest();
						isTestRunning = true;
						ActionTest test = tests.get(selectedTest);
						actionToRun = test.run();
						actionToRun.addListener(testOverListener);
						isRunningLabel.setText("Running");
						isRunningLabel.getStyle().fontColor = Color.RED;
						break;
				}
				return super.keyDown(event, keycode);
			}
		});
		
		Gdx.input.setInputProcessor(stage);
	}

	private void resetCurrentTest() {
		if(isTestRunning) {
			isTestRunning = false;
			actionManager.freeAll(false);
		}
		
		if(actionToRun != null) {
			ActionPools.free(actionToRun);
			actionToRun = null;
		}
		
		isRunningLabel.setText("Not Running");
		isRunningLabel.getStyle().fontColor = Color.BLACK;
		testDelayTimer = 0;
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		hudViewport.update(width, height, true);
	}

	@Override
	public void dispose() {
		batch.dispose();
		assetManager.dispose();
		pixelTexture.dispose();
		stage.dispose();
		skin.dispose();
	}
	
	public void addTest(ActionTest test) {
		tests.put(test.name, test);
	}
	
	public void reset() {
		resetCurrentTest();
	}
	
	public void createTests() {}
	public void createHud(Table root, Skin skin) {}
	public void update(float delta) {}
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {}
	public void drawWithShapeRenderer(ShapeRenderer renderer) {}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		float delta = Gdx.graphics.getDeltaTime();
		
		if(isTestRunning) {
			if(actionToRun != null) {
				if((testDelayTimer += delta) >= testDelayDuration) {
					actionManager.addAction(actionToRun);
					actionToRun = null;
					testDelayTimer = 0;
				}
			}
		}
		
		actionManager.update(delta);
		stage.act(delta);
		update(delta);

		batch.setProjectionMatrix(hudViewport.getCamera().combined);
		batch.enableBlending();
		batch.begin();
		draw(batch, shapeDrawer);
		shapeDrawer.rectangle(0, 0, hudViewport.getWorldWidth() - 1, hudViewport.getWorldHeight() - 1, Color.GREEN);
		batch.end();
		
		stage.getViewport().apply();
		stage.draw();
		
		shapeRenderer.setProjectionMatrix(hudViewport.getCamera().combined);
		shapeRenderer.begin();
		drawWithShapeRenderer(shapeRenderer);
		shapeRenderer.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	public static class LabelTextFieldWidget {
		
		private boolean allowNegativeValues;
		Label label;
		TextField textField;
		
		public LabelTextFieldWidget(String labelTitle, Skin skin, Table table) {
			label = new Label(labelTitle, skin);
			textField = new TextField("", skin);
			table.add(label).left();
			table.add(textField).width(100).padRight(0);
		}
		
		public void setAllowNegativeValues(boolean allowNegativeValues) {
			this.allowNegativeValues = allowNegativeValues;
		}
		
		public boolean allowNegativeValues() {
			return allowNegativeValues;
		}
	}
	
	public static class LabelTextFieldIntWidget extends LabelTextFieldWidget {
		
		public LabelTextFieldIntWidget(String labelTitle, Skin skin, Table table, int startValue) {
			super(labelTitle, skin, table);
			textField.setText(String.valueOf(startValue));
			textField.setTextFieldFilter(new TextFieldFilter() {
				@Override
				public boolean acceptChar(TextField textField, char c) {
					return Character.isDigit(c);
				}
			});
		}
		
		public void setValue(int value) {
			textField.setText(String.valueOf(value));
		}
		
		public int getValue() {
			if(textField.getText().isEmpty()) return 0;
			return Integer.parseInt(textField.getText());
		}
	}
	
	public static class LabelTextFieldFloatWidget extends LabelTextFieldWidget {
		
		public LabelTextFieldFloatWidget(String labelTitle, Skin skin, Table table, float startValue) {
			super(labelTitle, skin, table);
			textField.setText(String.valueOf(startValue));
			textField.setTextFieldFilter(new TextFieldFilter() {
				@Override
				public boolean acceptChar(TextField textField, char c) {
					if(!Character.isDigit(c)) {
						switch(c) {
							case '.':
								if(textField.getText().contains(".")) return false;
								break;
							case '-':
								if(!allowNegativeValues()) return false;
								if(textField.getText().length() > 1) {
									if(textField.getCursorPosition() != 0) return false;
									
									char fc = textField.getText().charAt(0);
									if(fc == '-') return false;
								}
								break;
							default:
								return false;
						}
					}
					return true;
				}
			});
		}
		
		public void setValue(int value) {
			textField.setText(String.valueOf(value));
		}
		
		public float getValue() {
			if(textField.getText().isEmpty()) return 0;
			return Float.parseFloat(textField.getText());
		}
	}
	
	public static class DoubleLabelWidget {
		
		Label titleLabel;
		Label valueLabel;
		
		public DoubleLabelWidget(String labelTitle, Skin skin, Table table) {
			titleLabel = new Label(labelTitle, skin);
			valueLabel = new Label("0.0", new LabelStyle(skin.get(LabelStyle.class)));
			valueLabel.getStyle().fontColor = Color.BLACK;
			table.add(titleLabel).left();
			table.add(valueLabel).width(100);
		}
		
		public void setValue(Number number) {
			valueLabel.setText(String.valueOf(number.floatValue()));
		}
	}
	
	public static class LabelCheckBoxWidget {
		
		Label titleLabel;
		CheckBox checkBox;
			   
		public LabelCheckBoxWidget(String title, Skin skin, Table table) {
			titleLabel = new Label(title, skin);
			checkBox = new CheckBox("", skin);
			table.add(titleLabel).left();
			table.add(checkBox).width(100);
		}
		
		public boolean isChecked() {
			return checkBox.isChecked();
		}
	}

	public static abstract class ActionTest {
		
		public final String name;
		
		public ActionTest(String name) {
			this.name = name;
		}
		
		public abstract Action<?> run();
	}

}
