package com.haphazrd.movblox.UI;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.haphazrd.movblox.DataStorage.SettingsData;
import com.haphazrd.movblox.R;
import com.haphazrd.movblox.Utils.CountdownTimer;
import com.haphazrd.movblox.Utils.MovConstants;
import com.haphazrd.movblox.Utils.SoundFX;


/**
 * Created by brittanystubbs on 6/24/15.
 */
public class Lives {
    private ImageView mLife3;
    private ImageView mLife2;
    private ImageView mLife1;
    private Context mContext;
    public Activity mActivity;
    public SoundFX mSoundFX;
    private SettingsData mSettings;
    public CountdownTimer mTimer;

    public Lives(Context context, Activity activity, SettingsData settingsData, SoundFX soundFX) {
        mContext = context;
        mActivity = activity;
        mSettings = settingsData;
        mSoundFX = soundFX;

        mLife3 = (ImageView) activity.findViewById(R.id.bloxLifeThree);
        mLife2 = (ImageView) activity.findViewById(R.id.bloxLifeTwo);
        mLife1 = (ImageView) activity.findViewById(R.id.bloxLifeOne);
    }

    public int setLivesLeft(int lives) {
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_down_anim);
        switch (lives) {
            case 3:
                lives = lives - 1;
                mSettings.setLives(lives);
                mLife1.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mLife1.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case 2:
                lives = lives - 1;
                mSettings.setLives(lives);
                mLife2.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mLife2.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case 1:
                lives = lives - 1;
                mSettings.setLives(lives);
                MovConstants.GAME_STATE = "gameOver";
                mLife3.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mLife3.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            default:
                break;
        }
        return lives;
    }

    public void getLivesLeft(int lives, CountdownTimer timer) {
        mTimer = timer;
        switch (lives) {
            case 3:
                mContext.getSharedPreferences("WAIT_TIME", 0).edit().clear().commit();
                break;
            case 2:
                mLife1.setVisibility(View.INVISIBLE);
                break;
            case 1:
                mLife2.setVisibility(View.INVISIBLE);
                mLife1.setVisibility(View.INVISIBLE);
                break;
            case 0:
                mLife3.setVisibility(View.INVISIBLE);
                mLife2.setVisibility(View.INVISIBLE);
                mLife1.setVisibility(View.INVISIBLE);
                MovConstants.GAME_STATE = "gameOver";

//                PopOuts popOut = new PopOuts(mContext, mActivity);
//                popOut.showWait(timer);
                DialogEndGame dialog = new DialogEndGame(mContext, mSoundFX);
                dialog.showWait(timer);
                break;
            default:
                break;
        }
    }
}
