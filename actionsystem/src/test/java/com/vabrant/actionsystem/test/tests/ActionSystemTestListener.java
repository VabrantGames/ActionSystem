package com.vabrant.actionsystem.test.tests;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionManager;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class ActionSystemTestListener extends ApplicationAdapter implements InputProcessor {
	
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
	private TextButton currentTestTextButton;
	
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
		
		hudViewport = new FitViewport(600, 600);
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
		stage.setDebugAll(true);
		skin = new Skin(Gdx.files.internal("orangepeelui/uiskin.json"));
		
		Table root = new Table();
		stage.addActor(root);
		root.setWidth(400);
		root.setHeight(600);
		
		Table table = new Table();
		table.pad(4).defaults().growX().space(4);
		
		ScrollPane scrollPane = new ScrollPane(table, skin);
		scrollPane.setFadeScrollBars(false);
		scrollPane.setSmoothScrolling(false);
		stage.setScrollFocus(scrollPane);
		
		TextButton resetButton = new TextButton("reset", skin);
		resetButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				reset();
				resetCurrentTest();
				actionManager.freeAll(false);
			}
		});
		table.add(resetButton).height(40);
		table.row();
		
		for(Entry<String, ActionTest> e : tests) {
			final TextButton button = new TextButton(e.key, skin);
			
			button.addListener(new ClickListener() {
				ActionTest test = e.value;
				
				@Override
				public void clicked(InputEvent event, float x, float y) {
//					if(currentTestTextButton != null) resetCurrentTest();
					
					currentTestTextButton = button;
					currentTestTextButton.setColor(Color.RED);
					
					test.run().addListener(testOverListener);
				}
			});
			
			table.add(button).height(40);
			table.row();
		}
		
		root.add(scrollPane).left().expand().fillX().top();
		
		Gdx.input.setInputProcessor(stage);
	}
	
	private void resetCurrentTest() {
		if(currentTestTextButton == null) return;
		currentTestTextButton.setColor(Color.WHITE);
		currentTestTextButton = null;
	}
	
	@Override
	public void resize(int width, int height) {
		hudViewport.update(width, height, true);
		
		hudViewport.setScreenX(400);
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
	
	public void reset() {}
	public void createTests() {}
	public void update(float delta) {}
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {}
	public void drawWithShapeRenderer(ShapeRenderer renderer) {}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		float delta = Gdx.graphics.getDeltaTime();
		actionManager.update(delta);
		stage.act(delta);
		update(delta);

		stage.getViewport().apply();
		stage.draw();
		
		hudViewport.apply();
		batch.setProjectionMatrix(hudViewport.getCamera().combined);
		batch.enableBlending();
		batch.begin();
		draw(batch, shapeDrawer);
		
		shapeDrawer.rectangle(0, 0, hudViewport.getWorldWidth() - 1, hudViewport.getWorldHeight() - 1, Color.GREEN);
		batch.end();
		
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

	public static abstract class ActionTest {
		
		public final String name;
		
		public ActionTest(String name) {
			this.name = name;
		}
		
		public abstract Action<?> run();
	}

}
