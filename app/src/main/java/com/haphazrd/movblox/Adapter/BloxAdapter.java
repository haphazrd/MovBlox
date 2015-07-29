package com.haphazrd.movblox.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haphazrd.movblox.Blox.Blox;
import com.haphazrd.movblox.R;
import com.haphazrd.movblox.DataStorage.SettingsData;

import java.util.List;

/**
 * Created by brittanystubbs on 5/26/15.
 */
public class BloxAdapter extends BaseAdapter {

    protected Context mContext;
    private List<Blox> mBloxes;
    protected int mLevel = 1;
    protected SettingsData mSettingsData;

    public BloxAdapter(Context context, List<Blox> bloxes) {
        mContext = context;
        mBloxes = bloxes;
    }

    @Override
    public int getCount() {
        return mBloxes.size();
    }

    @Override
    public Object getItem(int position) {
        return mBloxes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void swapBlox(List<Blox> bloxes) {
        mBloxes = bloxes;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        mSettingsData = new SettingsData(mContext);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.blox_item, null);
            holder = new ViewHolder();

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Blox currentBlox = mBloxes.get(position);
        holder.bloxImages = (ImageView) convertView.findViewById(R.id.largeBlox);
        holder.goalText = (TextView) convertView.findViewById(R.id.goalText);

        holder.symbol = (ImageView) convertView.findViewById(R.id.flamoSymbol);
        convertView.setTag(holder);

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "squares_bold_free-webfont.ttf");
        holder.goalText.setTypeface(font);

        if(position == 0 && !currentBlox.getBloxColor().equals("#222222")) {
            holder.goalText.setVisibility(View.VISIBLE);
        } else {
            holder.goalText.setVisibility(View.INVISIBLE);
        }

//        //set to bloxImages man
//        if (currentBlox.getBloxColor().equals("#222222")) {
//            holder.bloxImages.setImageResource(R.drawable.blox_man_cover);
//        } else {
//            holder.bloxImages.setImageDrawable(null);
//        }


        //flame blox
        if (currentBlox.getBloxColor().equals("#FFCC66")) {
            holder.symbol.setImageResource(R.drawable.flamosymbol);
            holder.symbol.setVisibility(View.VISIBLE);
        }else if(currentBlox.getBloxColor().equals("#666666")) {
            //dead blox
            holder.symbol.setImageResource(R.drawable.deadsymbol);
            holder.symbol.setVisibility(View.VISIBLE);
        }else if(currentBlox.getBloxColor().equals("#FF6666")  && position != 0 && mSettingsData.getColorBlind()) {
            //red
            holder.symbol.setImageResource(R.drawable.redsymbol);
            holder.symbol.setVisibility(View.VISIBLE);
        }else if(currentBlox.getBloxColor().equals("#0099FF") && position != 0 && mSettingsData.getColorBlind()) {
            //blue
            holder.symbol.setImageResource(R.drawable.bluesymbol);
            holder.symbol.setVisibility(View.VISIBLE);
        }else if(currentBlox.getBloxColor().equals("#66CC66")  && position != 0 && mSettingsData.getColorBlind()) {
            //green
            holder.symbol.setImageResource(R.drawable.greensymbol);
            holder.symbol.setVisibility(View.VISIBLE);
        }else if(currentBlox.getBloxColor().equals("#9966FF")  && position != 0 && mSettingsData.getColorBlind()) {
            //purple
            holder.symbol.setImageResource(R.drawable.purplesymbol);
            holder.symbol.setVisibility(View.VISIBLE);
        }else if(currentBlox.getBloxColor().equals("#222222") && mSettingsData.getColorBlind()) {
            holder.symbol.setImageResource(R.drawable.bloxmansymbol_colorblind);
            holder.symbol.setVisibility(View.VISIBLE);
        }else if(currentBlox.getBloxColor().equals("#222222") && !mSettingsData.getColorBlind()){
            holder.symbol.setImageResource(R.drawable.bloxmansymbol);
            holder.symbol.setVisibility(View.VISIBLE);
        } else {
            holder.symbol.setVisibility(View.INVISIBLE);
        }


            //set color
            GradientDrawable shapeDrawable = (GradientDrawable) holder.bloxImages.getBackground();

        if(currentBlox.getBloxColor().equals("#222222") && !mSettingsData.getColorBlind()) {
            shapeDrawable.setColor(Color.parseColor("#FFFFCC"));
        } else {
            shapeDrawable.setColor(Color.parseColor(currentBlox.getBloxColor()));
        }


       return convertView;
    }

    private static class ViewHolder {
        ImageView bloxImages;
        ImageView symbol;
        TextView goalText;
    }

}
