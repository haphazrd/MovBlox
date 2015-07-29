package com.haphazrd.movblox.DataStorage;

import android.content.Context;
import android.content.SharedPreferences;

import com.haphazrd.movblox.R;

/**
 * Created by brittanystubbs on 6/10/15.
 */
public class GameLivesData {
    private SharedPreferences mSharedPreferences;
    public Context mContext;

    public GameLivesData(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(context.getString(R.string.game_lives_data), Context.MODE_PRIVATE);
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
