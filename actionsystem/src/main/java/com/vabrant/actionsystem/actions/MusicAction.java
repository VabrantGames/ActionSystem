package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.audio.Music;

public class MusicAction extends TimeAction<MusicAction> {
	
	public static MusicAction obtain() {
		return obtain(MusicAction.class);
	}
	
	public static MusicAction play(Music music, float duration) {
		return obtain()
				.setMusic(music, duration);
	}
	
	public static MusicAction play(Music music, float duration, float pan) {
		return obtain()
				.setPan(pan)
				.setMusic(music, duration);
	}
	
	private float volume = 1;
	private float pan;
	private Music music;
	
	public MusicAction setMusic(Music music, float duration) {
		if(music == null) throw new IllegalArgumentException("Music is null");
		if(isRunning) return this;
		this.music = music;
		this.duration = duration;
		return this;
	}
	
	public MusicAction setVolume(float volume) {
		this.volume = volume;
		if(music != null) music.setVolume(volume);
		return this;
	}
	
	public MusicAction setPan(float pan) {
		this.pan = pan;
		if(music != null) music.setPan(pan, volume);
		return this;
	}
	
	public float getVolume() {
		return volume;
	}
	
	public Music getMusic() {
		return music;
	}
	
	/**
	 * This method has no affect.
	 */
	@Override
	public MusicAction setDuration(float duration) {
		return this;
	}
	
	@Override
	protected void startLogic() {
		if(music == null) throw new RuntimeException("Music needs to be set before starting.");
		super.startLogic();
		music.setPan(pan, volume);
		music.play();
	}
	
	@Override
	protected void endLogic() {
		super.endLogic();
		music.stop();
	}
	
	@Override
	protected void pauseLogic() {
		music.pause();
	}
	
	@Override
	protected void resumeLogic() {
		music.play();
	}
	
	@Override
	public void clear() {
		super.clear();
		if(music != null) music.stop();
		volume = 1;
		pan = 0;
	}
	
	@Override
	public void reset() {
		super.reset();
		music = null;
	}
	
}
