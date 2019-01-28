package com.s0mbr3.moodtracker.core.models;

import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.activities.HistoricActivity;
import com.s0mbr3.moodtracker.activities.MainActivity;

import org.w3c.dom.Text;

import java.io.LineNumberReader;


/**
 * Humor class is a set of setters/getters method to show on screen appropriate smileys
 * and background colors by reflection
 *
 * Created by Oxhart on 21/01/2019.
 */
public class Humor {
    private ImageView mSmiley;
    private ViewGroup mLayout;
    private LinearLayout mLinearLayout;

    public Humor(ViewGroup layout){
        this.mLayout = layout;

    }

    public void setSadSmiley(ImageView smiley) {
        smiley.setImageResource(R.drawable.smiley_sad);
        this.mLayout.setBackgroundResource(R.color.faded_red);
    }

    public void setDisappointedSmiley(ImageView smiley){
        smiley.setImageResource(R.drawable.smiley_disappointed);
        this.mLayout.setBackgroundResource(R.color.warm_grey);
    }

    public void setNormalSmiley(ImageView smiley){
        smiley.setImageResource(R.drawable.smiley_normal);
        this.mLayout.setBackgroundResource(R.color.cornflower_blue_65);
    }

    public void setHappySmiley(ImageView smiley){
        smiley.setImageResource(R.drawable.smiley_happy);
        this.mLayout.setBackgroundResource(R.color.light_sage);
    }

    public void setSuperHappySmiley(ImageView smiley){
        smiley.setImageResource(R.drawable.smiley_super_happy);
        this.mLayout.setBackgroundResource(R.color.banana_yellow);
    }

    public void setSadSmiley(TextView historicLine, int height, int width) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width*20/100, height/7);
        historicLine.setLayoutParams(lp);
        historicLine.setBackgroundResource(R.color.faded_red);
    }

    public void setDisappointedSmiley(TextView historicLine, int height, int width){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width*40/100, height/7);
        historicLine.setLayoutParams(lp);
        historicLine.setBackgroundResource(R.color.warm_grey);
    }

    public void setNormalSmiley(TextView historicLine, int height, int width){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width*60/100, height/7);
        historicLine.setLayoutParams(lp);
        historicLine.setBackgroundResource(R.color.cornflower_blue_65);
    }

    public void setHappySmiley(TextView historicLine, int height, int width){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width*80/100, height/7);
        historicLine.setLayoutParams(lp);
        historicLine.setBackgroundResource(R.color.light_sage);
    }

    public void setSuperHappySmiley(TextView historicLine, int height, int width){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height/7);
        historicLine.setLayoutParams(lp);
        historicLine.setBackgroundResource(R.color.banana_yellow);
    }

}
