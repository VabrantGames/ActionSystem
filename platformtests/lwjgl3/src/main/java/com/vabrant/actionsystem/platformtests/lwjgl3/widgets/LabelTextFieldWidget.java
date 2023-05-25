package com.vabrant.actionsystem.platformtests.lwjgl3.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class LabelTextFieldWidget extends Table {

    private boolean allowNegativeValues;
    private Label label;
    private TextField textField;

    public LabelTextFieldWidget(String labelTitle, Skin skin) {
        label = new Label(labelTitle, skin);
        textField = new TextField("", skin);

        defaults().padRight(10);
        add(label);
        add(textField);
    }

    public Label getLabel() {
        return label;
    }

    public TextField getTextField() {
        return textField;
    }

    public void allowNegativeValues(boolean allow) {
        allowNegativeValues = allow;
    }

    public boolean allowNegativeValues() {
        return allowNegativeValues;
    }
}
