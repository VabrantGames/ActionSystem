/**
 *	Copyright 2020 See AUTHORS file.
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package com.vabrant.actionsystem.test.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.SoundAction;

/** @author John Barton */
public class SoundActionTest extends ActionSystemTestListener {

    private LabelTextFieldFloatWidget volumeWidget;
    private LabelTextFieldFloatWidget pitchWidget;
    private LabelTextFieldFloatWidget panWidget;
    private LabelCheckBoxWidget monoWidget;

    private Sound monoSound;
    private Sound stereoSound;

    @Override
    public void create() {
        super.create();
        monoSound = Gdx.audio.newSound(Gdx.files.internal("simpleSawMono.mp3"));
        stereoSound = Gdx.audio.newSound(Gdx.files.internal("simpleSawStereo.mp3"));
    }

    @Override
    public void createHud(Table root, Skin skin) {
        Label valuesLabel = new Label("Values", new LabelStyle(skin.get(LabelStyle.class)));
        valuesLabel.getStyle().fontColor = Color.BLACK;
        root.add(valuesLabel).left();
        root.row();
        volumeWidget = new LabelTextFieldFloatWidget("volume: ", skin, root, 1f);
        root.row();
        pitchWidget = new LabelTextFieldFloatWidget("pitch: ", skin, root, 1f);
        root.row();
        panWidget = new LabelTextFieldFloatWidget("pan: ", skin, root, 0f);
        panWidget.allowNegativeValues();
        root.row();
        monoWidget = new LabelCheckBoxWidget("mono: ", skin, root);
    }

    @Override
    public void createTests() {
        addTest(new ActionTest("PlaySound") {
            @Override
            public Action<?> run() {
                return SoundAction.play(
                        getSound(), 6.857f, volumeWidget.getValue(), pitchWidget.getValue(), panWidget.getValue());
            }
        });

        addTest(new ActionTest("Blank") {
            @Override
            public Action<?> run() {
                return null;
            }
        });
    }

    public Sound getSound() {
        return monoWidget.isChecked() ? monoSound : stereoSound;
    }
}
