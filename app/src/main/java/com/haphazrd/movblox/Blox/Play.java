package com.haphazrd.movblox.Blox;

import android.content.Context;

import com.haphazrd.movblox.DataStorage.LevelData;

/**
 * Created by brittanystubbs on 5/26/15.
 */
public class Play {
    protected int mLevel;
    protected int mLives;

   private String[] timeLeft = {
            "0:30", "0:30", "0:30", "0:30", "0:30", "0:20", "0:20", "0:20", "0:20", "0:15",
            "0:20", "0:20", "0:20", "0:20", "0:10", "0:30", "0:40", "0:30", "0:30", "0:30",
            "0:20", "0:40", "0:30", "0:30", "0:30", "0:30", "0:30", "0:30", "0:40", "0:40",
    };

    private int[] colNums = {
            3,3,4,4,5,5,5,5,5,6,
            6,6,3,6,6,6,5,6,4,6,
            6,6,6,6,4,6,6,6,6,6,
    };

    private int[] rowNums = {
            3,4,4,5,5,6,6,6,6,6,
            6,6,6,6,3,6,6,6,6,7,
            7,7,7,7,7,4,7,7,7,7,
    };



    public static Level[] getLevels(Context context){

        String[] mColors = {
                "#FF6666", //red
                "#0099FF", //blue
                "#66CC66", //green
                "#9933CC" //purple
        };

        Level[] levels = new Level[30];
        int colorNum = 0;
        //set locked levels
        LevelData levelLocked = new LevelData(context);
        int locked = levelLocked.getUnlockedTo();

        for (int i=0; i < 30; i++) {

            //rotate through colors
            if(colorNum == 3) {
                colorNum = 0;
            } else {
                colorNum += 1;
            }

            Level level = new Level();
            level.setLevel(i + 1);
            level.setRating(levelLocked.getRating(i + 1));
            level.setNumStars(3);
            level.setPlayable(false);
            level.setLocked(true);
            level.setColor(mColors[colorNum]);

            if (i<locked) {
                level.setLocked(false);
                level.setPlayable(true);
            }

            levels[i] = level;
        }
        return levels;
    }

    public int getRows(int level) {
        return rowNums[level - 1];
    }

    public int getCols(int level) {
        return colNums[level - 1];
    }

    public String getTime(int level) {
        return timeLeft[level - 1];
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        mLevel = level;
    }

    public int getLives() {
        return mLives;
    }

    public void setLives(int lives) {
        mLives = lives;
    }


}
