package com.vabrant.actionsystem.platformtests.lwjgl3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.events.ActionEvent;
import com.vabrant.actionsystem.events.ActionListener;
import com.vabrant.actionsystem.platformtests.lwjgl3.Entry.FloatEntry;

public class MoveActionTester extends TesterBase {

    private FloatEntry xStartEntry;
    private FloatEntry yStartEntry;
    private FloatEntry xEndEntry;
    private FloatEntry yEndEntry;
    private FloatEntry xAmountEntry;
    private FloatEntry angleEntry;
    private FloatEntry yAmountEntry;
    private FloatEntry durationEntry;
    private final int textFieldWidth = 80;

    private ActionListener endListener = e -> {};

    @Override
    public void create() {
        super.create();
        createDefaultActionable(100, 100, Color.BLACK);
        xStartEntry.setValue(actionable.getX());
        yStartEntry.setValue(actionable.getY());
    }

    @Override
    public void createTests() {
        addTest("MoveXBy", () -> {
            actionable.setPosition(xStartEntry.getValue(), yStartEntry.getValue());
            return MoveAction.moveXBy(
                            actionable, xAmountEntry.getValue(), durationEntry.getValue(), Interpolation.linear)
                    .subscribeToEvent(ActionEvent.END_EVENT, endListener);
        });

        addTest("MoveYBy", () -> {
            actionable.setPosition(xStartEntry.getValue(), yStartEntry.getValue());
            return MoveAction.moveYBy(
                            actionable, yAmountEntry.getValue(), durationEntry.getValue(), Interpolation.linear)
                    .subscribeToEvent(ActionEvent.END_EVENT, endListener);
        });

        addTest("MoveXTo", () -> {
            actionable.setPosition(xStartEntry.getValue(), yStartEntry.getValue());
            return MoveAction.moveXTo(actionable, xEndEntry.getValue(), durationEntry.getValue(), Interpolation.linear)
                    .subscribeToEvent(ActionEvent.END_EVENT, endListener);
        });
    }

    @Override
    public void createHud(Table root, Skin skin) {
        final int rightPadding = 4;

        Table table = new Table();
        Label label = null;

        // ===== X =====//
        label = new Label("x", skin);
        xStartEntry = new FloatEntry(skin);
        xStartEntry.setMaxLength(4);
        table.add(label).pad(rightPadding);
        table.add(xStartEntry).width(textFieldWidth).pad(rightPadding);
        xEndEntry = new FloatEntry(skin);
        xEndEntry.setMaxLength(4);
        table.add(xEndEntry).width(textFieldWidth).pad(rightPadding);
        table.row();

        // ===== Y =====//
        label = new Label("y", skin);
        yStartEntry = new FloatEntry(skin);
        yStartEntry.setMaxLength(4);
        table.add(label).pad(rightPadding);
        table.add(yStartEntry).width(textFieldWidth).pad(rightPadding);
        yEndEntry = new FloatEntry(skin);
        yEndEntry.setMaxLength(4);
        table.add(yEndEntry).width(textFieldWidth).pad(rightPadding);
        table.row();

        // ===== Amount =====//
        label = new Label("amt", skin);
        xAmountEntry = new FloatEntry(skin);
        xAmountEntry.setMaxLength(4);
        table.add(label).pad(rightPadding);
        table.add(xAmountEntry).width(textFieldWidth).pad(rightPadding);
        yAmountEntry = new FloatEntry(skin);
        yAmountEntry.setMaxLength(4);
        table.add(yAmountEntry).width(textFieldWidth).pad(rightPadding);
        table.row();

        // Angle
        label = new Label("ang(deg)", skin);
        table.add(label).pad(rightPadding);
        angleEntry = new FloatEntry(skin);
        angleEntry.setMaxLength(3);
        table.add(angleEntry).width(textFieldWidth).pad(rightPadding);
        table.row();

        // Duration
        label = new Label("duration", skin);
        table.add(label).pad(rightPadding);
        durationEntry = new FloatEntry(1, skin);
        durationEntry.setName("duration");
        durationEntry.setMaxLength(3);
        table.add(durationEntry).width(textFieldWidth).pad(rightPadding);
        table.row();

        TextButton setPositionButton = new TextButton("Set Position", skin);
        setPositionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                actionable.setPosition(xStartEntry.getValue(), yStartEntry.getValue());
            }
        });
        table.add(setPositionButton).colspan(3).pad(rightPadding);

        root.add(table).expand().left().top();
    }
}
