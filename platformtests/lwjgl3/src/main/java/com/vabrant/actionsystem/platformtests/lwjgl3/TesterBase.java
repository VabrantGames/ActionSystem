package com.vabrant.actionsystem.platformtests.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.tommyettinger.ds.ObjectObjectMap;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.events.ActionEvent;
import com.vabrant.actionsystem.events.ActionListener;
import com.vabrant.actionsystem.platformtests.tests.Actionable;
import com.vabrant.actionsystem.platformtests.tests.PlatformTest;

@WindowSize(width = 1280, height = 800)
public abstract class TesterBase extends PlatformTest {

    private boolean isTestRunning;
    private float testDelayTimer = 0;
    private final float testDelayDuration = 0.5f;
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public AssetManager assetManager;
    protected Actionable actionable;
    protected Texture squareTexture;
    public Viewport hudViewport;
    protected Stage stage;
    protected Skin skin;
    protected ObjectObjectMap<String, ActionTest> tests;
    private Action<?> actionToRun;
    private String selectedTest;
    private Label isRunningLabel;
    private Label fpsLabel;

    private ActionListener testOverListener = new ActionListener() {
        @Override
        public void onEvent(ActionEvent e) {
            removeCurrentTest();
        }
    };

    @Override
    public void create() {
        super.create();

//        hudViewport = new ScreenViewport();
        hudViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        tests = new ObjectObjectMap<>();
        stage = new Stage(new ScreenViewport(), batch);
//        skin = new Skin(Gdx.files.internal("orangepeelui/uiskin.json"));
        skin = new Skin(Gdx.files.internal("skincomposerui/skin-composer-ui.json"));
        squareTexture = new Texture(Gdx.files.internal("square.png"));

        createTests();

        Table root = new Table();
//        root.debugAll();
        stage.addActor(root);
        root.setFillParent(true);

        Table t = new Table();
        t.defaults().pad(4);

        SelectBox<String> testSelectBox = new SelectBox<>(skin);
        testSelectBox.setItems(new Array(tests.keySet().toArray()));
        testSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedTest = testSelectBox.getSelected();
            }
        });
        selectedTest = testSelectBox.getSelected();
        t.add(testSelectBox);

        isRunningLabel = new Label("Not Running", new LabelStyle(skin.get(LabelStyle.class)));
        isRunningLabel.getStyle().fontColor = Color.BLACK;
        t.add(isRunningLabel);

        root.defaults().pad(4);
        root.add(t).expandX();
        root.row();

        createHud(root, skin);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Keys.ESCAPE:
                        stage.setKeyboardFocus(null);
                        break;
                    case Keys.SPACE:
                        if (selectedTest == null) return false;

                        if (isTestRunning) removeCurrentTest();
                        isTestRunning = true;
                        ActionTest test = tests.get(selectedTest);
                        actionToRun = test.run();
                        actionToRun.subscribeToEvent(ActionEvent.END_EVENT, testOverListener);
                        isRunningLabel.setText("Running");
                        isRunningLabel.getStyle().fontColor = Color.RED;
                        break;
                }
                return super.keyDown(event, keycode);
            }
        });


        Gdx.input.setInputProcessor(stage);
    }

    private void removeCurrentTest() {
        if (isTestRunning) {
            isTestRunning = false;
            actionManager.freeAll(false);
        }

        if (actionToRun != null) {
            ActionPools.free(actionToRun);
            actionToRun = null;
        }

        isRunningLabel.setText("Not Running");
        isRunningLabel.getStyle().fontColor = Color.BLACK;
        testDelayTimer = 0;
    }

    public void addTest(String name, ActionTest test) {
        tests.put(name, test);
    }

    public void createDefaultActionable(int width, int height, Color color) {
        actionable = new Actionable(squareTexture, width, height, color);
        float x = (Gdx.graphics.getWidth() - width) * 0.5f;
        float y = (Gdx.graphics.getHeight() - height) * 0.5f;
        actionable.setPosition(x, y);
    }

    @Override
    public void resize(int width, int height) {
//        hudViewport = new FitViewport(width, height);
        hudViewport.update(width, height, true);
//        hudViewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        squareTexture.dispose();
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float delta = Gdx.graphics.getDeltaTime();

        if (isTestRunning) {
            if (actionToRun != null) {
                if ((testDelayTimer += delta) >= testDelayDuration) {
                    actionManager.addAction(actionToRun);
                    actionToRun = null;
                    testDelayTimer = 0;
                }
            }
        }

        actionManager.update(delta);
        stage.act(delta);

        stage.getViewport().apply();
        stage.draw();

        hudViewport.apply();
        batch.setProjectionMatrix(hudViewport.getCamera().combined);
        batch.begin();
        draw(batch);
        batch.end();
    }

    public void draw(SpriteBatch batch) {
        if (actionable != null) actionable.draw(batch);
    }

    public abstract void createTests();

    public abstract void createHud(Table root, Skin skin);

    public interface ActionTest {
        Action run();
    }
}
