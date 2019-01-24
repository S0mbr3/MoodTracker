package com.s0mbr3.moodtracker.controller.MainControllers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.s0mbr3.moodtracker.controller.MainActivity;

/**
 * Created by Oxhart on 21/01/2019.
 */
public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String DEBUG_TAG = "GESTURES";
    private MainController mMainController;
    private int mIndex;
    private boolean mChanged;
    private IndexGetter mIndexListener;

    public MyGestureListener(int index, MainController mainController){
        this.mMainController = mainController;
        this.mIndex = index;
        this.mChanged = false;
        this.mIndexListener = null;
        mMainController.getMethodName(3);
    }


    public interface IndexGetter{
        public void getIndex(int index);
    }

    public void setIndexListener(IndexGetter indexListener) {
        mIndexListener = indexListener;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //return super.onDown(e);
        Log.d(DEBUG_TAG, "onDown");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //return super.onScroll(e1, e2, distanceX, distanceY);
        if (distanceY < 0 && this.mChanged == false &&  this.mIndex >= 0){
            this.mChanged = true;
            if (mIndex != 0) this.mIndex--;
            mMainController.getMethodName(this.mIndex);
        } else if (distanceY > 0 && this.mChanged == false && this.mIndex <= 4){
            if (mIndex != 4) this.mIndex++;
            mMainController.getMethodName(this.mIndex);
            this.mChanged = true;
        }
        if(mIndexListener != null) mIndexListener.getIndex(mIndex);
        Log.d(DEBUG_TAG, "onScroll " + distanceX + " " + distanceY + " " + mIndex);
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //return super.onFling(e1, e2, velocityX, velocityY);
        this.mChanged = false;
        Log.d(DEBUG_TAG, "onFling");

        return true;
    }
}
