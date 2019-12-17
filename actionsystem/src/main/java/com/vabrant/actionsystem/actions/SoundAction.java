package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

public class SoundAction extends TimeAction<SoundAction> {

	public static SoundAction getAction() {
		return getAction(SoundAction.class);
	}
	
	public static SoundAction play(Sound sound, float duration) {
		SoundAction action = getAction();
		action.setDuration(duration);
		action.play(sound);
		return action;
	}
	
	public static SoundAction play(Sound sound, float duration, float volume) {
		SoundAction action = getAction();
		action.setDuration(duration);
		action.setVolume(volume);
		action.play(sound);
		return action;
	}
	
	public static SoundAction play(Sound sound, float duration, float volume, float pitch, float pan) {
		SoundAction action = getAction();
		
		float actualDuration = duration;
		if(pitch > 1.0) {
			actualDuration = duration / pitch;
		}
		else if(pitch < 1.0f && pitch >= 0.5f) {
			actualDuration = duration * MathUtils.map(1, 0.5f, 1, 2, pitch);
		}
		else if(pitch < 0.5f) {
//			actualDuration = duration * MathUtils.map(0.5f, 0, 2, 6, pitch);
//			actualDuration = duration * 5;//0.2
//			actualDuration = duration * 3.35f;//0.3
//			actualDuration = duration * 2.5f;//0.4
//			actualDuration = duration * 2;//0.5
		}
		
		action.setDuration(actualDuration);
		action.setVolume(volume);
		action.setPitch(pitch);
		action.setPan(pan);
		action.play(sound);
		return action;
	}

	private long soundId = -1;
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
		if(soundId != -1) sound.setVolume(soundId, volume); 
		return this;
	}
	
	public SoundAction setPitch(float pitch) {
		this.pitch = pitch;
		if(soundId != -1) sound.setPitch(soundId, pitch);
		return this;
	}
	
	public SoundAction setPan(float pan) {
		this.pan = pan;
		if(soundId != -1) sound.setPan(soundId, pan, volume);
		return this;
	}
	
	@Override
	public SoundAction start() {
		super.start();
		soundId = sound.play(volume, pitch, pan);
		return this;
	}
	
	@Override
	protected void customPauseLogic() {
		sound.pause(soundId);
	}
	
	@Override
	protected void customResumeLogic() {
		sound.resume(soundId);
	}

	@Override
	public void reset() {
		super.reset();
		sound = null;
		soundId = -1;
		volume = 1;
		pitch = 1;
		pan = 0;
	}
}
