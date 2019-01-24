package com.s0mbr3.moodtracker.model;

import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;


import com.s0mbr3.moodtracker.R;


/**
 * Created by Oxhart on 21/01/2019.
 */
public class Humor {
    private ImageView mSmiley;
    private ConstraintLayout mLayout;

    public Humor(ConstraintLayout layout, ImageView smiley){
        this.mSmiley = smiley;
        this.mLayout = layout;

    }

    public ImageView getSadSmiley() {
        this.mSmiley.setImageResource(R.drawable.smiley_sad);
        this.mLayout.setBackgroundColor(0xffde3c50);
        return mSmiley;
    }

    public ImageView getDisappointedSmiley(){
        this.mSmiley.setImageResource(R.drawable.smiley_disappointed);
        this.mLayout.setBackgroundColor(0xff9b9b9b);
        return mSmiley;
    }

    public ImageView getNormalSmiley(){
       this.mSmiley.setImageResource(R.drawable.smiley_normal);
        this.mLayout.setBackgroundColor(0xa5468ad9);
        return mSmiley;
    }

    public ImageView getHappySmiley(){
       this.mSmiley.setImageResource(R.drawable.smiley_happy);
        this.mLayout.setBackgroundColor(0xffb8e986);
        return mSmiley;
    }

    public ImageView getSuperHappySmiley(){
       this.mSmiley.setImageResource(R.drawable.smiley_super_happy);
        this.mLayout.setBackgroundColor(0xfff9ec4f);
        return mSmiley;
    }

}
