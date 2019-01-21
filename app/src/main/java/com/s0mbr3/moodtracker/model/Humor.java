package com.s0mbr3.moodtracker.model;

import android.widget.ImageView;

import com.s0mbr3.moodtracker.R;


/**
 * Created by Oxhart on 21/01/2019.
 */
public class Humor {
    private ImageView mSmiley;

    public Humor(ImageView smiley){
        this.mSmiley = smiley;

    }

    public ImageView getSadSmiley() {
        this.mSmiley.setImageResource(R.drawable.smiley_sad);
        return mSmiley;
    }

    public ImageView getDisappointedSmiley(){
        this.mSmiley.setImageResource(R.drawable.smiley_disappointed);
        return mSmiley;
    }

    public ImageView getNormalSmiley(){
       this.mSmiley.setImageResource(R.drawable.smiley_normal);
        return mSmiley;
    }

    public ImageView getHappySmiley(){
       this.mSmiley.setImageResource(R.drawable.smiley_happy);
        return mSmiley;
    }

    public ImageView getSuperHappySmiley(){
       this.mSmiley.setImageResource(R.drawable.smiley_super_happy);
        return mSmiley;
    }

}
