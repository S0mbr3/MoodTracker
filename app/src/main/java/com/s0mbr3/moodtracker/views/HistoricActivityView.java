package com.s0mbr3.moodtracker.views;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.models.AppStartDriver;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * HistoricActivityView draw dynamically the history using reflection to detect which line to draw
 * depending of the history data contained in the SharedPreferences
 *
 *
 * Created by Oxhart on 21/01/2019.
 */
public class HistoricActivityView {
    private LinearLayout mHistoricLayout;
    private int mHeight;
    private int mWidth;
    private TextView mHistoricLine;
    private ConstraintLayout mConstraintLayout;
    private static final float scale = Resources.getSystem().getDisplayMetrics().density;

    public HistoricActivityView(TextView historicLine, LinearLayout layout,
								ConstraintLayout constraintLayout, int height, int width){
        this.mHistoricLayout = layout;
        this.mHeight = height;
        this.mWidth = width;
        this.mHistoricLine = historicLine;
        this.mConstraintLayout = constraintLayout;
    }

    public void createHistoricLine(int index){
        createHistoricTextView(index);
    }
    public void createHistoricLine(int index, Button commentButton, float buttonSize){
        createHistoricTextView(index);
        createHistoricCommentButton(commentButton, buttonSize);

    }
    public void createHistoricTextView(int index){
        int width = 1;
        Class<?> reflectClass;
        try {
            reflectClass = Class.forName(HistoricActivityView.class.getName());
            Method method = reflectClass.getMethod(
            		AppStartDriver.INSTANCE.getHumor(index), (Class[]) null);
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
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,mHeight/7);
        this.mConstraintLayout.setLayoutParams(lp);
        this.mHistoricLayout.addView(this.mConstraintLayout);
        this.mConstraintLayout.addView(this.mHistoricLine);
        ConstraintSet set = new ConstraintSet();
        set.clone(this.mConstraintLayout);
        set.constrainHeight(this.mHistoricLine.getId(),ConstraintSet.WRAP_CONTENT);
        set.constrainWidth(this.mHistoricLine.getId(),ConstraintSet.WRAP_CONTENT);
        set.applyTo(this.mConstraintLayout);
    }


    private void createHistoricCommentButton(Button commentButton, float size) {
        commentButton.setId(View.generateViewId());
        commentButton.setBackgroundResource(R.drawable.ic_comment_black_48px);
        this.mConstraintLayout.addView(commentButton);
        ConstraintSet set = new ConstraintSet();
        set.clone(this.mConstraintLayout);
        set.connect(this.mHistoricLine.getId(),
				ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        set.connect(commentButton.getId(),
				ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, dpToPx(5));
        set.centerVertically(commentButton.getId(), ConstraintSet.PARENT_ID);
		set.constrainHeight(commentButton.getId(), (int) size);
		set.constrainWidth(commentButton.getId(), (int) size);
		set.applyTo(this.mConstraintLayout);
		Configuration configuration = Resources.getSystem().getConfiguration();
		DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
		float screenWidthdp = displayMetrics.widthPixels / displayMetrics.density;
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

    private int dpToPx(int dp)
    {
        return (int) (dp * this.scale);
    }

}
