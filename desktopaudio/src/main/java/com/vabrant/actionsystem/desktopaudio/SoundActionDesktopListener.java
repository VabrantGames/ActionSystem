package com.vabrant.actionsystem.desktopaudio;

import org.lwjgl.openal.AL10;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.audio.OpenALAudio;
import com.badlogic.gdx.utils.LongMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.vabrant.actionsystem.actions.SoundActionPlatformListener;

/** 
 * 
 *	@author Rafaskb
 *	@see <a href="https://github.com/rafaskb/Boom/blob/master/lwjgl3/src/main/java/com/rafaskoberg/boom/BoomLwjgl3.java">BoomLwjgl</a>
 */
public class SoundActionDesktopListener implements SoundActionPlatformListener{
	
	private static Field soundIdToSourceId;
	
	static {
		try {
			soundIdToSourceId = ClassReflection.getDeclaredField(OpenALAudio.class, "soundIdToSource");
			soundIdToSourceId.setAccessible(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isPlaying(long soundId) {
		return AL10.alGetSourcei(getSourceId(soundId), AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	private static int getSourceId(long soundId) {
		 try {
	            LongMap<Integer> soundIdToSource = (LongMap<Integer>) soundIdToSourceId.get(Gdx.audio);
	            Integer result = soundIdToSource.get(soundId);
	            if(result != null) {
	                return result;
	            }
	        } catch(ReflectionException e) {
	            e.printStackTrace();
	        }
	        return -1;
	}
	
}
