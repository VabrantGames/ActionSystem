package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.reflect.ClassReflection;

public class SoundAction extends Action<SoundAction> {

	private static SoundActionPlatformListener isSoundPlayingListener;
	
	public static void init(SoundActionPlatformListener listener) {
		if(listener == null) throw new IllegalArgumentException("Listener is null.");
		isSoundPlayingListener = listener;
	}

	public static SoundAction getAction() {
		return getAction(SoundAction.class);
	}
	
	public static SoundAction playSound(Sound sound) {
		SoundAction action = getAction();
		action.play(sound);
		return action;
	}

	private long soundId;
	private float volume;
	private float pan;
	private Sound sound;
	
	public SoundAction play(Sound sound) {
		if(sound == null) throw new IllegalArgumentException("Sound is null.");
		this.sound = sound;
		return this;
	}
	
	@Override
	public SoundAction start() {
		super.start();
		soundId = sound.play();
		return this;
	}
	
	float timer;
	
	@Override
	public boolean update(float delta) {
		if(isFinished) return true;
		if(isPaused) return false;
		if(!isRunning) start();
		
		if(!isSoundPlayingListener.isPlaying(soundId)) {
			end();
		}
		
		return isFinished;
	}
	
}
