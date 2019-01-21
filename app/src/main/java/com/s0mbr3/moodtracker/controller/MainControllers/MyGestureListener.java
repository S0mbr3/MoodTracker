package com.s0mbr3.moodtracker.controller.MainControllers;

import android.text.Layout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Oxhart on 21/01/2019.
 */
public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String DEBUG_TAG = "GESTURES";
    private LinearLayout mLayout;
    private ImageView mSmiley;
    private MainController mainController;
    private int mIndex;
    private boolean changed;

    public MyGestureListener(LinearLayout layout, ImageView smiley, int index){
        this.mSmiley = smiley;
        this.mLayout = layout;
        this.mainController = new MainController(layout, smiley);
        this.mIndex = index;
        this.changed = false;

        mainController.getMethodName(3);
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
        Log.d(DEBUG_TAG, "onScroll " + distanceX + " " + distanceY);
        if (distanceY < 0 && this.changed == false &&  this.mIndex >= 0){
            this.changed = true;
            if (mIndex != 0) this.mIndex--;
            Log.d(DEBUG_TAG, String.valueOf(this.mIndex));
            mainController.getMethodName(this.mIndex);
        } else if (distanceY > 0 && this.changed == false && this.mIndex <= 4){
            if (mIndex != 4) this.mIndex++;
            Log.d(DEBUG_TAG, String.valueOf(this.mIndex));
            mainController.getMethodName(this.mIndex);
            this.changed = true;
        }
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //return super.onFling(e1, e2, velocityX, velocityY);
        this.changed = false;
        Log.d(DEBUG_TAG, "onFling");

        return true;
    }
}
