package com.s0mbr3.moodtracker.controller;

import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.controller.MainControllers.MyGestureListener;


public class MainActivity extends AppCompatActivity {

    private ImageView mSmiley;
    private ConstraintLayout mLayout;
    private GestureDetectorCompat mDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mSmiley = findViewById(R.id.activity_main_smiley_image);
        this.mLayout = findViewById(R.id.activity_main_layout);
        this.mDetector = new GestureDetectorCompat(this, new MyGestureListener(this.mLayout, this.mSmiley, 3));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
