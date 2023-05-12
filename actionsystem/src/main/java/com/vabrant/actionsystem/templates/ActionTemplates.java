package com.vabrant.actionsystem.templates;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.Movable;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.actions.Rotatable;
import com.vabrant.actionsystem.actions.RotateAction;

public class ActionTemplates {

    public static <T extends Movable & Rotatable> GroupAction testTemplate(T actionable, float duration) {
        GroupAction group = GroupAction.obtain();
        group.parallel();
        group.add(MoveAction.moveBy(actionable, 20, 20, duration, Interpolation.circleOut));
        group.add(RotateAction.rotateBy(actionable, 180, duration, Interpolation.swingOut)
                .reverseBackToStart(false));
        return group;
    }
}
