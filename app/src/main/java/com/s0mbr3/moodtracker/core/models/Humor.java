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
    private ConstraintLayout mConstraintLayout;

    public Humor(TextView historicLine, LinearLayout layout, ConstraintLayout constraintLayout, int height, int width){
        this.mHistoricLayout = layout;
        this.mHeight = height;
        this.mWidth = width;
        this.mHistoricLine = historicLine;
        this.mConstraintLayout = constraintLayout;
        this.mHumorList.add("setSadSmiley");
        this.mHumorList.add("setDisappointedSmiley");
        this.mHumorList.add("setNormalSmiley");
        this.mHumorList.add("setHappySmiley");
        this.mHumorList.add("setSuperHappySmiley");
    }


    public void createHistoricLine(int index){
        createHistoricTextView(index);
    }
    public void createHistoricLine(int index, Button commentButton){
       createHistoricTextView(index);
       createHistoricCommentButton(commentButton);

    }
    public void createHistoricTextView(int index){
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

        this.mConstraintLayout.setId(View.generateViewId());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, mHeight/7);
        this.mConstraintLayout.setLayoutParams(lp);
        this.mHistoricLayout.addView(this.mConstraintLayout);
        this.mConstraintLayout.addView(this.mHistoricLine);
        ConstraintSet set = new ConstraintSet();
        set.clone(this.mConstraintLayout);
        set.constrainHeight(this.mHistoricLine.getId(),ConstraintSet.WRAP_CONTENT);
        set.constrainWidth(this.mHistoricLine.getId(),ConstraintSet.WRAP_CONTENT);
        set.applyTo(this.mConstraintLayout);
    }


    private void createHistoricCommentButton(Button commentButton){
        commentButton.setId(View.generateViewId());
        commentButton.setBackgroundResource(R.drawable.ic_comment_black_48px);
        this.mConstraintLayout.addView(commentButton);
        ConstraintSet set = new ConstraintSet();
        set.clone(this.mConstraintLayout);
        set.connect(commentButton.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,dpToPx(12));
        set.constrainHeight(commentButton.getId(),dpToPx(20));
        set.constrainWidth(commentButton.getId(), dpToPx(20));
        set.centerVertically(commentButton.getId(), ConstraintSet.PARENT_ID);
        set.applyTo(this.mConstraintLayout);
    }
    public int setSadSmiley() {
        this.mConstraintLayout.setBackgroundResource(R.color.faded_red);
        int width=  this.mWidth * 20/100;
        return width;
    }

    public int setDisappointedSmiley(){
        this.mConstraintLayout.setBackgroundResource(R.color.warm_grey);
        int width = this.mWidth * 40/100;
        return width;
    }

    public int setNormalSmiley(){
        this.mConstraintLayout.setBackgroundResource(R.color.cornflower_blue_65);
        int width = this.mWidth * 60/100;
        return width;
    }

    public int setHappySmiley(){
        this.mConstraintLayout.setBackgroundResource(R.color.light_sage);
        int width = this.mWidth*80/100;
        return width;
    }

    public int setSuperHappySmiley(){
        this.mConstraintLayout.setBackgroundResource(R.color.banana_yellow);
        int width = mWidth;
        return width;
    }

    private static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}
