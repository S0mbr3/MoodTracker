package com.s0mbr3.moodtracker.core.models;

import android.content.Context;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.s0mbr3.moodtracker.R;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Humor class is a set of setters/getters method to show on screen appropriate smileys
 * and background colors by reflection
 *
 * Created by Oxhart on 21/01/2019.
 */
public class Humor {
    private Context mContext;
    private LinearLayout mHistoricLayout;
    private int mHeight;
    private int mWidth;
    private TextView mHistoricLine;
    private List<String> mHumorList = new ArrayList<>();

    public Humor(TextView historicLine, LinearLayout layout, Context context, int height, int width){
        this.mHistoricLayout = layout;
        this.mHeight = height;
        this.mWidth = width;
        this.mHistoricLine = historicLine;
        this.mContext = context;

        this.mHumorList.add("setSadSmiley");
        this.mHumorList.add("setDisappointedSmiley");
        this.mHumorList.add("setNormalSmiley");
        this.mHumorList.add("setHappySmiley");
        this.mHumorList.add("setSuperHappySmiley");
    }


    public void createHistoricLine(int index, String commentTxt){
        int width = 1;
        Class reflectClass;
        try {
            reflectClass = Class.forName(Humor.class.getName());
            Method method = reflectClass.getMethod(this.mHumorList.get(index), (Class[]) null);
            width = (int) method.invoke(this, (Object[]) null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        ConstraintLayout constraintLayout = new ConstraintLayout(mContext);
        constraintLayout.setId(View.generateViewId());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, mHeight/7);
        constraintLayout.setLayoutParams(lp);
        this.mHistoricLayout.addView(constraintLayout);
        constraintLayout.addView(this.mHistoricLine);
        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);
        set.constrainHeight(this.mHistoricLine.getId(), 0);
        set.constrainWidth(this.mHistoricLine.getId(), 0);
        set.connect(this.mHistoricLine.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,0);
        set.connect(this.mHistoricLine.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
        set.connect(this.mHistoricLine.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,0);
        set.connect(this.mHistoricLine.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        set.applyTo(constraintLayout);
        if(commentTxt != null) createHistoricCommentButton(constraintLayout, width);
    }


    private void createHistoricCommentButton(ConstraintLayout constraintLayout, int width){
        final Button commentButton = new Button(mContext);
        commentButton.setId(View.generateViewId());
        commentButton.setBackgroundResource(R.drawable.ic_comment_black_48px);
        constraintLayout.addView(commentButton);
        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);
        set.connect(commentButton.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,40);
        set.constrainHeight(commentButton.getId(), 40);
        set.constrainWidth(commentButton.getId(), 40);
        set.centerVertically(commentButton.getId(), ConstraintSet.PARENT_ID);
        set.applyTo(constraintLayout);

    }
    public int setSadSmiley() {
        this.mHistoricLine.setBackgroundResource(R.color.faded_red);
        int width=  this.mWidth * 20/100;
        return width;
    }

    public int setDisappointedSmiley(){
        this.mHistoricLine.setBackgroundResource(R.color.warm_grey);
        int width = this.mWidth * 40/100;
        return width;
    }

    public int setNormalSmiley(){
        this.mHistoricLine.setBackgroundResource(R.color.cornflower_blue_65);
        int width = this.mWidth * 60/100;
        return width;
    }

    public int setHappySmiley(){
        this.mHistoricLine.setBackgroundResource(R.color.light_sage);
        int width = this.mWidth*80/100;
        return width;
    }

    public int setSuperHappySmiley(){
        this.mHistoricLine.setBackgroundResource(R.color.banana_yellow);
        int width = mWidth;
        return width;
    }

    private static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

}
