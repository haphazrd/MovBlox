package com.haphazrd.movblox.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.haphazrd.movblox.Adapter.BloxAdapter;
import com.haphazrd.movblox.DataStorage.SettingsData;
import com.haphazrd.movblox.R;
import com.haphazrd.movblox.Utils.CountdownTimer;
import com.haphazrd.movblox.Utils.MovConstants;
import com.haphazrd.movblox.Utils.SoundFX;

/**
 * Created by brittanystubbs on 6/23/15.
 */
public class GameMenu {

    private RelativeLayout mMenuPopOut;
    private Button mCloseButton;

    private Switch mMusicSwitch;
    private Switch mSoundFXSwitch;
    private Switch mColorBlindSwitch;
    private Switch mTutorialSwitch;
    private Button mRestartButton;
    private Button mHomeButton;
    private ImageView mCatch;

    Context mContext;
    Activity mActivity;
    SoundFX mSoundFX;

    CountdownTimer timer;
    SettingsData settings;
    int livesLeft;


    public GameMenu(Context context, SoundFX soundFX){
        //pop out layout
        mContext = context;
        mActivity = (Activity) context;
        soundFX = new SoundFX(context);
        mSoundFX = soundFX;

        mMenuPopOut = (RelativeLayout) mActivity.findViewById(R.id.menuPopout);
        mCloseButton = (Button) mActivity.findViewById(R.id.closeMenuButton);
        mMusicSwitch = (Switch) mActivity.findViewById(R.id.musicSwitch);
        mSoundFXSwitch = (Switch) mActivity.findViewById(R.id.soundFXSwitch);
        mColorBlindSwitch = (Switch) mActivity.findViewById(R.id.colorBlindSwitch);
        mTutorialSwitch = (Switch) mActivity.findViewById(R.id.tutorialSwitch);
        mRestartButton = (Button) mActivity.findViewById(R.id.restart);
        mHomeButton = (Button) mActivity.findViewById(R.id.goHome);
        mCatch = (ImageView) mActivity.findViewById(R.id.touchCatch);

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "squares_bold_free-webfont.ttf");
        mHomeButton.setTypeface(font);
        mMusicSwitch.setTypeface(font);
        mSoundFXSwitch.setTypeface(font);
        mColorBlindSwitch.setTypeface(font);
        mRestartButton.setTypeface(font);
        mHomeButton.setTypeface(font);
        mCloseButton.setTypeface(font);
        mTutorialSwitch.setTypeface(font);

        mMusicSwitch.setSwitchTypeface(font);
        mSoundFXSwitch.setSwitchTypeface(font);
        mColorBlindSwitch.setSwitchTypeface(font);
        mTutorialSwitch.setSwitchTypeface(font);
    }

    public void showMenu(CountdownTimer currentTimer, final BloxAdapter adapter, int lives) {
        timer = currentTimer;
        settings = new SettingsData(mContext);
        livesLeft = lives;
                if (MovConstants.GAME_STATE.equals("play")) {
                    //stop timer
                    timer.cancelTimer();
                    mCatch.setVisibility(View.VISIBLE);
                    mMenuPopOut.setVisibility(View.VISIBLE);
                    Animation menuAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in);
                    mMenuPopOut.startAnimation(menuAnimation);
                    MovConstants.GAME_STATE = "paused";
                    //change settings
                    mMusicSwitch.setChecked(settings.getMusic());
                    mSoundFXSwitch.setChecked(settings.getSoundFX());
                    mColorBlindSwitch.setChecked(settings.getColorBlind());
                    mTutorialSwitch.setChecked(settings.getTutorial());

                    mMusicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            mSoundFX.playSound(0, 0.8f, 0);
                            settings.setMusic(isChecked);
                        }
                    });

                    mSoundFXSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            mSoundFX.playSound(0, 0.8f, 0);
                            settings.setSoundFX(isChecked);
                        }
                    });

                    mColorBlindSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            mSoundFX.playSound(0, 0.8f, 0);
                            settings.setColorBlind(isChecked);
                            adapter.notifyDataSetChanged();
                            colorBlindChange();
                        }
                    });

                    mTutorialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            mSoundFX.playSound(0, 0.8f, 0);
                            settings.setTutorial(isChecked);
                        }
                    });

                    mHomeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSoundFX.playSound(0, 0.8f, 0);
                            Intent intent = new Intent(mContext, TitleActivity.class);
                            mActivity.finish();
                            mActivity.startActivity(intent);
                            mCatch.setVisibility(View.INVISIBLE);
                            mActivity.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                        }
                    });


                    mRestartButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSoundFX.playSound(0, 0.8f, 0);
                            Animation menuAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_back_out);
                            mMenuPopOut.startAnimation(menuAnimation);
                            menuAnimation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    mMenuPopOut.setVisibility(View.INVISIBLE);
                                    mCatch.setVisibility(View.INVISIBLE);
//                                    PopOuts popOut = new PopOuts(mContext, mActivity);
//                                    popOut.showRestartPopOut(livesLeft, timer);
                                    DialogEndGame dialog = new DialogEndGame(mContext, mSoundFX);
                                    dialog.showRestartPopOut(livesLeft, timer);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                        }

//                            //CHANGE LIVES LEFT
//                            livesLeft=livesLeft-1;
//
//                            getLivesLeft(livesLeft);
//
                    });

                    mCloseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSoundFX.playSound(0, 0.8f, 0);
                            Animation menuAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_back_out);
                            mMenuPopOut.startAnimation(menuAnimation);
                            menuAnimation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    MovConstants.GAME_STATE = "play";
                                    timer.startTimer(livesLeft);
                                    mMenuPopOut.setVisibility(View.INVISIBLE);
                                    mCatch.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                        }
                    });

                    mCatch.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
                }

            }

    //change items for colorblind mode
    public void colorBlindChange(){
        RelativeLayout background = (RelativeLayout) mActivity.findViewById(R.id.playBackground);
        boolean isColorBlind = settings.getColorBlind();

        if(isColorBlind){
            background.setBackgroundColor(Color.WHITE);
        } else {
            background.setBackgroundColor(Color.parseColor("#333333"));
        }
    }

}
