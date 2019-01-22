package com.s0mbr3.moodtracker.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.controller.MainControllers.MainController;
import com.s0mbr3.moodtracker.controller.MainControllers.MyGestureListener;


public class MainActivity extends AppCompatActivity {

    private ImageView mSmiley;
    private Button mCommentBtn;
    private Button mHistoricBtn;
    private ConstraintLayout mLayout;
    private GestureDetectorCompat mDetector;
    private String mCommentTxt;
    private SharedPreferences mPreferences;
    public static final int  HISTORIC_ACTIVITY_REQUEST_CODE = 1337;
    public static final String PREF_KEY_COMMENT_TXT = "PREF_KEY_COMMENT_TXT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mSmiley = findViewById(R.id.activity_main_smiley_image);
        this.mLayout = findViewById(R.id.activity_main_layout);
        this.mCommentBtn = findViewById(R.id.activity_main_comment_btn);
        this.mHistoricBtn = findViewById(R.id.activity_main_historic_btn);
        this.mPreferences = getPreferences(MODE_PRIVATE);

        this.mDetector = new GestureDetectorCompat(this, new MyGestureListener(this.mLayout, this.mSmiley, 3));

        this.mCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addComment();
            }
        });
        this.historic();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void addComment(){
        final EditText commentInput = new EditText(this);
        commentInput.setHint("Commentez votre Humeur!");
        new AlertDialog.Builder(this)
                .setTitle("Commentaire")
                .setView(commentInput)
                .setPositiveButton("VALIDER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mCommentTxt = commentInput.getText().toString();
                    }
                })
                .setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    private void historic(){
       mHistoricBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              mPreferences.edit().putString(PREF_KEY_COMMENT_TXT, mCommentTxt).apply();

              Intent historicActivityIntent = new Intent(MainActivity.this, HistoricActivity.class);
              startActivityForResult(historicActivityIntent, HISTORIC_ACTIVITY_REQUEST_CODE);
           }
       });
    }
}
