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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vabrant.actionsystem.actions.ActionManager;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class ActionSystemTestListener extends ApplicationAdapter implements InputProcessor {
	
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	public AssetManager assetManager;
	public ActionManager actionManager;
	public ShapeDrawer shapeDrawer;
	public Texture pixelTexture;
	public Viewport viewport;
	
	@Override
	public void create() {	
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		viewport = new FitViewport(960, 640);
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
		
		Gdx.input.setInputProcessor(this);
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void dispose() {
		batch.dispose();
		assetManager.dispose();
		pixelTexture.dispose();
	}
	
	public void update(float delta) {}
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {}
	public void drawWithShapeRenderer(ShapeRenderer renderer) {}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		float delta = Gdx.graphics.getDeltaTime();
		actionManager.update(delta);
		update(delta);

		batch.setProjectionMatrix(viewport.getCamera().combined);
		batch.enableBlending();
		batch.begin();
		draw(batch, shapeDrawer);
		batch.end();
		
		shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
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

}
