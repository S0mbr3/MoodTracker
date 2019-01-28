package com.s0mbr3.moodtracker.core.controllers;

import android.support.constraint.ConstraintLayout;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


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

    MainController() {
    }


    /**
     * getMethodName method invoke by reflection the according method stored in the humorsList String
     * List depending of the index determined in the MyGestureListener class
     *
     * @see MyGestureListener
     * @param index
     */
    public void getMethodName(int index, boolean isHistoric) {
        Class c1;
        try {
            c1 = Class.forName(Humor.class.getName());
            if(isHistoric){
                this.mMethod = c1.getMethod(this.mHumorsList.get(index), TextView.class, int.class, int.class);
                this.mMethod.invoke(this.mHumor, mHistoricLine, mHeight, mWidth);
            }
            else{
                this.mMethod = c1.getMethod(this.mHumorsList.get(index), ImageView.class);
                this.mMethod.invoke(this.mHumor, mSmiley);
            }
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

    public void setHistoricLayout(TextView historicLine, int height, int width ){
        this.mHistoricLine = historicLine;
        this.mHeight = height;
        this.mWidth = width;
    }

    public void initMainController(ViewGroup layout, ImageView smiley) {
        this.mHumor = new Humor(layout);
        this.mSmiley = smiley;

        this.mHumorsList.add("setSadSmiley");
        this.mHumorsList.add("setDisappointedSmiley");
        this.mHumorsList.add("setNormalSmiley");
        this.mHumorsList.add("setHappySmiley");
        this.mHumorsList.add("setSuperHappySmiley");
    }
}
