package com.vabrant.actionsystem.platformtests.lwjgl3;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class Entry extends TextField {

    private boolean allowNegativeValues = true;

    public Entry(Skin skin) {
        super("", skin);
    }

    boolean allowNegativeValues() {
        return allowNegativeValues;
    }

    public void restrictNegativeValues() {
        allowNegativeValues = false;
    }

    public static class IntEntry extends Entry {

        public IntEntry(Skin skin) {
            this(0, skin);
        }

        public IntEntry(int startValue, Skin skin) {
            super(skin);
            setValue(startValue);
            setTextFieldFilter((textField, c) -> {
                if (!Character.isDigit(c)) {
                    if (!allowNegativeValues() || c != '-') return false;
                    return getText().length() < 1
                            || (getCursorPosition() == 0 && getText().charAt(0) != '-');
                }
                return true;
            });
        }

        public void setValue(int value) {
            setText(Integer.toString(value));
        }

        public int getValue() {
            if (getText().isEmpty()) return 0;
            return Integer.parseInt(getText());
        }
    }

    public static class FloatEntry extends Entry {

        public FloatEntry(Skin skin) {
            this(0, skin);
        }

        public FloatEntry(float startValue, Skin skin) {
            super(skin);
            setValue(startValue);
            setTextFieldFilter((textField, c) -> {
                if (!Character.isDigit(c)) {
                    switch (c) {
                        case '.':
                            return !getText().contains(".");
                        case '-':
                            if (!allowNegativeValues()) return false;
                            return getText().length() < 1
                                    || (getCursorPosition() == 0 && getText().charAt(0) != '-');
                        default:
                            return false;
                    }
                }
                return true;
            });
        }

        public void setValue(float value) {
            setText(Float.toString(value));
        }

        public float getValue() {
            if (getText().isEmpty()) return 0;
            return Float.parseFloat(getText());
        }
    }
}
