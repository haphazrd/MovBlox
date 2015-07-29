package com.haphazrd.movblox.Adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haphazrd.movblox.Blox.Blox;
import com.haphazrd.movblox.Blox.Level;
import com.haphazrd.movblox.R;
import com.haphazrd.movblox.UI.PlayActivity;
import com.haphazrd.movblox.Utils.SoundFX;

/**
 * Created by brittanystubbs on 5/20/15.
 */
public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.LevelViewHolder> {

    private Level[] mLevels;
    private Context mContext;
    private Activity mActivity;
    public final static String CURRENT_LEVEL  = "CURRENT_LEVEL";
    public final static String CURRENT_PLAY  = "CURRENT_PLAY";
    protected ObjectAnimator animBloxMan;
    ObjectAnimator animPlay;
    SoundFX mSoundFX;


    public LevelAdapter(Context context, Level[] levels, SoundFX soundFX) {
        mContext = context;
        mActivity = (Activity) context;
        mLevels = levels;
        mSoundFX = soundFX;
    }

    @Override
    public LevelViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.level_list_item, parent, false);
        LevelViewHolder holder = new LevelViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(LevelViewHolder holder, int i) {
        holder.bindLevel(mLevels[i]);
    }

    @Override
    public int getItemCount() {
        return mLevels.length;
    }

    public class LevelViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{

        //do not move
        public RatingBar mRatingBar;
        public TextView mLevelNum;
        public TextView mLocked;
        public TextView mPlayButton;
        public ImageView mBloxMan;
        public RelativeLayout mLevelBg;

        //view holder
        public LevelViewHolder(View itemView) {
            super(itemView);
            mRatingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            mLevelNum = (TextView) itemView.findViewById(R.id.levelNumber);
            mLocked = (TextView) itemView.findViewById(R.id.lockedText);
            mPlayButton = (TextView) itemView.findViewById(R.id.playLevelButton);
            mBloxMan = (ImageView) itemView.findViewById(R.id.bloxManLevels);
            mLevelBg = (RelativeLayout) itemView.findViewById(R.id.levelRectangle);

            itemView.setOnClickListener(this);
        }

        //bind data to current showing in listview
        public void bindLevel(Level level) {
            mRatingBar.setRating(level.getRating());
            mRatingBar.setNumStars(level.getNumStars());
            mLevelNum.setText(String.valueOf(level.getLevel()));

            //custom font
            Typeface font = Typeface.createFromAsset(mContext.getAssets(), "squares_bold_free-webfont.ttf");
            mLevelNum.setTypeface(font);
            mLocked.setTypeface(font);
            mPlayButton.setTypeface(font);

            GradientDrawable shapeDrawable = (GradientDrawable) mLevelBg.getBackground();
            shapeDrawable.setColor(level.getColor());

            if (level.isLocked()) {
                mLocked.setVisibility(View.VISIBLE);
                mBloxMan.setVisibility(View.INVISIBLE);
                mPlayButton.setVisibility(View.INVISIBLE);


            } else {
                mLocked.setVisibility(View.INVISIBLE);
                if (level.isPlayable()) {
                    mBloxMan.setVisibility(View.VISIBLE);
                    mPlayButton.setVisibility(View.VISIBLE);

                }

//                animBloxMan = ObjectAnimator.ofFloat(mBloxMan, "translationY", 0, -40);
//                animBloxMan.setDuration(3000);
//                animBloxMan.setRepeatMode(2);
//                animBloxMan.setRepeatCount(-1);
//                animBloxMan.start();
//
//                //animate title
//                animPlay = ObjectAnimator.ofFloat(mPlayButton, "alpha", 0.2f, 1f);
//                animPlay.setDuration(1000);
//                animPlay.setRepeatMode(2);
//                animPlay.setRepeatCount(-1);
//                animPlay.start();
            }
            //set play button and bloxImages man later
        }

        @Override
        public void onClick(View v) {
            //check if locked
          //  mSoundFX.playSound(0, 0.8f, 0);
            int position = getAdapterPosition();
            boolean currentLocked = mLevels[position].isLocked();

            if(!currentLocked) {
                //go to play selected level, attach blox data
                Intent intent = new Intent(mContext, PlayActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelableArray(CURRENT_LEVEL, getBlox(mLevels[position].getLevel()));
                extras.putInt(CURRENT_PLAY, mLevels[position].getLevel());
                intent.putExtras(extras);
                mContext.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        }
    }

    //set bloxes for level
    private Blox[] getBlox(int level) {

        Blox bloxCount = new Blox();
        int numBlox = bloxCount.numBlox(level);

        Blox[] bloxes = new Blox[numBlox];

        for (int i = 0; i < numBlox; i++) {
            Blox blox = new Blox();
            blox.setBloxColor(level, i);
            blox.setBloxName(blox.getBloxColor());

            bloxes[i] = blox;
        }
        return bloxes;
    }
}
