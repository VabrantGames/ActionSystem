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

import com.badlogic.gdx.audio.Music;

@Deprecated
public class MusicAction extends TimeAction<MusicAction> {

    public static MusicAction obtain() {
        return obtain(MusicAction.class);
    }

    public static MusicAction play(Music music, float duration) {
        return obtain().setMusic(music, duration);
    }

    public static MusicAction play(Music music, float duration, float volume) {
        return obtain().setVolume(volume).setMusic(music, duration);
    }

    public static MusicAction play(Music music, float duration, float volume, float pan) {
        return obtain().setVolume(volume).setPan(pan).setMusic(music, duration);
    }

    private float volume = 1;
    private float pan;
    private Music music;

    public MusicAction setMusic(Music music, float duration) {
        if (music == null) throw new IllegalArgumentException("Music is null");
        if (isRunning) return this;
        this.music = music;
        this.duration = duration;
        return this;
    }

    public MusicAction setVolume(float volume) {
        this.volume = volume;
        if (music != null) music.setVolume(volume);
        return this;
    }

    public MusicAction setPan(float pan) {
        this.pan = pan;
        if (music != null) music.setPan(pan, volume);
        return this;
    }

    public float getVolume() {
        return volume;
    }

    public Music getMusic() {
        return music;
    }

    /** This method has no affect. */
    @Override
    public MusicAction setDuration(float duration) {
        return this;
    }

    @Override
    protected void startLogic() {
        if (music == null) throw new RuntimeException("Music needs to be set before starting.");
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
        if (music != null) music.stop();
        volume = 1;
        pan = 0;
    }

    @Override
    public void reset() {
        super.reset();
        music = null;
    }
}
