package com.example.android.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import static android.R.attr.defaultValue;

public class work_result extends Drawer {
    String workout;
    double kcal;
    private Drawable background;
    private TextView workoutname, calperhour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_work_result);
        Intent intent = getIntent();
        workout = intent.getStringExtra("workout");
        kcal = intent.getDoubleExtra("kcal/h", defaultValue);
        //setting the background to haft tranparent
        background = findViewById(R.id.workpage).getBackground();
        background.setAlpha(127);

        //setting the result value to textview
        workoutname = (TextView) findViewById(R.id.workname);
        calperhour = (TextView)findViewById(R.id.cph);
        workoutname.setText(workout);
        calperhour.setText(kcal+" kcal/h");

    }
}
