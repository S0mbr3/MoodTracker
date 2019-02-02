package com.s0mbr3.moodtracker.controllers;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.s0mbr3.moodtracker.views.MainActivityView;
import com.s0mbr3.moodtracker.models.AppStartDriver;


/**
 * MyGestureListener class is used to get gestures from screen
 *
 * Created by Oxhart on 21/01/2019.
 */
public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String DEBUG_TAG = "GESTURES";
    private MainActivityView mMainActivityView;
    private boolean mChanged;
    private IndexGetter mIndexListener;

    public MyGestureListener(MainActivityView mainActivityView){
        this.mMainActivityView = mainActivityView;
        this.mChanged = false;
        this.mIndexListener = null;
    }


    /**
     * IndexGetter Interface is a custom Listener that will be triggered to send the index
     * of the humor used to get the appropriate method by reflection to show on screen the
     * correspondig smiley to the MainActivity
     *
     * @see IndexGetter
     */
    public interface IndexGetter{
        void getIndex(int index);
    }

    public void setIndexListener(IndexGetter indexListener) {
        this.mIndexListener = indexListener;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(DEBUG_TAG, "onDown");
        return true;
    }

    /**
     * onScroll method catch an up/down swipe on the screen, incrementing/decrementing the
     * humorsList index to show on screen appropriate smiley by choosing the according method
     * by reflection, usage of the boolean mChanged to stop traveling in the array at the first
     * incrementation/decrementation, indexListener is triggered to send the index to the
     * MainActivity
     *
     * @see MainActivityView
     * @param e1
     * @param e2
     * @param distanceX
     * @param distanceY
     * @return
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        int index = AppStartDriver.INSTANCE.getIndex();
        if (distanceY < 0 && !this.mChanged &&  index >= 0){
            this.mChanged = true;
            if (index != 0) index--;
            mMainActivityView.getMethodName(index);
        } else if (distanceY > 0 && !this.mChanged && index <= 4){
            if (index != 4) index++;
            mMainActivityView.getMethodName(index);
            this.mChanged = true;
        }
        if(mIndexListener != null) mIndexListener.getIndex(index);
        Log.d(DEBUG_TAG, "onScroll " + distanceX + " " + distanceY + " " + index);
        AppStartDriver.INSTANCE.setIndex(index);
        return true;
    }

    /**
     * onFling method is used to re arm the boolean and allow changing again the index inside
     * humorList
     * @param e1
     * @param e2
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        this.mChanged = false;
        Log.d(DEBUG_TAG, "onFling");

        return true;
    }
}
