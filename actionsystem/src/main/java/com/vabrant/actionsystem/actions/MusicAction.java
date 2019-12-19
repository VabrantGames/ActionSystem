package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.audio.Music;

public class MusicAction extends TimeAction<MusicAction> {
	
	public static MusicAction getAction() {
		return getAction(MusicAction.class);
	}
	
	public static MusicAction play(Music music, float duration) {
		MusicAction action = getAction();
		action.setDuration(duration);
		action.play(music);
		return action;
	}
	
	private float volume = 1;
	private float pan;
	private Music music;
	
	public MusicAction play(Music music) {
		if(music == null) throw new IllegalArgumentException("Music is null");
		this.music = music;
		return this;
	}
	
	public MusicAction setVolume(float volume) {
		this.volume = volume;
		music.setVolume(volume);
		return this;
	}
	
	public MusicAction setPan(float pan) {
		this.pan = pan;
		music.setPan(pan, volume);
		return this;
	}
	
	@Override
	public MusicAction start() {
		super.start();
		music.setPan(pan, volume);
		music.play();
		return this;
	}
	
	@Override
	protected void customPauseLogic() {
		music.pause();
	}
	
	@Override
	protected void customResumeLogic() {
		music.play();
	}
	
	@Override
	public void reset() {
		super.reset();
		if(music.isPlaying()) music.stop();
		music = null;
		volume = 1;
		pan = 0;
	}
	
}
