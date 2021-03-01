package com.vabrant.actionsystem.test.actiontester;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class RenderPanel extends Table {
	
	private class Test extends Actor {
		
		private boolean rectangle;
		private ShapeDrawer shapeDrawer;
		
		public Test(ShapeDrawer shapeDrawer) {
			this.shapeDrawer = shapeDrawer;
		}
		
		@Override
		public void draw(Batch batch, float parentAlpha) {
			if(rectangle) {
				shapeDrawer.filledRectangle(getX(), getY(), getWidth(), getHeight(), Color.RED);
			}
			else {
				shapeDrawer.setColor(Color.RED);
				shapeDrawer.filledCircle(getX(), getY(), 50);
				shapeDrawer.setColor(Color.WHITE);
			}
		}
	}
	
	Group objects;
	ShapeDrawer shapeDrawer;

	public RenderPanel(Stage stage, Skin skin) {
		super(skin);
		background("panel-orange");
		
		objects = new Group();
		
//		add(objects).expand().left().bottom();
		add(objects).grow();
		
		TextureRegion region = skin.getAtlas().findRegion("white");
		shapeDrawer = new ShapeDrawer(stage.getBatch(), region);

		Test t1 = new Test(shapeDrawer);
		t1.setBounds(0, 0, 100, 100);
		t1.rectangle = true;
		
		Test t2 = new Test(shapeDrawer);
		t2.setBounds(300, 400, 100, 100);
		
		objects.addActor(t1);
		objects.addActor(t2);
		
	}

}
