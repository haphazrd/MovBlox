package com.haphazrd.movblox.DataStorage;

import android.content.Context;
import android.content.SharedPreferences;

import com.haphazrd.movblox.R;

/**
 * Created by brittanystubbs on 6/18/15.
 */
public class LevelData {
    SharedPreferences mSharedPreferences;
    Context mContext;

    public LevelData(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(context.getString(R.string.level_data), Context.MODE_PRIVATE);
    }

    //set furthest unlocked level
    public void setUnlockedTo(int level) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(mContext.getString(R.string.level_unlocked_data), level);
        editor.commit();
    }
    public int getUnlockedTo() {
        int defaultValue = mContext.getResources().getInteger(R.integer.level_unlocked_default);
        int levelUnlocked = mSharedPreferences.getInt(mContext.getString(R.string.level_unlocked_data), defaultValue);
        return levelUnlocked;
    }

    //set rating for level
    public void setRating(int level, int rating) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt("LEVEL_" + level, rating);
        editor.commit();
    }
    public int getRating(int level) {
        int defaultValue = mContext.getResources().getInteger(R.integer.rating_default);
        int levelRating = mSharedPreferences.getInt("LEVEL_" + level, defaultValue);
        return levelRating;
    }
}
