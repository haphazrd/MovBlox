package com.haphazrd.movblox.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.haphazrd.movblox.Adapter.LevelAdapter;
import com.haphazrd.movblox.Blox.Level;
import com.haphazrd.movblox.R;
import com.haphazrd.movblox.Utils.SoundFX;

import java.util.Arrays;


public class SelectLevelActivity extends Activity {

    public Level[] mLevels;
    public RecyclerView mRecyclerView;
    public SoundFX mSoundFX;
    public RelativeLayout mLevelBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_world);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLevelBg = (RelativeLayout) findViewById(R.id.levelRectangle);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(TitleActivity.LEVEL);
        mLevels = Arrays.copyOf(parcelables, parcelables.length, Level[].class);

        LevelAdapter adapter = new LevelAdapter(this, mLevels, mSoundFX);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //animate activity transition on back press
        finish();
        overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSoundFX.resumeSounds();
    }

    //action bar option animate
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            // ...

            // up button
            case android.R.id.home:
                mSoundFX.playSound(0, 0.8f, 0);
                finish();
                overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mSoundFX = new SoundFX(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSoundFX.stopSounds();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSoundFX.pauseSounds();
    }
}
