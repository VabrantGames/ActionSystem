package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.audio.Sound;

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
	
	public static SoundAction playSound(Sound sound, float volume) {
		SoundAction action = getAction();
		action.play(sound);
		action.setVolume(volume);
		return action;
	}
	
	public static SoundAction playSound(Sound sound, float volume, float pitch) {
		SoundAction action = getAction();
		return action;
	}
	
	public static SoundAction playSound(Sound sound, float volume, float pitch, float pan) {
		SoundAction action = getAction();
		action.setVolume(volume);
		action.setPitch(pitch);
		action.setPan(pan);
		return action;
	}

	private long soundId;
	private float volume = 1;
	private float pitch = 1;
	private float pan = 0;
	private Sound sound;
	
	public SoundAction play(Sound sound) {
		if(sound == null) throw new IllegalArgumentException("Sound is null.");
		this.sound = sound;
		return this;
	}

	public SoundAction setVolume(float volume) {
		this.volume = volume;
		return this;
	}
	
	public SoundAction setPitch(float pitch) {
		this.pitch = pitch;
		return this;
	}
	
	public SoundAction setPan(float pan) {
		this.pan = pan;
		return this;
	}
	
	@Override
	public SoundAction start() {
		super.start();
		soundId = sound.play(volume, pitch, pan);
		return this;
	}
	
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
	
	@Override
	public void reset() {
		super.reset();
		soundId = 0;
		volume = 1;
		pitch = 1;
		pan = 0;
	}
}
