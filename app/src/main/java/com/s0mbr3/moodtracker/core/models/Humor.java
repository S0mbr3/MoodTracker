package com.s0mbr3.moodtracker.core.models;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
public enum Humor {
    INSTANCE;
    private Context mContext;
    private LinearLayout mHistoricLayout;
    private int mHeight;
    private int mWidth;
    private TextView mHistoricLine;
    private List<String> mHumorList = new ArrayList<>();

    Humor(){
        this.mHumorList.add("setSadSmiley");
        this.mHumorList.add("setDisappointedSmiley");
        this.mHumorList.add("setNormalSmiley");
        this.mHumorList.add("setHappySmiley");
        this.mHumorList.add("setSuperHappySmiley");
    }

    public void setHistoricLayout(TextView historicLine, LinearLayout layout, Context context, int height, int width){
        this.mHistoricLayout = layout;
        this.mHeight = height;
        this.mWidth = width;
        this.mHistoricLine = historicLine;
        this.mContext = context;
    }

    public void createHistoricLine(int index){
        int width = 1;
        Class reflectClass;
        try {
            reflectClass = Class.forName(Humor.class.getName());
            Method method = reflectClass.getMethod(this.mHumorList.get(index), (Class[]) null);
            width = (int) method.invoke(this, (Object[]) null);
            Log.d("mich", String.valueOf(width));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        RelativeLayout relativeLayout = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(width, mHeight/7);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, mHeight/7);
        this.mHistoricLayout.addView(relativeLayout);
        this.mHistoricLine.setLayoutParams(lp);
        Log.d("mich", String.valueOf(width));
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

}
