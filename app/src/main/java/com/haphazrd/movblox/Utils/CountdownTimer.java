package com.haphazrd.movblox.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haphazrd.movblox.DataStorage.GameLivesData;
import com.haphazrd.movblox.DataStorage.SettingsData;
import com.haphazrd.movblox.R;
import com.haphazrd.movblox.UI.DialogEndGame;
import com.haphazrd.movblox.UI.Lives;
import com.haphazrd.movblox.UI.PlayActivity;

/**
 * Created by brittanystubbs on 6/16/15.
 */
public class CountdownTimer {


    private CountDownTimer startCountDownLives;
    protected Context mContext;
    protected Activity mActivity;
    protected SoundFX mSoundFX;
    protected GameLivesData mGameLivesData;
    protected  String time;
    protected TextView mTimeText;
    protected TextView mLivesTimeText;
    protected TextView mWaitTitleText;
    protected TextView mWaitDescText;
    protected Button mLivesHomeButton;
    protected Button mCollectLives;
    protected LinearLayout mOutLayout;
    protected long milli;
    protected long timeLeftInt;
    protected int lives;
    private SettingsData settings;

    private static final String TAG = PlayActivity.class.getSimpleName();

    public CountdownTimer(Context context, SoundFX soundFX){
        mContext = context;
        mActivity = (Activity) context;
        mSoundFX = soundFX;

        settings = new SettingsData(mContext);
    }

    public long convertToMilli(String timer){
        String[] parts = timer.split(":");
        int minute = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);

        return ((minute * 60) + seconds) * 1000;
    }

    public String convertToString(long milli){

       // long toSeconds = milli / 1000;

        int seconds = (int) (milli / 1000) % 60 ;
        int minutes = (int) ((milli / 1000 ) / 60);

        return String.format("%d:%02d",
                minutes,
                seconds);
    }

//timer for more lives when you lose
    public void outOfLivesTimer(TextView timeText, Dialog dialog){
        mWaitDescText = (TextView) dialog.findViewById(R.id.comeBack);
        mWaitTitleText = (TextView) dialog.findViewById(R.id.waitGameText);
        mLivesHomeButton = (Button) dialog.findViewById(R.id.waitHomeButton);
        mCollectLives = (Button) dialog.findViewById(R.id.collectLives);
        mOutLayout = (LinearLayout) dialog.findViewById(R.id.outButtonLayout);

        mLivesTimeText = timeText;
        time = convertToString(settings.getTime());
        timeLeftInt = settings.getTime();
       // Toast.makeText(mContext, settings.timerIsGoing() + "" , Toast.LENGTH_SHORT).show();
        startCountDownLives = new CountDownTimer(timeLeftInt, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mLivesTimeText.setText(convertToString(millisUntilFinished));

                if(millisUntilFinished / 1000 == 1){
                    mLivesTimeText.setText("0:00");
                    mWaitDescText.setText("Get more lives");
                    mWaitTitleText.setText("Collect 3 lives!");
                    mOutLayout.setVisibility(View.INVISIBLE);
                    mCollectLives.setVisibility(View.VISIBLE);
                    mContext.getSharedPreferences("WAIT_TIME", 0).edit().clear().commit();
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    public void cancelOutOfLivesTimer(){
        if(startCountDownLives !=null) {
            startCountDownLives.cancel();
            startCountDownLives = null;
        }
    }

            CountDownTimer playTimer;
            public void startTimer(int livesData) {
                lives = livesData;
                mTimeText = (TextView) mActivity.findViewById(R.id.triesText);
                milli = MovConstants.TIME_LEFT;
                playTimer = new CountDownTimer(milli, 1000) {
                    public void onTick(long millisUntilFinished) {
                        //on countdown
                        mTimeText.setText(convertToString(millisUntilFinished));
                        MovConstants.TIME_LEFT = millisUntilFinished;
                        if(millisUntilFinished / 1000 == 1){
                            mGameLivesData = new GameLivesData(mContext);
                            //done
                            //remove life
                            mTimeText.setText("0:00");
                            MovConstants.GAME_STATE = "lostLife";

                            //set lives left
                            Lives livesLeft = new Lives(mContext, mActivity, settings, mSoundFX);
                            lives = livesLeft.setLivesLeft(lives);

//                            PopOuts popOut = new PopOuts(mContext, mActivity);
                            DialogEndGame dialog = new DialogEndGame(mContext, mSoundFX);

                                 if (lives > 0) {
                                    //popOut.showPlayAgain("Oh no! You ran out of time!", lives);
                                     dialog.lostLifeDialog("Oh no! You ran out of time!", lives);
                                }else {
                                    // popOut.showWait(playTimer);
                                }
                            }

                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            }

            public void cancelTimer(){

                if(playTimer !=null) {
                    Log.e(TAG, playTimer + " timer");
                    playTimer.cancel();
                    playTimer = null;
                }
            }
        }

