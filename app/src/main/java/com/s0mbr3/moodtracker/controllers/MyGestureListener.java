package com.s0mbr3.moodtracker.controllers;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.s0mbr3.moodtracker.views.MainActivityView;


/**
 * MyGestureListener class is used to get gestures from screen
 *
 * Created by Oxhart on 21/01/2019.
 */
public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
    private MainActivityView mMainActivityView;
    private boolean mChanged;
    private IndexGetter mIndexListener;
    private int mIndex;

    public MyGestureListener(MainActivityView mainActivityView, int index){
        this.mMainActivityView = mainActivityView;
        this.mChanged = false;
        this.mIndex= index;

        this.mIndexListener = null;
    }


    /**
     * IndexGetter Interface is a custom Listener that will be triggered to send the mIndex
     * of the humor used to get the appropriate method by reflection to show on screen the
     * correspondig smiley to the MainActivity
     *
     */
    public interface IndexGetter{
        void getIndex(int mIndex);
    }

    public void setIndexListener(IndexGetter mIndexListener) {
        this.mIndexListener = mIndexListener;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        this.mChanged = false;
        return true;
    }

    /**
	 * Simple catch of screen swipes and adjusting the mIndex to get the wished humor by reflection
     * @see MainActivityView
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    	boolean isScrolling = false;
        if (distanceY < 0 && !this.mChanged &&  mIndex > 0){
            this.mChanged = true;
            isScrolling = true;
            --mIndex;
            mMainActivityView.getMethodName(mIndex);
        } else if (distanceY > 0 && !this.mChanged && mIndex < 4){
           ++mIndex;
            mMainActivityView.getMethodName(mIndex);
            this.mChanged = true;
            isScrolling = true;
        }
        if(mIndexListener != null && isScrolling) mIndexListener.getIndex(mIndex);
        return true;
    }

}
