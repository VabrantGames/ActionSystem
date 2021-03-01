package com.vabrant.actionsystem.test.actiontester;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

public class CustomList {
	
	private class Input extends InputAdapter {
		
		private boolean bypass;
		private Vector2 temp;
		private Stage stage;
		private CustomList list;
		
		public Input(CustomList list, Stage stage) {
			this.list = list;
			this.stage = stage;
			temp = new Vector2();
		}
		
	}
	
	public static class CustomListEntry<T> extends Table implements Poolable {
		
		private Skin skin ;
		
		public CustomListEntry(Skin skin) {
			
		}
	}
	
	private Array<CustomListEntry> entries;
	private Pool<CustomListEntry> pool;
	private Input input;
	private TextureRegionDrawable selectedDrawable;
	private Vector2 selectedPosition;
	private CustomListEntry selectedEntry;
	
	public CustomList(Skin skin, Stage stage) {
		entries = new Array<>(20);
		input = new Input(this, stage);
		selectedDrawable = ((TextureRegionDrawable)skin.getDrawable("maroon"));
		selectedPosition = new Vector2();
		
		pool = new Pool<CustomListEntry>() {
			@Override
			protected CustomListEntry newObject() {
				return new CustomListEntry(skin);
			}
		};
	}
	

}
