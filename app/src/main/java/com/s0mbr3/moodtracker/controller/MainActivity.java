package com.s0mbr3.moodtracker.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.controller.MainControllers.MainController;
import com.s0mbr3.moodtracker.model.Humor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView mSmiley;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSmiley = findViewById(R.id.activity_main_smiley_image);
        try {
            MainController mainController = new MainController(mSmiley);
            mainController.getMethodName();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //mSmiley.setImageResource(R.drawable.smiley_disappointed);

    }
}
