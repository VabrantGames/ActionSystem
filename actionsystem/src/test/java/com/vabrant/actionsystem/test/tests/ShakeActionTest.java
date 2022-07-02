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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ShakeAction;
import com.vabrant.actionsystem.actions.ShakeAction.ShakeLogic;
import com.vabrant.actionsystem.test.TestObject;

/**
 * @author John Barton
 *
 */
public class ShakeActionTest extends ActionSystemTestListener {

	private LabelTextFieldFloatWidget xAmountWidget;
	private LabelTextFieldFloatWidget yAmountWidget;
	private LabelTextFieldFloatWidget angleAmountWidget;
	private LabelTextFieldFloatWidget durationWidget;
	private LabelCheckBoxWidget useLogic2Widget;
	private LabelCheckBoxWidget usePercentWidget;
	private LabelCheckBoxWidget reverseBackToStartWidget;
	
	private TestObject testObject;

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
		testObject.setPosition(x, y);
	}
	
	@Override
	public void createHud(Table root, Skin skin) {
		LabelStyle headerStyle = new LabelStyle(skin.get(LabelStyle.class));
		headerStyle.fontColor = Color.BLACK;
		
		Label valuesLabel = new Label("Values", headerStyle);
		root.add(valuesLabel).left();
		root.row();
		xAmountWidget = new LabelTextFieldFloatWidget("xAmount: ", skin, root, 5);
		root.row();
		yAmountWidget = new LabelTextFieldFloatWidget("yAmount: ", skin, root, 5);
		root.row();
		angleAmountWidget = new LabelTextFieldFloatWidget("angleAmount: ", skin, root, 5);
		root.row();
		durationWidget = new LabelTextFieldFloatWidget("duration: ", skin, root, 1);
		root.row();
		usePercentWidget = new LabelCheckBoxWidget("usePercent: ", skin, root);
		root.row();
		useLogic2Widget = new LabelCheckBoxWidget("useCosSinLogic: ", skin, root);
		root.row();
		reverseBackToStartWidget = new LabelCheckBoxWidget("reverseBackToStart: ", skin, root);
	}
	
	private ShakeLogic getShakeLogic() {
		return useLogic2Widget.isChecked() ? ShakeAction.COS_SIN_SHAKE_LOGIC :ShakeAction.RANDOM_SHAKE_LOGIC;
	}
	
	@Override
	public void createTests() {
		addTest(new ActionTest("ShakeX") {
			@Override
			public Action<?> run() {
				return ShakeAction.shakeX(testObject, getShakeLogic(), new ShakeAction.DefaultShakeLogicData(), xAmountWidget.getValue(),
						durationWidget.getValue(), Interpolation.linear)
						.setUsePercent(usePercentWidget.isChecked())
						.reverseBackToStart(reverseBackToStartWidget.isChecked());
			}
		});
		
		addTest(new ActionTest("ShakeY") {
			@Override
			public Action<?> run() {
				return ShakeAction.shakeY(testObject, getShakeLogic(), new ShakeAction.DefaultShakeLogicData(), yAmountWidget.getValue(),
						durationWidget.getValue(), Interpolation.linear)
						.setUsePercent(usePercentWidget.isChecked())
						.reverseBackToStart(reverseBackToStartWidget.isChecked());
			}
		});
		
		addTest(new ActionTest("ShakeAngle") {
			@Override
			public Action<?> run() {
				return ShakeAction.shakeAngle(testObject, getShakeLogic(), new ShakeAction.DefaultShakeLogicData(),
						angleAmountWidget.getValue(), durationWidget.getValue(), 
						Interpolation.linear)
						.setUsePercent(usePercentWidget.isChecked())
						.reverseBackToStart(reverseBackToStartWidget.isChecked());
			}
		});
		
		addTest(new ActionTest("Shake") {

			@Override
			public Action<?> run() {
				return ShakeAction.shake(testObject, getShakeLogic(), new ShakeAction.DefaultShakeLogicData(), xAmountWidget.getValue(),
						yAmountWidget.getValue(), angleAmountWidget.getValue(),
						durationWidget.getValue(), Interpolation.linear)
						.setUsePercent(usePercentWidget.isChecked())
						.reverseBackToStart(reverseBackToStartWidget.isChecked());
			}
		});
		
		addTest(new ActionTest("UsePercent(Fixed)") {
			@Override
			public Action<?> run() {
				return ShakeAction.shake(testObject, ShakeAction.COS_SIN_SHAKE_LOGIC,
								new ShakeAction.DefaultShakeLogicData(), 4f, 3f,
								4f, 2.5f, Interpolation.smooth2)
						.setUsePercent(true)
						.reverseBackToStart(true);
			}
		});

		addTest(new ActionTest("GDQShake") {
			@Override
			public Action<?> run() {
				return ShakeAction.shake(testObject, new ShakeAction.GDQShakeLogic(0.5f),
								new ShakeAction.GDQShakeLogic.GDQShakeLogicData(), xAmountWidget.getValue(),
								yAmountWidget.getValue(), angleAmountWidget.getValue(), durationWidget.getValue(),
								null)
						.setUsePercent(usePercentWidget.isChecked());
			}
		});

	}

	@Override
	public void drawWithShapeRenderer(ShapeRenderer renderer) {
		testObject.draw(renderer);
	}





}

