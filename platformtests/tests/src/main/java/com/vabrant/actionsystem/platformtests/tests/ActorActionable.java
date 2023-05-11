package com.vabrant.actionsystem.platformtests.tests;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.vabrant.actionsystem.actions.*;

public class ActorActionable extends Actor implements Movable, Colorable, Rotatable, Shakable, Zoomable, Scalable {


    @Override
    public void setShakeX(float x) {

    }

    @Override
    public void setShakeY(float y) {

    }

    @Override
    public void setShakeAngle(float angle) {

    }

    @Override
    public float getShakeX() {
        return 0;
    }

    @Override
    public float getShakeY() {
        return 0;
    }

    @Override
    public float getShakeAngle() {
        return 0;
    }

    @Override
    public void setZoom(float zoom) {

    }

    @Override
    public float getZoom() {
        return 0;
    }
}
