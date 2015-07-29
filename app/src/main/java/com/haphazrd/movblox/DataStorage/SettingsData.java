package com.haphazrd.movblox.DataStorage;

import android.content.Context;
import android.content.SharedPreferences;

import com.haphazrd.movblox.R;

import java.util.Date;

/**
 * Created by brittanystubbs on 6/18/15.
 */
public class SettingsData {
    private SharedPreferences mSharedPreferences;
    public Context mContext;

    public SettingsData(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(context.getString(R.string.settings_data), Context.MODE_PRIVATE);
    }
    public void setMusic(boolean checked) {
       // boolean checked = ((Switch) view).isChecked();

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(mContext.getString(R.string.music_checked), checked);
        editor.commit();
    }
    public boolean getMusic() {
        boolean defaultValue = true;
        boolean musicOn = mSharedPreferences.getBoolean(mContext.getString(R.string.music_checked), defaultValue);
        return musicOn;
    }

    public void setSoundFX(boolean checked) {
        //boolean checked = ((Switch) view).isChecked();

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(mContext.getString(R.string.sound_fx_checked), checked);
        editor.commit();
    }
    public boolean getSoundFX() {
        boolean defaultValue = true;
        boolean soundFXOn = mSharedPreferences.getBoolean(mContext.getString(R.string.sound_fx_checked), defaultValue);
        return soundFXOn;
    }

    public void setColorBlind(boolean checked) {
        //boolean checked = ((Switch) view).isChecked();

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(mContext.getString(R.string.color_blind_checked), checked);
        editor.commit();
    }
    public boolean getColorBlind() {
        boolean defaultValue = false;
        boolean colorBlindOn = mSharedPreferences.getBoolean(mContext.getString(R.string.color_blind_checked), defaultValue);
        return colorBlindOn;
    }

    public void setTutorial(boolean checked){
        //boolean checked = ((Switch) view).isChecked();

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("TUTORIAL_CHECKED", checked);
        editor.commit();
    }

    public boolean getTutorial(){
        boolean defaultValue = true;
        boolean tutorialOn = mSharedPreferences.getBoolean("TUTORIAL_CHECKED", defaultValue);
        return tutorialOn;
    }

    //time for more lives
    public void setTime(){
        Date currentDate = new Date();

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong("WAIT_TIME", currentDate.getTime() + 1800000); //30 min
        editor.commit();
    }

    public long getTime(){
        Date date = new Date();
       long futureTime = mSharedPreferences.getLong("WAIT_TIME", 0);
        long currentDate = date.getTime();

        if(futureTime > 0){
            return futureTime - currentDate;
        }else {
            return futureTime;
        }
    }

    public void setTimerGoing(boolean isGoing){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("TIMER_IS_GOING", isGoing);
        editor.commit();
    }

    public boolean timerIsGoing(){
        boolean timer = mSharedPreferences.getBoolean("TIMER_IS_GOING", false);
        return timer;
    }


    //set lives
    public void setLives(int lives){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt("LIVES_LEFT", lives);
        editor.commit();
    }

    public int getLives() {
        int lives = mSharedPreferences.getInt("LIVES_LEFT", 3);
        return lives;
    }

    public void setLivesLeft(int lives) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(mContext.getString(R.string.lives_left_data), lives);
        editor.commit();
    }
    public int getLivesLeft() {
        int defaultValue = mContext.getResources().getInteger(R.integer.lives_left_default);
        int livesLeft = mSharedPreferences.getInt(mContext.getString(R.string.lives_left_data), defaultValue);
        return livesLeft;
    }
}
