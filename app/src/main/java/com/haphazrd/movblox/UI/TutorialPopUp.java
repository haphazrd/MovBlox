package com.haphazrd.movblox.UI;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.haphazrd.movblox.DataStorage.SettingsData;
import com.haphazrd.movblox.R;
import com.haphazrd.movblox.Utils.CountdownTimer;
import com.haphazrd.movblox.Utils.MovConstants;


/**
 * Created by brittanystubbs on 7/1/15.
 */
public class TutorialPopUp extends Activity{
    public Context mContext;
    protected Button mCloseButton;
    protected TextView mDescText;
    protected ImageView mBlox;
    protected ImageView mSymbol;
    protected Activity mActivity;
    protected TextView mHowToText;

    public TutorialPopUp(Context context){
        mContext = context;
        mActivity = (Activity) context;
    }

    public void showPopUp(final CountdownTimer timer, final int lives, final String type) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popUpView = inflater.inflate(R.layout.popup_layout, null, false);
        final PopupWindow popup = new PopupWindow(popUpView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popup.setContentView(popUpView);

        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setAnimationStyle(R.style.Animation);
        popup.setFocusable(true);
        popup.setOutsideTouchable(false);

        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                timer.startTimer(lives);
                MovConstants.GAME_STATE = "play";
            }
        });

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "squares_bold_free-webfont.ttf");

        mCloseButton = (Button) popUpView.findViewById(R.id.closeButton);
        mDescText = (TextView) popUpView.findViewById(R.id.tutorialDesc);
        mHowToText = (TextView) popUpView.findViewById(R.id.howToText);
        mBlox = (ImageView) popUpView.findViewById(R.id.blox_view);
        mSymbol = (ImageView) popUpView.findViewById(R.id.blox_symbol);
        mCloseButton.setTypeface(font);
        mDescText.setTypeface(font);
        mHowToText.setTypeface(font);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GradientDrawable shapeDrawable = (GradientDrawable) mBlox.getBackground();
                if (type.equals("deadblox")) {
                    mDescText.setText("Be careful of deadblox, they will not budge one bit!");
                    mSymbol.setImageResource(R.drawable.deadsymbol);
                    shapeDrawable.setColor(Color.parseColor("#666666"));
                } else if (type.equals("flamoblox")) {
                    mDescText.setText("Flamoblox will ignite blox around them. These are wildcard blox that can be used anywhere.");
                    mSymbol.setImageResource(R.drawable.flamosymbol);
                    shapeDrawable.setColor(Color.parseColor("#FFCC66"));
                } else {
                    SettingsData settings = new SettingsData(mContext);
                    if (settings.getColorBlind()) {
                        mSymbol.setImageResource(R.drawable.bloxmansymbol_colorblind);
                        shapeDrawable.setColor(Color.parseColor("#222222"));
                    } else {
                        mSymbol.setImageResource(R.drawable.bloxmansymbol);
                        shapeDrawable.setColor(Color.parseColor("#FFFFCC"));
            }
                }
                popup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);


                mCloseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                });
            }
        }, 100L);

    }
}
