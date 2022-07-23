package com.vabrant.actionsystem.test.tests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.HSBColorAction;
import com.vabrant.actionsystem.events.ActionEvent;
import com.vabrant.actionsystem.events.ActionListener;
import com.vabrant.actionsystem.test.TestObject;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static org.junit.Assert.*;

public class HSBColorActionTest extends ActionSystemTestListener {

    private TestObject testObject;

    private LabelTextFieldFloatWidget durationWidget;

    private LabelTextFieldFloatWidget hueStartWidget;
    private LabelTextFieldFloatWidget hueEndWidget;
    private LabelTextFieldFloatWidget saturationStartWidget;
    private LabelTextFieldFloatWidget saturationEndWidget;
    private LabelTextFieldFloatWidget brightnessStartWidget;
    private LabelTextFieldFloatWidget brightnessEndWidget;
    private LabelTextFieldFloatWidget alphaStartWidget;
    private LabelTextFieldFloatWidget alphaEndWidget;

    private DoubleLabelWidget currentHueWidget;
    private DoubleLabelWidget currentSaturationWidget;
    private DoubleLabelWidget currentBrightnessWidget;
    private DoubleLabelWidget currentAlphaWidget;

    private DoubleLabelWidget targetHueWidget;
    private DoubleLabelWidget targetSaturationWidget;
    private DoubleLabelWidget targetBrightnessWidget;
    private DoubleLabelWidget targetAlphaWidget;

    private final ActionListener metricsListener = new ActionListener() {
        @Override
        public void onEvent(ActionEvent e) {
            Color c = testObject.getColor();
            currentHueWidget.setValue(HSBColorAction.getHue(c));
            currentSaturationWidget.setValue(HSBColorAction.getSaturation(c));
            currentBrightnessWidget.setValue(HSBColorAction.getBrightness(c));
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
        hueStartWidget = new LabelTextFieldFloatWidget("Hue: ", skin, root, 0);
        hueEndWidget = new LabelTextFieldFloatWidget("", skin, root, 0);
        root.row();
        saturationStartWidget = new LabelTextFieldFloatWidget("Saturation: ", skin, root, 1);
        saturationEndWidget = new LabelTextFieldFloatWidget("", skin, root, 1);
        root.row();
        brightnessStartWidget = new LabelTextFieldFloatWidget("Brightness: ", skin, root, 1);
        brightnessEndWidget = new LabelTextFieldFloatWidget("", skin, root, 1);
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
        currentHueWidget = new DoubleLabelWidget("CurrentHue: ", skin, root);
        root.row();
        currentSaturationWidget = new DoubleLabelWidget("CurrentSaturation: ", skin, root);
        root.row();
        currentBrightnessWidget = new DoubleLabelWidget("CurrentBrightness: ", skin, root);
        root.row();
        currentAlphaWidget = new DoubleLabelWidget("CurrentAlpha: ", skin, root);
        root.row();
        root.add(new Label("", skin));
        root.row();
        targetHueWidget = new DoubleLabelWidget("TargetHue: ", skin, root);
        root.row();
        targetSaturationWidget = new DoubleLabelWidget("TargetSaturation: ", skin, root);
        root.row();
        targetBrightnessWidget = new DoubleLabelWidget("TargetBrightness: ", skin, root);
        root.row();
        targetAlphaWidget = new DoubleLabelWidget("TargetAlpha: ", skin, root);
    }

     /**
     * @see com.vabrant.actionsystem.actions.MoveAction
     */
    @Override
    public void createTests() {
        addTest(new ActionTest("ChangeColorHSB") {
            @Override
            public Action<?> run() {
                clearCurrentValues();
                clearTargetValues();
                setTestObjectColor();
                setTargetValue(hueEndWidget.getValue() % 360, saturationEndWidget.getValue(),
                        brightnessEndWidget.getValue(), alphaStartWidget.getValue());

                return HSBColorAction.changeColor(testObject, hueEndWidget.getValue(), saturationEndWidget.getValue(),
                                brightnessEndWidget.getValue(), durationWidget.getValue(), Interpolation.linear)
                        .subscribeToEvent(ActionEvent.END_EVENT, metricsListener);
            }
        });

        addTest(new ActionTest("ChangeColorHSBA") {
            @Override
            public Action<?> run() {
                clearCurrentValues();
                clearTargetValues();
                setTestObjectColor();
                setTargetValue(hueEndWidget.getValue() % 360, saturationEndWidget.getValue(),
                        brightnessEndWidget.getValue(), alphaEndWidget.getValue());

                return HSBColorAction.changeColor(testObject, hueEndWidget.getValue(), saturationEndWidget.getValue(),
                                brightnessEndWidget.getValue(), alphaEndWidget.getValue(), durationWidget.getValue(),
                                Interpolation.linear)
                        .subscribeToEvent(ActionEvent.END_EVENT, metricsListener);
            }
        });

        addTest(new ActionTest("ChangeHue") {
            @Override
            public Action<?> run() {
                clearCurrentValues();
                clearTargetValues();
                setTestObjectColor();
                setTargetValue(hueEndWidget.getValue() % 360, saturationStartWidget.getValue(),
                        brightnessStartWidget.getValue(), alphaStartWidget.getValue());

                return HSBColorAction.changeHue(testObject, hueEndWidget.getValue(), durationWidget.getValue(),
                                Interpolation.linear)
                        .subscribeToEvent(ActionEvent.END_EVENT, metricsListener);
            }
        });

        addTest(new ActionTest("ChangeSaturation") {
            @Override
            public Action<?> run() {
                clearCurrentValues();
                clearTargetValues();
                setTestObjectColor();
                setTargetValue(hueStartWidget.getValue(), saturationEndWidget.getValue(),
                        brightnessStartWidget.getValue(), alphaStartWidget.getValue());

                return HSBColorAction.changeSaturation(testObject, saturationEndWidget.getValue(),
                                durationWidget.getValue(), Interpolation.linear)
                        .subscribeToEvent(ActionEvent.END_EVENT, metricsListener);
            }
        });

        addTest(new ActionTest("ChangeBrightness") {
            @Override
            public Action<?> run() {
                clearCurrentValues();
                clearTargetValues();
                setTestObjectColor();
                setTargetValue(hueStartWidget.getValue(), saturationStartWidget.getValue(),
                        brightnessEndWidget.getValue(), alphaStartWidget.getValue());

                return HSBColorAction.changeBrightness(testObject, brightnessEndWidget.getValue(),
                                durationWidget.getValue(), Interpolation.linear)
                        .subscribeToEvent(ActionEvent.END_EVENT, metricsListener);
            }
        });
    }

    private void setTestObjectColor() {
        HSBColorAction.HSBToRGB(testObject.getColor(), hueStartWidget.getValue(), saturationStartWidget.getValue(),
                brightnessStartWidget.getValue());
        testObject.getColor().a = alphaStartWidget.getValue();
    }

    private void setTestObjectColor(float red, float green, float blue, float alpha) {
        testObject.getColor().set(red, green, blue, alpha);
    }

    private void setTargetValue(float hue, float saturation, float brightness, float alpha) {
        targetHueWidget.setValue(hue);
        targetSaturationWidget.setValue(saturation);
        targetBrightnessWidget.setValue(brightness);
        targetAlphaWidget.setValue(alpha);
    }

    private void clearCurrentValues() {
        currentHueWidget.setValue(-1);
        currentSaturationWidget.setValue(-1);
        currentBrightnessWidget.setValue(-1);
        currentAlphaWidget.setValue(-1);
    }

    private void clearTargetValues() {
        targetHueWidget.setValue(-1);
        targetSaturationWidget.setValue(-1);
        targetBrightnessWidget.setValue(-1);
        targetAlphaWidget.setValue(-1);
    }

    @Override
    public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
        testObject.draw(shapeDrawer);
    }
}