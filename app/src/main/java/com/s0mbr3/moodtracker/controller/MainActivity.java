package com.s0mbr3.moodtracker.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.model.Humor;

public class MainActivity extends AppCompatActivity {

    private ImageView mSmiley;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSmiley = findViewById(R.id.activity_main_smiley_image);
        Humor humor = new Humor(mSmiley);
        humor.getSadSmiley();
        //mSmiley.setImageResource(R.drawable.smiley_disappointed);

    }
}
