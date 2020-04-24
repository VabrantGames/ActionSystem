/**
 *	Copyright 2019 John Barton
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

public class SoundAction extends TimeAction<SoundAction> {

	public static SoundAction obtain() {
		return obtain(SoundAction.class);
	}
	
	public static SoundAction play(Sound sound, float duration) {
		return obtain()
				.setDuration(duration)
				.setSound(sound, duration);
	}
	
	public static SoundAction play(Sound sound, float duration, float volume) {
		return obtain()
				.setDuration(duration)
				.setVolume(volume)
				.setSound(sound, duration);
	}
	
	public static SoundAction play(Sound sound, float duration, float volume, float pitch, float pan) {
		return obtain()
				.setVolume(volume)
				.setPitch(pitch)
				.setPan(pan)
				.setSound(sound, duration);
	}

	private boolean calculateDuration;
	private long soundId = -1;
	private float originalDuration;
	private float volume = 1;
	private float pitch = 1;
	private float pan = 0;
	private Sound sound;
	
	public SoundAction setSound(Sound sound, float duration) {
		if(sound == null) throw new IllegalArgumentException("Sound is null.");
		if(isRunning) return this;
		this.sound = sound;
		this.originalDuration = this.duration = duration;
		if(calculateDuration) calculateDuration();
		return this;
	}

	public SoundAction setVolume(float volume) {
		this.volume = MathUtils.clamp(volume, 0, 1);
		if(soundId != -1) sound.setVolume(soundId, this.volume); 
		return this;
	}
	
	/**
	 * Sets the pitch of the sound. 
	 * @param pitch
	 * @param duration
	 * @return
	 */
	public SoundAction setPitch(float pitch) {
		this.pitch = MathUtils.clamp(pitch, 0.5f, 2.0f);
		
		//If the sound is null the duration will be calculated when the sound is set
		if(sound == null) {
			calculateDuration = true;
		}
		else {
			calculateDuration();
			if(soundId != -1) sound.setPitch(soundId, this.pitch);
		}
		return this;
	}
	
	public SoundAction setPan(float pan) {
		this.pan = MathUtils.clamp(pan, -1f, 1f);
		if(soundId != -1) sound.setPan(soundId, this.pan, volume);
		return this;
	}
	
	public float getVolume() {
		return volume;
	}
	
	public float getPan() {
		return pan;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getOriginalDuration() {
		return originalDuration;
	}
	
	/**
	 * This method has no affect. Duration is determined by the original duration and the pitch. 
	 */
	@Override
	public SoundAction setDuration(float duration) {
		return this;
	}
	
	private void calculateDuration() {
		calculateDuration = false;
		
		if(pitch > 1.0f) {
			duration = originalDuration / pitch;
		}
		else if(pitch < 1.0) {
			duration = duration * MathUtils.map(1f, 0.5f, 1f, 2f, pitch);
		}
		else {
			duration = originalDuration;
		}
	}

	@Override
	protected void startLogic() {
		if(sound == null) throw new RuntimeException("Sound needs to be set before starting.");
		super.startLogic();
		sound.stop(soundId);
		soundId = sound.play(volume, pitch, pan);
	}
	
	@Override
	protected void pauseLogic() {
		sound.pause(soundId);
	}
	
	@Override
	protected void resumeLogic() {
		sound.resume(soundId);
	}

	@Override
	public void clear() {
		super.clear();
		sound.stop(soundId);
		volume = 1;
		pitch = 1;
		pan = 0;
	}
	
	@Override
	public void reset() {
		super.reset();
		calculateDuration = false;
		sound = null;
		soundId = -1;
		originalDuration = 0;
	}
}
