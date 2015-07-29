package com.haphazrd.movblox.UI;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haphazrd.movblox.Adapter.BloxAdapter;
import com.haphazrd.movblox.Adapter.LevelAdapter;
import com.haphazrd.movblox.Blox.Blox;
import com.haphazrd.movblox.Blox.Move;
import com.haphazrd.movblox.Blox.Play;
import com.haphazrd.movblox.DataStorage.LevelData;
import com.haphazrd.movblox.DataStorage.SettingsData;
import com.haphazrd.movblox.R;
import com.haphazrd.movblox.Utils.CountdownTimer;
import com.haphazrd.movblox.Utils.MovConstants;
import com.haphazrd.movblox.Utils.SoundFX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PlayActivity extends Activity {

    public TextView mLevelText;
    public TextView mTriesText;
    public ImageView mMenuButton;

    //countdown timer
    CountdownTimer timer;
    //lives
    Lives lives;

    BloxAdapter adapter;

    private String gameOverText = "Game Over";

    private Blox[] mBloxes;
    private int mLevel;
    public int widthOfBlox;
    private long mInitialTime; //time for level for score
    private int livesLeft;
    private int leftBloxX;
    private int rightBloxX;

    SettingsData settings;
    SoundFX mSoundFX;
    DialogEndGame dialog;

    public static final String TAG = PlayActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        MovConstants.GAME_STATE = "start";
        final GridView gridView = (GridView) findViewById(R.id.gridBlox);

        //set up adapter
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Parcelable[] parcelables = extras.getParcelableArray(LevelAdapter.CURRENT_LEVEL);
        mBloxes = Arrays.copyOf(parcelables, parcelables.length, Blox[].class);
        mLevel = extras.getInt(LevelAdapter.CURRENT_PLAY);

        final List<Blox> arrayList = new ArrayList<Blox>();

        for(Blox a : mBloxes){
            arrayList.add(a);
        }



        Blox bloxManClass = new Blox();
        MovConstants.CURRENT_BLOX_MAN_POS = bloxManClass.getBloxManPos(mLevel);

        //Toast.makeText(PlayActivity.this, bloxManClass.getBloxManPos(mLevel) + "", Toast.LENGTH_SHORT).show();

        adapter = new BloxAdapter(this, arrayList);
        gridView.setAdapter(adapter);

        //"actionbar items"
        mLevelText = (TextView) findViewById(R.id.levelPlayText);
        mTriesText = (TextView) findViewById(R.id.triesText);
        mMenuButton = (ImageView) findViewById(R.id.playMenuButton);

        //custom font
        Typeface font = Typeface.createFromAsset(getAssets(), "squares_bold_free-webfont.ttf");
        mLevelText.setTypeface(font);
        mTriesText.setTypeface(font);

        //set level
        mLevelText.setText("LEVEL " + mLevel);
        //set timeLeft
        Play play = new Play();
        final int columnNum = play.getCols(mLevel);

        mTriesText.setText(play.getTime(mLevel));

        //pull saved number
        settings = new SettingsData(PlayActivity.this);

        //time related
        timer = new CountdownTimer(this, mSoundFX);
        MovConstants.TIME_LEFT = timer.convertToMilli(play.getTime(mLevel));
        mInitialTime = MovConstants.TIME_LEFT;

        this.getSharedPreferences(this.getString(R.string.settings_data), 0).edit().clear().commit();
        //add lives if timer is going
        livesLeft = settings.getLives();
        lives = new Lives(this, this, settings, mSoundFX);
        lives.getLivesLeft(livesLeft, timer);

        //set touching blox position
        MovConstants.TOUCHED_BLOX_POSITION = 0;


        //"start game" overlay
        if(MovConstants.GAME_STATE.equals("start")){
            TutorialPopUp tutorial = new TutorialPopUp(this);
            switch (mLevel) {
                case 1 :
                    //first
                    //if tutorial "ON" in settings
                    if(settings.getTutorial()) {
                        tutorial.showPopUp(timer, livesLeft, "first");
                        //or show standard layout
                    }else {
                        new StartCover(this, mLevel, timer, livesLeft);
                    }
                    break;
                case 3:
                    //dead blox
                    if(settings.getTutorial()) {
                        tutorial.showPopUp(timer, livesLeft, "deadblox");
                    }else {
                        new StartCover(this, mLevel, timer, livesLeft);
                    }
                    break;
                case 4:
                    //flamo blox
                    if(settings.getTutorial()) {
                        tutorial.showPopUp(timer, livesLeft, "flamoblox");
                    }else {
                        new StartCover(this, mLevel, timer, livesLeft);
                    }
                    break;
                default:
                    //standard overlay
                new StartCover(this, mLevel, timer, livesLeft);
                    //Toast.makeText(this, "STANDARD", Toast.LENGTH_LONG).show();
                    break;
            }
        }

        //set gridview columns
        gridView.setNumColumns(columnNum);
        final ViewGroup.LayoutParams params = gridView.getLayoutParams();


        //set gridview width dynamically
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;
       // Toast.makeText(PlayActivity.this, getBloxSizeForScreen() + "", Toast.LENGTH_SHORT).show();
        int px = (int) Math.ceil(((play.getCols(mLevel) * getBloxSizeForScreen()[0]) + (getBloxSizeForScreen()[1] * columnNum)) * logicalDensity);
        params.width = px;

        widthOfBlox = (int) Math.ceil(((play.getCols(mLevel) * getBloxSizeForScreen()[0]) + (getBloxSizeForScreen()[1] * columnNum)));


        //change background color depending on if colorblind
        colorBlindChange();

        //on menu button press
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSoundFX.pauseSounds();
                mSoundFX.playSound(6, 0.8f, 0);
                Log.e(TAG, MovConstants.GAME_STATE + " state");
                GameMenu menu = new GameMenu(PlayActivity.this, mSoundFX);
                menu.showMenu(timer, adapter, livesLeft);
            }
        });


                  //on blox press
                  gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()

                  {
                      @Override
                      public void onItemClick(AdapterView<?> parent, View view, int position,
                                              long id) {
                          //find x positions of first and last blox to tell if end of row
                          //dont allow movement if between animation
                          if (!MovConstants.IS_MOVING) {
                              leftBloxX = Math.round(parent.getChildAt(0).getX());
                              rightBloxX = Math.round(parent.getChildAt(arrayList.size() - 1).getX());

                              Move move = new Move();
                              //if it is a flamoblox, turn bloxes around into flamo blox
                              if (arrayList.get(position).getBloxName().equals("flamoBlox")) {
                                  //Toast.makeText(PlayActivity.this, "FLAME ON", Toast.LENGTH_SHORT).show();
                                  mSoundFX.playSound(1, 0.8f, 0);
                                  move.turnFlamo(arrayList, position, columnNum, Math.round(view.getX()), leftBloxX, rightBloxX, adapter, parent);
                              } else if(arrayList.get(position).getBloxName().equals("deadBlox")){
                                  mSoundFX.playSound(5, 0.8f, 0);
                              }

                              //get location bloxman is
                              boolean bloxIsAround = move.isBloxManAround(arrayList, position, columnNum, Math.round(view.getX()), leftBloxX, rightBloxX);
                              //  Toast.makeText(PlayActivity.this, rightBloxX + "", Toast.LENGTH_SHORT).show();
                              //move blox
                              if (MovConstants.GAME_STATE.equals("play") && bloxIsAround && !arrayList.get(position).getBloxName().equals("deadBlox")) {
                                  //hold previous blox man position to detect where bloxman was and set animation
                                  int previousBloxMan = MovConstants.CURRENT_BLOX_MAN_POS;
                                  if(!arrayList.get(position).getBloxName().equals("bloxMan")){
                                      mSoundFX.playSound(3, 0.8f, 0);
                                  }
                                  move.moveBloxPressed(adapter, gridView, view, position, mBloxes, MovConstants.CURRENT_BLOX_MAN_POS, arrayList);
                                 // Toast.makeText(PlayActivity.this, MovConstants.CURRENT_BLOX_MAN_POS + "", Toast.LENGTH_SHORT).show();

                                  boolean hasTouchedBlock = move.hasTouchedBlock(arrayList, previousBloxMan, columnNum, Math.round(parent.getChildAt(previousBloxMan).getX()), adapter, leftBloxX, rightBloxX);

                                  if (hasTouchedBlock) {
                                      //stop timer
                                      timer.cancelTimer();
                                      //remove life
                                      MovConstants.GAME_STATE = "lostLife";
                                      livesLeft = lives.setLivesLeft(livesLeft);
                                      gameOverText = "Oh no! You hit the same color!";

                                      //animate bloxes touching
                                      final ObjectAnimator scaleX = ObjectAnimator.ofFloat(parent.getChildAt(previousBloxMan), "scaleX", 0.3f);
                                      final ObjectAnimator scaleY = ObjectAnimator.ofFloat(parent.getChildAt(previousBloxMan), "scaleY", 0.3f);
                                      ObjectAnimator scaleXNext = ObjectAnimator.ofFloat(parent.getChildAt(MovConstants.TOUCHED_BLOX_POSITION), "scaleX", 0.3f);
                                      ObjectAnimator scaleYNext = ObjectAnimator.ofFloat(parent.getChildAt(MovConstants.TOUCHED_BLOX_POSITION), "scaleY", 0.3f);
                                      scaleX.setRepeatMode(Animation.REVERSE);
                                      scaleX.setRepeatCount(1);

                                      scaleY.setRepeatCount(1);
                                      scaleY.setRepeatMode(Animation.REVERSE);

                                      scaleXNext.setRepeatCount(1);
                                      scaleXNext.setRepeatMode(Animation.REVERSE);

                                      scaleYNext.setRepeatCount(1);
                                      scaleYNext.setRepeatMode(Animation.REVERSE);


                                      final AnimatorSet scaleDown = new AnimatorSet();
                                      scaleDown.setStartDelay(300);
                                      scaleDown.setDuration(1000);
                                      scaleDown.play(scaleX).with(scaleY).with(scaleXNext).with(scaleYNext);

                                      scaleDown.addListener(new Animator.AnimatorListener() {
                                          @Override
                                          public void onAnimationStart(Animator animation) {

                                          }

                                          @Override
                                          public void onAnimationEnd(Animator animation) {
                                              //save state in adapter
                                              adapter.swapBlox(arrayList);
                                             // PopOuts popOut = new PopOuts(PlayActivity.this, PlayActivity.this);
                                                dialog = new DialogEndGame(PlayActivity.this, mSoundFX);
                                              if(livesLeft > 0) {
                                                  //popOut.showPlayAgain(gameOverText, livesLeft);
                                                  dialog.lostLifeDialog(gameOverText, livesLeft);
                                              } else{
                                                  MovConstants.GAME_STATE = "gameOver";
                                                 // popOut.showWait(timer);
                                                  dialog.showWait(timer);
                                              }
                                          }

                                          @Override
                                          public void onAnimationCancel(Animator animation) {

                                          }

                                          @Override
                                          public void onAnimationRepeat(Animator animation) {

                                          }
                                      });
                                      scaleDown.start();
                                  }
                                  //reached the top
                                  else if (MovConstants.CURRENT_BLOX_MAN_POS == 0) {
                                      //stop timer
                                      timer.cancelTimer();
                                      //make goal text invisible
                                     // PopOuts popOut = new PopOuts(PlayActivity.this, PlayActivity.this);
                                      dialog = new DialogEndGame(PlayActivity.this, mSoundFX);
                                      LevelData levelLocked = new LevelData(PlayActivity.this);

                                      //set level unlocked
                                      if (mLevel < 30) {
                                          if (levelLocked.getUnlockedTo() < mLevel + 1) {
                                              levelLocked.setUnlockedTo(mLevel + 1);
                                          }
                                      }
                                      dialog.showWon(Math.round(mInitialTime / 1000), Math.round(MovConstants.TIME_LEFT / 1000), mLevel);
                                      //Toast.makeText(PlayActivity.this, getRating() + "", Toast.LENGTH_SHORT).show();

                                  }
                              }
                          }
                      }
                  });
              }





              public Integer[] getBloxSizeForScreen() {
                  Integer[] bloxSizes;
                  if ((getResources().getConfiguration().screenLayout &
                          Configuration.SCREENLAYOUT_SIZE_MASK) >=
                          Configuration.SCREENLAYOUT_SIZE_LARGE) {
                      // on a large screen device ...
                      bloxSizes = new Integer[]{40, 3};
//                      bloxSizes = new Integer[]{70, 5};
                      return bloxSizes;
                  } else {
                      bloxSizes = new Integer[]{40, 3};
                      return bloxSizes;
                  }
              }


              //change items for colorblind mode
              public void colorBlindChange() {
                  RelativeLayout background = (RelativeLayout) findViewById(R.id.playBackground);
                  boolean isColorBlind = settings.getColorBlind();

                  if (isColorBlind) {
                      background.setBackgroundColor(Color.WHITE);
                  } else {
                      background.setBackgroundColor(Color.parseColor("#333333"));
                  }
              }


    @Override
    protected void onPause() {
        super.onPause();
        GameMenu menu = new GameMenu(PlayActivity.this, mSoundFX);
        mSoundFX.pauseSounds();
        if(MovConstants.GAME_STATE.equals("play")) {
            menu.showMenu(timer, adapter, livesLeft);
            Log.e(TAG, "timer paused");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSoundFX.resumeSounds();
    }

    @Override
    protected void onStop() {
        super.onStop();

        timer.cancelOutOfLivesTimer();
        timer.cancelTimer();
        mSoundFX.stopSounds();
        dialog.stopDialogActivities();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSoundFX = new SoundFX(this);
    }

    @Override
    public void onBackPressed() {
        if(MovConstants.GAME_STATE.equals("play")) {
            GameMenu menu = new GameMenu(PlayActivity.this, mSoundFX);
            menu.showMenu(timer, adapter, livesLeft);
        } else if(MovConstants.GAME_STATE.equals("start")) {
            Intent intent = new Intent(this, SelectLevelActivity.class);
            intent.putExtra("LEVEL", Play.getLevels(this));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
            this.finish();
            overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
        }
    }
}
