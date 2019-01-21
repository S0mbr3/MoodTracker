package com.s0mbr3.moodtracker.controller.MainControllers;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Oxhart on 21/01/2019.
 */
public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String DEBUG_TAG = "GESTURES";
    private ImageView mSmiley;
    private MainController mainController;
    private int mIndex;

    public MyGestureListener(ImageView smiley, int index){
        this.mSmiley = smiley;
        this.mainController = new MainController(smiley);
        this.mIndex = index;

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
        if (distanceY < 0 && this.mIndex > 0){

            mainController.getMethodName(this.mIndex--);
        } else if (distanceY > 0 && this.mIndex < 4){
            mainController.getMethodName(this.mIndex++);
        }
        return true;
    }
}
