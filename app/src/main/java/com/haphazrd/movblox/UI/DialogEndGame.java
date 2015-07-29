package com.haphazrd.movblox.UI;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haphazrd.movblox.Blox.Play;
import com.haphazrd.movblox.DataStorage.LevelData;
import com.haphazrd.movblox.DataStorage.SettingsData;
import com.haphazrd.movblox.R;
import com.haphazrd.movblox.Utils.CountdownTimer;
import com.haphazrd.movblox.Utils.MovConstants;
import com.haphazrd.movblox.Utils.SoundFX;

/**
 * Created by brittanystubbs on 7/15/15.
 */
public class DialogEndGame extends Dialog{
    public Context mContext;
    public Activity mActivity;
    public SoundFX mSoundFX;

    public RelativeLayout mBackground;

    public TextView mLivesLeftText;
    public TextView mGameOverText;
    public Button mHomeButton;
    public Button mPlayAgainButton;

    //wait for lives
    public TextView mWaitTitleText;
    public TextView mWaitDescText;
    public TextView mTimerText;
    public Button mBuyLives;
    public LinearLayout mOutLayout;

    //won dialog
    public RatingBar mRating;
    public TextView mWonText;
    public TextView mScore;

    public Typeface font;
    public Dialog dialog;
    public CountdownTimer mTimer;
    public CountDownTimer startCountdown; //score animation timer
    public Lives lives;
    public int score;
    public String wonText = "Congrats!";
    int startingScore = 0;
    protected int levelScore = 50; //points per remaining second

    //restart dialog
    public Button mCancelButton;
    public TextView mRestartText;

    public DialogEndGame(Context context, SoundFX soundFX) {
        super(context);
        mContext = context;
        mActivity = (Activity) context;
        mSoundFX = soundFX;

        font = Typeface.createFromAsset(context.getAssets(), "squares_bold_free-webfont.ttf");
        MovConstants.GAME_STATE = "gameOver";
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.Animation);
        dialog.setContentView(R.layout.dialog_custom);
        dialog.setCancelable(false);
    }

        //restart game
    public void showRestartPopOut(final int lives, final CountdownTimer timer) {

        mBackground = (RelativeLayout) dialog.findViewById(R.id.restartBackground);
        mCancelButton = (Button) dialog.findViewById(R.id.cancelRestartButton);
        mPlayAgainButton = (Button) dialog.findViewById(R.id.restartButton);
        mRestartText = (TextView) dialog.findViewById(R.id.restartGameText);

        mCancelButton.setTypeface(font);
        mPlayAgainButton.setTypeface(font);
        mRestartText.setTypeface(font);

        mBackground.setVisibility(View.VISIBLE);
        dialog.show();

        mPlayAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mPlayAgainPopOut.setVisibility(View.INVISIBLE);
                SettingsData settings = new SettingsData(mContext);
                Lives livesData = new Lives(mContext, mActivity, settings, mSoundFX);
                livesData.setLivesLeft(lives);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mActivity.recreate();
                } else {
                    final Intent intent = mActivity.getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mActivity.finish();
                    mActivity.overridePendingTransition(0, 0);
                    mActivity.startActivity(intent);
                    mActivity.overridePendingTransition(0, 0);
                }
                dialog.dismiss();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        MovConstants.GAME_STATE = "play";
                        timer.startTimer(lives);
                    }
                });
                dialog.dismiss();
            }
        });

    }

    //lost life popup
    public void lostLifeDialog(String gameOverText, int lives){

        mBackground = (RelativeLayout) dialog.findViewById(R.id.playAgainBackground);
        mLivesLeftText = (TextView) dialog.findViewById(R.id.livesLeftText);
        mGameOverText = (TextView) dialog.findViewById(R.id.gameOverText);
        mHomeButton = (Button) dialog.findViewById(R.id.homePlayButton);
        mPlayAgainButton = (Button) dialog.findViewById(R.id.playAgainButton);

        mLivesLeftText.setTypeface(font);
        mHomeButton.setTypeface(font);
        mPlayAgainButton.setTypeface(font);
        mGameOverText.setTypeface(font);

        mGameOverText.setText(gameOverText);
        mLivesLeftText.setText("Lives left: " + lives);
        mBackground.setVisibility(View.VISIBLE);
        Animation animatePopOut = AnimationUtils.loadAnimation(mContext, R.anim.popout_show);
        mBackground.startAnimation(animatePopOut);
        dialog.show();

        mPlayAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mPlayAgainPopOut.setVisibility(View.INVISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mActivity.recreate();
                } else {
                    final Intent intent = mActivity.getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mActivity.finish();
                    mActivity.overridePendingTransition(0, 0);
                    mActivity.startActivity(intent);
                    mActivity.overridePendingTransition(0, 0);
                }
                dialog.dismiss();
            }
        });

        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, TitleActivity.class);
                mActivity.finish();
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                dialog.dismiss();
            }
        });
    }

    //wait for more lives
    public void showWait(CountdownTimer timer){
        mTimer = timer;
        //wait popout
        mBackground = (RelativeLayout) dialog.findViewById(R.id.waitBackground);
        mWaitTitleText = (TextView) dialog.findViewById(R.id.waitGameText);
        mWaitDescText = (TextView) dialog.findViewById(R.id.comeBack);
        mTimerText = (TextView) dialog.findViewById(R.id.timerText);
        mHomeButton = (Button) dialog.findViewById(R.id.waitHomeButton);
        mPlayAgainButton = (Button) dialog.findViewById(R.id.collectLives);
        mBuyLives = (Button) dialog.findViewById(R.id.buyLives);
        mOutLayout = (LinearLayout) dialog.findViewById(R.id.outButtonLayout);

        mWaitTitleText.setTypeface(font);
        mWaitDescText.setTypeface(font);
        mTimerText.setTypeface(font);
        mHomeButton.setTypeface(font);
        mPlayAgainButton.setTypeface(font);
        mBuyLives.setTypeface(font);

                mBackground.setVisibility(View.VISIBLE);
                Animation animatePopOut = AnimationUtils.loadAnimation(mContext, R.anim.popout_show);
                mBackground.startAnimation(animatePopOut);
                SettingsData settingsData = new SettingsData(mContext);
                //Toast.makeText(mContext, settingsData.getTime() + "", Toast.LENGTH_LONG).show();

                if (!settingsData.timerIsGoing()) {
                    settingsData.setTimerGoing(true);
                    settingsData.setTime();
                }

                mTimer.outOfLivesTimer(mTimerText, dialog);

        //if refreshed view when timer is over
                if (settingsData.getTime() <= 0 && settingsData.timerIsGoing()) {
                    mTimer.cancelOutOfLivesTimer();
                    mContext.getSharedPreferences("WAIT_TIME", 0).edit().clear().commit();

                    mWaitDescText.setText("Get more lives");
                    mWaitTitleText.setText("Collect 3 lives!");
                    mTimerText.setText("0:00");

                    mOutLayout.setVisibility(View.INVISIBLE);
                    mPlayAgainButton.setVisibility(View.VISIBLE);

                }
        dialog.show();

        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, TitleActivity.class);
                mActivity.finish();
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                dialog.dismiss();
            }
        });

        //collect lives when timer is done
        mPlayAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsData settingsData = new SettingsData(mContext);
                lives = new Lives(mContext, mActivity, settingsData, mSoundFX);
                settingsData.setLives(3);
                lives.getLivesLeft(settingsData.getLives(), mTimer);
                settingsData.setTimerGoing(false);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mActivity.recreate();
                } else {
                    final Intent intent = mActivity.getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mActivity.finish();
                    mActivity.overridePendingTransition(0, 0);
                    mActivity.startActivity(intent);
                    mActivity.overridePendingTransition(0, 0);
                }
                dialog.dismiss();
            }
        });
    }

    public void showWon(final int maxTime, final int timeLeft, int level) {
        mSoundFX.playSound(6, 0.8f, 0);
        mBackground = (RelativeLayout) dialog.findViewById(R.id.wonBackground);
        mRating = (RatingBar) dialog.findViewById(R.id.ratingBar);
        mWonText = (TextView) dialog.findViewById(R.id.wonGame);
        mHomeButton = (Button) dialog.findViewById(R.id.levelButton);
        mScore = (TextView) dialog.findViewById(R.id.scoreText);

        mWonText.setTypeface(font);
        mHomeButton.setTypeface(font);
        mScore.setTypeface(font);

        score = (timeLeft * levelScore) * 4;
        final int starRating = Math.round((timeLeft * levelScore) / maxTime);
        final int rating = setRating(starRating);
        mWonText.setText(wonText);
        mRating.setRating(rating);

        LevelData levelData = new LevelData(mActivity);

        //save rating
        if(levelData.getRating(level) < rating) {
            levelData.setRating(level, rating);
        }

                        mBackground.setVisibility(View.VISIBLE);
                        dialog.show();
                        mSoundFX.playSound(4, 0.6f, -1);
                        startCountdown = new CountDownTimer(score, 1) {
                            public void onTick(long millisUntilFinished) {
                                startingScore = startingScore + 1;
                                //on countdown
                                mScore.setText("Time Remaining: +" + Integer.toString(startingScore));
                            }
                            @Override
                            public void onFinish() {
                                mSoundFX.stopSound(4);
                                startingScore = 0;
                                mScore.animate().x(0).alpha(0).setDuration(1000).setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        mWonText.setAlpha(0);
                                        mWonText.setVisibility(View.VISIBLE);
                                        mWonText.animate().alpha(1).setDuration(1000);

                                        mRating.setAlpha(0);
                                        mRating.setVisibility(View.VISIBLE);
                                        mRating.animate().alpha(1).setDuration(1000);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });
                            }
                        }.start();

                mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mSoundFX.playSound(0, 0.6f, 0);
                    Intent intent = new Intent(mActivity, SelectLevelActivity.class);
                    intent.putExtra("LEVEL", Play.getLevels(mContext));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                    mActivity.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);

                    startCountdown.cancel();
                    dialog.dismiss();
                    dialog = null;
            }
        });
    }

    public int setRating(int score) {
        if (score > 35) {
            wonText = "Nice job!";
            return 3;
        } else if (score > 20) {
            wonText = "You can do better!";
            return 2;
        } else {
            wonText = "Not so great.";
            return 1;
        }
    }

    public void stopDialogActivities(){
        if(startCountdown != null) {
            startCountdown.cancel();
            startCountdown = null;
            mScore.setText("Time Remaining: +" + score);
        }
    }
    public void resumeDialogActivities(){
        if(startCountdown != null) {
            startCountdown.start();
        }
    }
}
