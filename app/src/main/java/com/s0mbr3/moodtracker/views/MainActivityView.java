package com.s0mbr3.moodtracker.views;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.widget.ImageView;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.models.AppStartDriver;
import com.s0mbr3.moodtracker.controllers.MyGestureListener;
import com.s0mbr3.moodtracker.models.SizeManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * MainActivityView class control what humor to choose depending of an index to a built string list
 * containing names of methods to use
 *
 * Created by Oxhart on 21/01/2019.
 */
public class MainActivityView {
    private Method mMethod;
    private ImageView mSmiley;
    private ConstraintLayout mLayout;
    private static final float scale = Resources.getSystem().getDisplayMetrics().density;

    public MainActivityView(ConstraintLayout layout, ImageView smiley) {
        this.mSmiley = smiley;
        this.mLayout = layout;
    }


    public void getMethodName(int index) {
        Class<?> c1;
        try {
            c1 = Class.forName(MainActivityView.class.getName());
                this.mMethod = c1.getMethod(AppStartDriver.INSTANCE.getHumor(index), (Class[]) null);
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

    public void constrainSet(){
        SizeManager getSize = new SizeManager();
        Object[] sizes = getSize.sizeManager();
        int height = (int) sizes[0];
        int width = (int) sizes[1];
        double ratio = 1.6 + (double) sizes[2];
        ConstraintSet set = new ConstraintSet();
        set.clone(mLayout);
        set.constrainWidth(this.mSmiley.getId(), (int) (width/ratio));
        set.constrainHeight(this.mSmiley.getId(), (int) (height/ratio));
        set.centerVertically(this.mSmiley.getId(), ConstraintSet.PARENT_ID);
        set.centerHorizontally(this.mSmiley.getId(), ConstraintSet.PARENT_ID);
        set.applyTo(mLayout);
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
    private int dpToPx(int dp)
    {
        return (int) (dp * this.scale);
    }
}
