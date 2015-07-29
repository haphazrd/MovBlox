package com.haphazrd.movblox.UI;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haphazrd.movblox.Blox.Play;
import com.haphazrd.movblox.R;
import com.haphazrd.movblox.Utils.ColorChanger;
import com.haphazrd.movblox.Utils.GoogleServicesHelper;
import com.haphazrd.movblox.Utils.SoundFX;

public class TitleActivity extends Activity implements GoogleServicesHelper.GoogleServicesListener {

    public static final String LEVEL = "LEVEL";

    protected TextView mTitleText;
    protected TextView mPlayText;
    protected TextView mSettingText;
    protected TextView mLeaderBoardText;
    protected RelativeLayout mMenuLayout;
    protected ImageView mBloxMan;
    protected ObjectAnimator anim;
    protected ObjectAnimator animPlay;
    protected AnimatorSet set;
    public SoundFX soundfx;


    private boolean isGooglePlayServicesAvailable;
    private GoogleServicesHelper googleServicesHelper;
    private static final int REQUEST_ACHIEVEMENTS = -102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // If the Android version is lower than Jellybean, use this call to hide
        // the status bar.
        this.googleServicesHelper = new GoogleServicesHelper(this, this);
        this.isGooglePlayServicesAvailable = false;

        //initialize views
        mTitleText = (TextView) findViewById(R.id.titleText);
        mPlayText = (TextView) findViewById(R.id.playButton);
        mSettingText = (TextView) findViewById(R.id.settingsButton);
        mLeaderBoardText = (TextView) findViewById(R.id.leaderButton);
        mMenuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
        mBloxMan = (ImageView) findViewById(R.id.bloxMan);

        //custom font
        Typeface font = Typeface.createFromAsset(getAssets(), "squares_bold_free-webfont.ttf");
        mTitleText.setTypeface(font);
        mPlayText.setTypeface(font);
        mSettingText.setTypeface(font);
        mLeaderBoardText.setTypeface(font);

        //change color each page refresh
        int color = ColorChanger.getColor();
        mMenuLayout.setBackgroundColor(color);
        //on text click go to new page
        mPlayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundfx.playSound(0, 0.8f, 0);
                Intent intent = new Intent(TitleActivity.this, SelectLevelActivity.class);
                intent.putExtra(LEVEL, Play.getLevels(TitleActivity.this));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        });

        //go to settings page
        mSettingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change activity and animate it
                soundfx.playSound(0, 0.8f, 0);
                Intent intent = new Intent(TitleActivity.this, GameSettings.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        mLeaderBoardText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundfx.playSound(0, 0.8f, 0);
                Toast.makeText(TitleActivity.this, isGooglePlayServicesAvailable + "", Toast.LENGTH_LONG).show();
//                if(isGooglePlayServicesAvailable){
//                    startActivityForResult(Games.Achievements.getAchievementsIntent(googleServicesHelper.getApi()), REQUEST_ACHIEVEMENTS);
//                }
            }
        });

        //animate cover blox
        anim =  ObjectAnimator.ofFloat(mBloxMan, "translationX", -100, 100);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setDuration(3000);
        anim.setRepeatCount(-1);

        //animate title
        animPlay =  ObjectAnimator.ofFloat(mPlayText, "alpha", 0.2f, 1f);
        animPlay.setRepeatMode(Animation.REVERSE);
        animPlay.setDuration(1000);
        animPlay.setRepeatCount(-1);

        set = new AnimatorSet();
        set.playTogether(anim, animPlay);
    }

    //on google services connected
    @Override
    public void onConnected() {
        Toast.makeText(TitleActivity.this,  "connected", Toast.LENGTH_LONG).show();
        isGooglePlayServicesAvailable = true;
    }

    @Override
    public void onDisconnected() {
       // Toast.makeText(TitleActivity.this,  "disconnected", Toast.LENGTH_LONG).show();
        isGooglePlayServicesAvailable = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleServicesHelper.connect();
        soundfx = new SoundFX(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleServicesHelper.disconnect();
        soundfx.stopSounds();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleServicesHelper.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        set.start();
        soundfx.resumeSounds();

    }

    @Override
    protected void onPause() {
        super.onPause();
        set.cancel();
        soundfx.pauseSounds();
    }

}
