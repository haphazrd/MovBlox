package com.haphazrd.movblox.Blox;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by brittanystubbs on 5/20/15.
 */
public class Level implements Parcelable{
    private int mLevel;
    private boolean mPlayable;
    private boolean mLocked;
    private float mRating;
    private int mColor;
    private int mNumStars = 3;

    public Level() {}

    public int getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = Color.parseColor(color);
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLocked(boolean locked){
        this.mLocked = locked;
    }

    public boolean isLocked(){
        return mLocked;
    }

    public void setLevel(int level) {
        this.mLevel = level;
    }

    public boolean isPlayable() {
        return mPlayable;
    }

    public void setPlayable(boolean playable) {
        this.mPlayable = playable;
    }

    public float getRating() {
        return mRating;
    }

    public void setRating(float rating) {
        this.mRating = rating;
    }

    public int getNumStars() {
        return mNumStars;
    }

    public void setNumStars(int numStars) {
        this.mNumStars = numStars;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mLevel);
        dest.writeByte((byte) (mPlayable ? 1 : 0));
        dest.writeByte((byte) (mLocked ? 1 : 0));
        dest.writeFloat(mRating);
        dest.writeInt(mColor);
        dest.writeInt(mNumStars);
    }

    private Level (Parcel in) {
        mLevel = in.readInt();
        mPlayable = in.readByte() != 0;
        mLocked = in.readByte() != 0;
        mRating = in.readFloat();
        mColor = in.readInt();
        mNumStars = in.readInt();
    }

    public static final Creator<Level> CREATOR = new Creator<Level>() {
        @Override
        public Level createFromParcel(Parcel source) {
            return new Level(source);
        }

        @Override
        public Level[] newArray(int size) {
            return new Level[size];
        }
    };
}
