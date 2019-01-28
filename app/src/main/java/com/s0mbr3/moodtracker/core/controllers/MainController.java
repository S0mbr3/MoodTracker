package com.s0mbr3.moodtracker.core.controllers;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.core.models.Humor;

import org.w3c.dom.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * MainController class control what humor to choose depending of an index to a built string list
 * containing names of methods to use
 *
 * Created by Oxhart on 21/01/2019.
 */
public enum MainController {
    INSTANCE;
    private List<String> mHumorsList = new ArrayList<>();
    private Method mMethod;
    private Humor mHumor;
    private int mIndex;
    private ImageView mSmiley;
    private TextView mHistoricLine;
    private int mHeight;
    private int mWidth;
    private Context mContext;
    private ConstraintLayout mLayout;

    MainController() {
    }


    /**
     * getMethodName method invoke by reflection the according method stored in the humorsList String
     * List depending of the index determined in the MyGestureListener class
     *
     * @see MyGestureListener
     * @param index
     */
    public void getMethodName(int index) {
        Class c1;
        try {
            c1 = Class.forName(MainController.class.getName());
                this.mMethod = c1.getMethod(this.mHumorsList.get(index), (Class[]) null);
                this.mMethod.invoke(this, (Object[]) null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public void initMainController(ConstraintLayout layout, ImageView smiley) {
        this.mSmiley = smiley;
        this.mLayout = layout;

        this.mHumorsList.add("setSadSmiley");
        this.mHumorsList.add("setDisappointedSmiley");
        this.mHumorsList.add("setNormalSmiley");
        this.mHumorsList.add("setHappySmiley");
        this.mHumorsList.add("setSuperHappySmiley");
    }

    public void setSadSmiley() {
        this.mSmiley.setImageResource(R.drawable.smiley_sad);
        this.mLayout.setBackgroundResource(R.color.faded_red);
    }

    public void setDisappointedSmiley(){
        this.mSmiley.setImageResource(R.drawable.smiley_disappointed);
        this.mLayout.setBackgroundResource(R.color.warm_grey);
    }

    public void setNormalSmiley(){
        this.mSmiley.setImageResource(R.drawable.smiley_normal);
        this.mLayout.setBackgroundResource(R.color.cornflower_blue_65);
    }

    public void setHappySmiley(){
        this.mSmiley.setImageResource(R.drawable.smiley_happy);
        this.mLayout.setBackgroundResource(R.color.light_sage);
    }

    public void setSuperHappySmiley(){
        this.mSmiley.setImageResource(R.drawable.smiley_super_happy);
        this.mLayout.setBackgroundResource(R.color.banana_yellow);
    }
}
