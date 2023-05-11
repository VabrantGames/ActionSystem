package com.vabrant.actionsystem.platformtests.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class DefaultPlatformTest extends PlatformTest {

    protected Batch batch;
    protected Viewport viewport;
    protected Actionable actionable;

    @Override
    public void create() {
        super.create();
        batch = new SpriteBatch();
        viewport = new ScreenViewport();
        actionable = new Actionable(new Texture(Gdx.files.internal("square.png")));
        actionable.setColor(Color.BLACK);
        actionable.setX((Gdx.graphics.getWidth() - 100) * 0.5f);
        actionable.setY((Gdx.graphics.getHeight() - 100) * 0.5f);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        super.render();
        ScreenUtils.clear(Color.WHITE);
        batch.begin();
        actionable.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
