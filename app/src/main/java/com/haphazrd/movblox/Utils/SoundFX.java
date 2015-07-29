package com.haphazrd.movblox.Utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.haphazrd.movblox.R;

/**
 * Created by brittanystubbs on 7/20/15.
 */
public class SoundFX {
    Context mContext;
    SoundPool sounds;
    private int soundIds[] = new int[10];

    public SoundFX(Context context) {
        mContext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            sounds = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .setMaxStreams(5)
                    .build();

        } else {
            sounds = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        soundIds[0] = sounds.load(context, R.raw.click_button, 1);
        soundIds[1] = sounds.load(context, R.raw.fire_blox, 1);
        soundIds[2] = sounds.load(context, R.raw.game_over, 1);
        soundIds[3] = sounds.load(context, R.raw.move, 1);
        soundIds[4] = sounds.load(context, R.raw.score_tick, 1);
        soundIds[5] = sounds.load(context, R.raw.thud_cant_move, 1);
       // soundIds[6] = sounds.load(context, R.raw.win, 1);
        soundIds[6] = sounds.load(context, R.raw.woosh, 1);
    }

    public void playSound(int index, float soundLevel, int loop){
        sounds.play(soundIds[index], soundLevel,soundLevel,1, loop, 1.0f);
    }

    public void stopSound(int index){
        sounds.stop(soundIds[index]);
    }

    public void pauseSounds(){
        sounds.autoPause();
    }
    public void resumeSounds(){
        sounds.autoResume();
    }

    public void stopSounds(){
        sounds.release();
        sounds = null;
    }
}
