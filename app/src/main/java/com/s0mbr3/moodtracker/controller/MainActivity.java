package com.s0mbr3.moodtracker.controller;

import android.content.DialogInterface;
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
import com.s0mbr3.moodtracker.controller.MainControllers.MyGestureListener;


public class MainActivity extends AppCompatActivity {

    private ImageView mSmiley;
    private Button mCommentBtn;
    private ConstraintLayout mLayout;
    private GestureDetectorCompat mDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mSmiley = findViewById(R.id.activity_main_smiley_image);
        this.mLayout = findViewById(R.id.activity_main_layout);
        this.mCommentBtn = findViewById(R.id.activity_main_comment_btn);

        this.mDetector = new GestureDetectorCompat(this, new MyGestureListener(this.mLayout, this.mSmiley, 3));

        this.mCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void addComment(){
        final EditText commentInput = new EditText(this);

// Set the default text to a link of the Queen
        commentInput.setHint("Commentez votre Humeur!");

        new AlertDialog.Builder(this)
                .setTitle("Commentaire")
                .setView(commentInput)
                .setPositiveButton("VALIDER", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String url = commentInput.getText().toString();
                    }
                })
                .setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }
}
