package com.vabrant.actionsystem.actions;

public interface ColorModel {
    void setup(ColorAction action);
    void percent(float percent, ColorAction action);
    void reset();
}
