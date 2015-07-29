package com.haphazrd.movblox.Blox;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.haphazrd.movblox.Adapter.BloxAdapter;
import com.haphazrd.movblox.UI.PlayActivity;
import com.haphazrd.movblox.Utils.MovConstants;

import java.util.List;

/**
 * Created by brittanystubbs on 6/1/15.
 */

public class Move {

    protected View colorBlox;
    protected View bloxMan;
    protected int originalXMan;
    protected int originalYMan;
    protected int originalXColor;
    protected int originalYColor;

    private boolean moved = false;
    private static final String TAG = PlayActivity.class.getSimpleName();
    //get bloxman position relative to clicked position
    //position + 1 = directly to right + != 0 --have to account for row they are on
    public boolean isBloxManAround(List<Blox> list, int position, int columns, int x, int left, int right) {
        int bloxLength = list.size();
        //is directly below
        if (position + columns + 1 <= bloxLength && list.get(position + columns).getBloxName().equals("bloxMan")) {
            return true;
            //is directly above
        }else if(position - columns > -1 && list.get(position - columns).getBloxName().equals("bloxMan")) {
            return true;
            //is to the left and on same row
        } else if(position - 1 > -1 && list.get(position - 1).getBloxName().equals("bloxMan") && x != left){
            return true;
            //is to the right and on same row
        } else if(position + 2 <= bloxLength && list.get(position + 1).getBloxName().equals("bloxMan") && x != right){
            return true;
        }
        return false;

    }

//move block and change position in array
    public void moveBloxPressed(final BloxAdapter adapter, GridView gridView, View v, final int arrayPos, Blox[] bloxes, int bloxManPos, final List<Blox> list){
        colorBlox = v;
        bloxMan = gridView.getChildAt(bloxManPos);
       // Log.e(TAG, PlayActivity.CURRENT_BLOX_MAN_POS + "");

        int gridLoc[] = new int[2];
        gridView.getLocationOnScreen(gridLoc);
        int originalPosColor[] = new int[2];
        colorBlox.getLocationOnScreen(originalPosColor);
        int originalPosMan[] = new int[2];
        bloxMan.getLocationOnScreen(originalPosMan);

        originalXMan = Math.round(bloxMan.getX());
        originalYMan = Math.round(bloxMan.getY());
        originalXColor = Math.round(colorBlox.getX());
        originalYColor = Math.round(colorBlox.getY());

        //Log.e(TAG, bloxManPos + "");

            Blox tempBloxMan = list.get(bloxManPos);
            Blox tempColor = list.get(arrayPos);

            list.set(arrayPos, tempBloxMan);
            list.set(bloxManPos, tempColor);

        ObjectAnimator animateManX = ObjectAnimator.ofFloat(bloxMan, "x", originalPosMan[0] - gridLoc[0], originalPosColor[0] - gridLoc[0]);
        ObjectAnimator animateManY = ObjectAnimator.ofFloat(bloxMan, "y", originalPosMan[1] - gridLoc[1], originalPosColor[1] - gridLoc[1]);
        ObjectAnimator animateColorX = ObjectAnimator.ofFloat(colorBlox, "x", originalPosColor[0] - gridLoc[0] ,originalPosMan[0] - gridLoc[0]);
        ObjectAnimator animateColorY = ObjectAnimator.ofFloat(colorBlox, "y", originalPosColor[1] - gridLoc[1], originalPosMan[1] - gridLoc[1]);

        AnimatorSet setAnimate = new AnimatorSet();
        setAnimate.playTogether(animateManX, animateManY, animateColorX, animateColorY);
        setAnimate.setDuration(300);


        setAnimate.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                MovConstants.IS_MOVING = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                bloxMan.setX(originalXMan);
                bloxMan.setY(originalYMan);
                colorBlox.setX(originalXColor);
                colorBlox.setY(originalYColor);
                adapter.swapBlox(list);
                MovConstants.IS_MOVING = false;

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        setAnimate.start();

        MovConstants.CURRENT_BLOX_MAN_POS = arrayPos;
    }
//if two of the same colored blox are touching
    public boolean hasTouchedBlock(List<Blox> list, int position, int columns, int x, final BloxAdapter adapter, int left, int right){
        int bloxLength = list.size();
        //is directly below
        if (position + columns + 1 <= bloxLength && list.get(position + columns).getBloxName().equals(list.get(position).getBloxName()) && !list.get(position + columns).getBloxName().equals("flamoMinion")) {
            list.get(position + columns).setMatchedBloxColor();
            list.get(position).setMatchedBloxColor();
            MovConstants.TOUCHED_BLOX_POSITION = position + columns;

            return true;
            //is directly above
        }else if(position - columns > -1 && list.get(position - columns).getBloxName().equals(list.get(position).getBloxName()) && !list.get(position - columns).getBloxName().equals("flamoMinion")) {
            list.get(position - columns).setMatchedBloxColor();
            list.get(position).setMatchedBloxColor();
            MovConstants.TOUCHED_BLOX_POSITION = position - columns;

            return true;
            //is to the left and on same row
        } else if(position - 1 > -1 && list.get(position - 1).getBloxName().equals(list.get(position).getBloxName()) && x != left && !list.get(position - 1).getBloxName().equals("flamoMinion")){
            list.get(position - 1).setMatchedBloxColor();
            list.get(position).setMatchedBloxColor();
            MovConstants.TOUCHED_BLOX_POSITION = position - 1;

            return true;
            //is to the right and on same row
        } else if(position + 2 <= bloxLength && list.get(position + 1).getBloxName().equals(list.get(position).getBloxName()) && x != right && !list.get(position + 1).getBloxName().equals("flamoMinion")){
            list.get(position + 1).setMatchedBloxColor();
            list.get(position).setMatchedBloxColor();
            MovConstants.TOUCHED_BLOX_POSITION = position + 1;

            return true;
        }
        return false;
    }

//turn into flamo blox
    public void turnFlamo(List<Blox> list, final int position, final int columns, int x, int left, int right, final BloxAdapter adapter, final AdapterView<?> view){
        int bloxLength = list.size();

        if (position + columns + 1 <= bloxLength && !list.get(position + columns).getBloxName().equals("bloxMan")) {
            list.get(position + columns).setFlamoColor();
            list.get(position + columns).setFlamoMinionName();
        }
        //is directly above
        if(position - columns > -1 && !list.get(position - columns).getBloxName().equals("bloxMan")) {
            list.get(position - columns).setFlamoColor();
            list.get(position - columns).setFlamoMinionName();
            //is to the left and on same row
        }
        if(position - 1 > -1 && !list.get(position - 1).getBloxName().equals("bloxMan") && x != left){
            list.get(position - 1).setFlamoColor();
            list.get(position - 1).setFlamoMinionName();
            //is to the right and on same row
        }
        if(position + 2 <= bloxLength && !list.get(position + 1).getBloxName().equals("bloxMan") && x != right){
            list.get(position + 1).setFlamoColor();
            list.get(position + 1).setFlamoMinionName();
        }
        list.get(position).setFlamoColor();
        list.get(position).setFlamoMinionName();
    }

}
