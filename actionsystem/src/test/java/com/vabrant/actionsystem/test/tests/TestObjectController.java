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

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vabrant.actionsystem.test.TestObject;

import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * @author John Barton
 *
 */
public class TestObjectController {
	
	private static final TestObjectController instance = new TestObjectController();
	
	public static TestObjectController getInstance() {
		return instance;
	}
	
	Array<TestObject> testObjects = new Array<>(5);
	
	private TestObjectController() {}
	
	public TestObject create() {
		TestObject o = new TestObject();
		o.setSize(50, 50);
		o.isRunning = true;
		testObjects.add(o);
		return o;
	}
	
	public void center(TestObject object, Viewport viewport) {
		float x = (viewport.getWorldWidth() - object.width) / 2;
		float y = (viewport.getWorldHeight() - object.height) / 2;
		object.setPosition(x, y);
	}
	
	public void draw(ShapeRenderer shapeRenderer) {
		for(int i = testObjects.size - 1; i >= 0; i--) {
			TestObject o = testObjects.get(i);
			
			o.draw(shapeRenderer);
			
			if(!testObjects.get(i).isRunning) {
				testObjects.pop();
			}
		}
	}

	public void draw(ShapeDrawer shapeDrawer) {
		for(int i = testObjects.size - 1; i >= 0; i--) {
			TestObject o = testObjects.get(i);
			
			o.draw(shapeDrawer);
			
			if(!testObjects.get(i).isRunning) {
				testObjects.pop();
			}
		}
	}

}
