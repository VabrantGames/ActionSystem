package com.vabrant.actionsystem.test.tests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ColorAction;
import com.vabrant.actionsystem.actions.RGBColorAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.events.ActionEvent;
import com.vabrant.actionsystem.events.ActionListener;
import com.vabrant.actionsystem.test.TestObject;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static org.junit.Assert.*;

public class RGBColorActionTest extends ActionSystemTestListener {

    private TestObject testObject;

    private LabelTextFieldFloatWidget durationWidget;

    private LabelTextFieldFloatWidget redStartWidget;
    private LabelTextFieldFloatWidget redEndWidget;
    private LabelTextFieldFloatWidget greenStartWidget;
    private LabelTextFieldFloatWidget greenEndWidget;
    private LabelTextFieldFloatWidget blueStartWidget;
    private LabelTextFieldFloatWidget blueEndWidget;
    private LabelTextFieldFloatWidget alphaStartWidget;
    private LabelTextFieldFloatWidget alphaEndWidget;

    private DoubleLabelWidget currentRedWidget;
    private DoubleLabelWidget currentGreenWidget;
    private DoubleLabelWidget currentBlueWidget;
    private DoubleLabelWidget currentAlphaWidget;

    private DoubleLabelWidget targetRedWidget;
    private DoubleLabelWidget targetGreenWidget;
    private DoubleLabelWidget targetBlueWidget;
    private DoubleLabelWidget targetAlphaWidget;

    private final ActionListener metricsListener = new ActionListener() {
        @Override
        public void onEvent(ActionEvent e) {
            Color c = testObject.getColor();
            currentRedWidget.setValue(c.r);
            currentGreenWidget.setValue(c.g);
            currentBlueWidget.setValue(c.b);
            currentAlphaWidget.setValue(c.a);
        }
    };

    @Override
    public void create() {
        super.create();
        testObject = new TestObject();
        testObject.setSize(200);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        float x = (width - testObject.getWidth()) * 0.5f;
        float y = (height - testObject.getHeight()) * 0.5f;
        testObject.setX(x);
        testObject.setY(y);
    }

    @Override
    public void createHud(Table root, Skin skin) {
        TextButton setColorButton = new TextButton("Set Color", skin);
        setColorButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setTestObjectColor();
            }
        });

        root.add(setColorButton).left();
        root.row();
        root.add(new Label("", skin));
        root.row();

        Label valuesLabel = new Label("Set Values", new Label.LabelStyle(skin.get(Label.LabelStyle.class)));
        valuesLabel.getStyle().fontColor = Color.BLACK;
        root.add(valuesLabel).left();
        root.row();
        redStartWidget = new LabelTextFieldFloatWidget("Red: ", skin, root, 0);
        redEndWidget = new LabelTextFieldFloatWidget("", skin, root, 0);
        root.row();
        greenStartWidget = new LabelTextFieldFloatWidget("Green: ", skin, root, 0);
        greenEndWidget = new LabelTextFieldFloatWidget("", skin, root, 0);
        root.row();
        blueStartWidget = new LabelTextFieldFloatWidget("Blue: ", skin, root, 0);
        blueEndWidget = new LabelTextFieldFloatWidget("", skin, root, 0);
        root.row();
        alphaStartWidget = new LabelTextFieldFloatWidget("Alpha: ", skin, root, 1);
        alphaEndWidget = new LabelTextFieldFloatWidget("", skin, root, 1);
        root.row();
        durationWidget = new LabelTextFieldFloatWidget("Duration: ", skin, root, 1);
        root.row();

        Label metricsLabel = new Label("Metrics", new Label.LabelStyle(skin.get(Label.LabelStyle.class)));
        metricsLabel.getStyle().fontColor = Color.BLACK;
        root.add(metricsLabel).left();
        root.row();
        currentRedWidget = new DoubleLabelWidget("CurrentRed: ", skin, root);
        root.row();
        currentGreenWidget = new DoubleLabelWidget("CurrentGreen: ", skin, root);
        root.row();
        currentBlueWidget = new DoubleLabelWidget("CurrentBlue: ", skin, root);
        root.row();
        currentAlphaWidget = new DoubleLabelWidget("CurrentAlpha: ", skin, root);
        root.row();
        root.add(new Label("", skin));
        root.row();
        targetRedWidget = new DoubleLabelWidget("TargetRed: ", skin, root);
        root.row();
        targetGreenWidget = new DoubleLabelWidget("TargetGreen: ", skin, root);
        root.row();
        targetBlueWidget = new DoubleLabelWidget("TargetBlue: ", skin, root);
        root.row();
        targetAlphaWidget = new DoubleLabelWidget("TargetAlpha: ", skin, root);
    }

    @Override
    public void createTests() {
        addTest(new ActionTest("ChangeColor(Fixed)") {
            @Override
            public Action<?> run() {
                clearCurrentValues();
                clearTargetValues();
                setTestObjectColor(1, 0, 0, 1);

                Color endColor = null;

                switch (MathUtils.random(0, 2)) {
                    case 0:
                        endColor = Color.BLUE;
                        break;
                    case 1:
                        endColor = Color.PURPLE;
                        break;
                    case 2:
                        endColor = Color.GOLD;
                        break;
                    default:
                        endColor = Color.WHITE;
                }

                setTargetValues(endColor.r, endColor.g, endColor.b, 1);

                return RGBColorAction.changeColor(testObject, endColor, durationWidget.getValue(), Interpolation.linear)
                        .subscribeToEvent(ActionEvent.END_EVENT, metricsListener);
            }
        });

        addTest(new ActionTest("ChangeColorRGB") {
            @Override
            public Action<?> run() {
                clearCurrentValues();
                clearTargetValues();
                setTestObjectColor();
                setTargetValues(redEndWidget.getValue(), greenEndWidget.getValue(), blueEndWidget.getValue(), 1);

                return RGBColorAction.changeColor(testObject, redEndWidget.getValue(), greenEndWidget.getValue(),
                                blueEndWidget.getValue(), durationWidget.getValue(), Interpolation.linear)
                        .subscribeToEvent(ActionEvent.END_EVENT, metricsListener);
            }
        });

        addTest(new ActionTest("ChangeColorRGBA") {
            @Override
            public Action<?> run() {
                clearCurrentValues();
                clearTargetValues();
                setTestObjectColor();
                setTargetValues(redEndWidget.getValue(), greenEndWidget.getValue(), blueEndWidget.getValue(),
                        alphaEndWidget.getValue());

                return RGBColorAction.changeColor(testObject, redEndWidget.getValue(), greenEndWidget.getValue(),
                                blueEndWidget.getValue(), alphaEndWidget.getValue(), durationWidget.getValue(),
                                Interpolation.linear)
                        .subscribeToEvent(ActionEvent.END_EVENT, metricsListener);
            }
        });

        addTest(new ActionTest("ChangeRed") {
            @Override
            public Action<?> run() {
                clearCurrentValues();
                clearTargetValues();
                setTestObjectColor();
                setTargetValues(redEndWidget.getValue(), greenStartWidget.getValue(), blueStartWidget.getValue(),
                        alphaStartWidget.getValue());

                return RGBColorAction.changeRed(testObject, redEndWidget.getValue(), durationWidget.getValue(),
                                Interpolation.linear)
                        .subscribeToEvent(ActionEvent.END_EVENT, metricsListener);
            }
        });

        addTest(new ActionTest("ChangeGreen") {
            @Override
            public Action<?> run() {
                clearCurrentValues();
                clearTargetValues();
                setTestObjectColor();
                setTargetValues(redStartWidget.getValue(), greenEndWidget.getValue(), blueStartWidget.getValue(),
                        alphaStartWidget.getValue());

                return RGBColorAction.changeGreen(testObject, greenEndWidget.getValue(), durationWidget.getValue(),
                                Interpolation.linear)
                        .subscribeToEvent(ActionEvent.END_EVENT, metricsListener);
            }
        });

        addTest(new ActionTest("ChangeBlue") {
            @Override
            public Action<?> run() {
                clearCurrentValues();
                clearTargetValues();
                setTestObjectColor();
                setTargetValues(redStartWidget.getValue(), greenStartWidget.getValue(), blueEndWidget.getValue(),
                        alphaStartWidget.getValue());

                return RGBColorAction.changeBlue(testObject, blueEndWidget.getValue(), durationWidget.getValue(),
                                Interpolation.linear)
                        .subscribeToEvent(ActionEvent.END_EVENT, metricsListener);
            }
        });

        addTest(new ActionTest("RepeatTest") {
            @Override
            public Action<?> run() {
                clearCurrentValues();
                clearTargetValues();
                setTestObjectColor(redStartWidget.getValue(), greenStartWidget.getValue(), blueStartWidget.getValue(),
                        alphaStartWidget.getValue());

//                return RepeatAction.repeat(
//                        RGBColorAction.changeColor(testObject, redEndWidget.getValue(), greenEndWidget.getValue(),
//                                blueEndWidget.getValue(), alphaEndWidget.getValue(), durationWidget.getValue(),
//                                Interpolation.linear), 2);
                return RepeatAction.repeat(
                        RGBColorAction.changeRed(testObject, redEndWidget.getValue(), durationWidget.getValue(),
                                        Interpolation.linear)
                                .soloChannels(true)
                                .subscribeToEvent(ActionEvent.END_EVENT, new ActionListener() {
                                    @Override
                                    public void onEvent(ActionEvent e) {
                                        if (e.getAction() instanceof RGBColorAction) {
                                            RGBColorAction action = (RGBColorAction) e.getAction();
                                            action.getPercentable().getColor().g = 1;
                                        }
                                    }
                                }),
                        2);


            }
        });
    }

    private void setTestObjectColor() {
        testObject.getColor().set(redStartWidget.getValue(), greenStartWidget.getValue(), blueStartWidget.getValue(),
                alphaStartWidget.getValue());
    }

    private void setTestObjectColor(float red, float green, float blue, float alpha) {
        testObject.getColor().set(red, green, blue, alpha);
    }

    private void setTargetValues(float red, float green, float blue, float alpha) {
        targetRedWidget.setValue(red);
        targetGreenWidget.setValue(green);
        targetBlueWidget.setValue(blue);
        targetAlphaWidget.setValue(alpha);
    }

    private void clearCurrentValues() {
        currentRedWidget.setValue(-1);
        currentGreenWidget.setValue(-1);
        currentBlueWidget.setValue(-1);
        currentAlphaWidget.setValue(-1);
    }

    private void clearTargetValues() {
        targetRedWidget.setValue(-1);
        targetGreenWidget.setValue(-1);
        targetBlueWidget.setValue(-1);
        targetAlphaWidget.setValue(-1);
    }

    @Override
    public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
        testObject.draw(shapeDrawer);
    }
}