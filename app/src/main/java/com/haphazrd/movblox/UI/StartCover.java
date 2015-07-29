package com.haphazrd.movblox.UI;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haphazrd.movblox.R;
import com.haphazrd.movblox.Utils.ColorChanger;
import com.haphazrd.movblox.Utils.CountdownTimer;
import com.haphazrd.movblox.Utils.MovConstants;

/**
 * Created by brittanystubbs on 6/29/15.
 */
public class StartCover {
    protected RelativeLayout mOverlay;
    protected TextView mLevelText;
    protected TextView mTouchText;
    protected ImageView mBloxMan;
    public Typeface font;

    public StartCover(Activity activity, int level, final CountdownTimer timer, final int lives){
        mOverlay = (RelativeLayout) activity.findViewById(R.id.playOverlay);
        mLevelText = (TextView) activity.findViewById(R.id.levelNumberOverlay);
        mTouchText = (TextView) activity.findViewById(R.id.pressToPlay);
        mBloxMan = (ImageView) activity.findViewById(R.id.bloxManOverlay);

        font = Typeface.createFromAsset(activity.getAssets(), "squares_bold_free-webfont.ttf");
        mLevelText.setTypeface(font);
        mTouchText.setTypeface(font);

        //change color background randomly
        int color = ColorChanger.getColor();
        mOverlay.setBackgroundColor(color);
        mOverlay.setVisibility(View.VISIBLE);

        mLevelText.setText("LEVEL " + level);

        //animate touch text
        final ObjectAnimator animText =  ObjectAnimator.ofFloat(mTouchText, "alpha", 0.2f, 1f);
        animText.setDuration(1000);
        animText.setRepeatMode(2);
        animText.setRepeatCount(-1);
        animText.start();

        //animate bloxman
        final ObjectAnimator anim =  ObjectAnimator.ofFloat(mBloxMan, "translationY", -20, 20);
        anim.setDuration(2000);
        anim.setRepeatMode(2);
        anim.setRepeatCount(-1);
        anim.start();

        mBloxMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOverlay.setVisibility(View.INVISIBLE);

                if (mOverlay.getVisibility() == View.INVISIBLE) {
                    timer.startTimer(lives);
                    MovConstants.GAME_STATE = "play";
                }
                animText.cancel();
                anim.cancel();
            }
        });

        mOverlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

}
