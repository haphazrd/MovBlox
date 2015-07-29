package com.haphazrd.movblox.UI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.haphazrd.movblox.DataStorage.SettingsData;
import com.haphazrd.movblox.R;
import com.haphazrd.movblox.Utils.SoundFX;


/**
 * Created by brittanystubbs on 6/29/15.
 */
public class GameSettings extends Activity{
    private Switch mMusicSwitch;
    private Switch mSoundFXSwitch;
    private Switch mColorBlindSwitch;
    private Switch mTutorialSwitch;
    protected Button mHomeButton;
    protected TextView mSettingText;

    private SettingsData settings;
    public SoundFX mSoundFX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // If the Android version is lower than Jellybean, use this call to hide
        // the status bar.

        settings = new SettingsData(this);

        //initialize variables
        mHomeButton = (Button) findViewById(R.id.homeButton);
        mMusicSwitch = (Switch) findViewById(R.id.musicSwitch);
        mSoundFXSwitch = (Switch) findViewById(R.id.soundFXSwitch);
        mColorBlindSwitch = (Switch) findViewById(R.id.colorBlindSwitch);
        mTutorialSwitch = (Switch) findViewById(R.id.tutorialSwitch);
        mSettingText = (TextView) findViewById(R.id.movSettingText);

        //change settings
        mMusicSwitch.setChecked(settings.getMusic());
        mSoundFXSwitch.setChecked(settings.getSoundFX());
        mColorBlindSwitch.setChecked(settings.getColorBlind());
        mTutorialSwitch.setChecked(settings.getTutorial());


        Typeface font = Typeface.createFromAsset(this.getAssets(), "squares_bold_free-webfont.ttf");
        mHomeButton.setTypeface(font);
        mMusicSwitch.setTypeface(font);
        mSoundFXSwitch.setTypeface(font);
        mColorBlindSwitch.setTypeface(font);
        mSettingText.setTypeface(font);
        mTutorialSwitch.setTypeface(font);

        mMusicSwitch.setSwitchTypeface(font);
        mSoundFXSwitch.setSwitchTypeface(font);
        mColorBlindSwitch.setSwitchTypeface(font);
        mTutorialSwitch.setSwitchTypeface(font);

        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSoundFX.playSound(0, 0.8f, 0);
                Intent intent = new Intent(GameSettings.this, TitleActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
            }
        });

        mMusicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSoundFX.playSound(0, 0.8f, 0);
                settings.setMusic(isChecked);
                //Toast.makeText(GameSettings.this, "MUSIC " + isChecked + "", Toast.LENGTH_LONG).show();
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
            }
        });

        mTutorialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSoundFX.playSound(0, 0.8f, 0);
                settings.setTutorial(isChecked);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //animate activity transition on back press
        finish();
        overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSoundFX.stopSounds();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSoundFX = new SoundFX(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSoundFX.pauseSounds();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSoundFX.resumeSounds();
    }
}
