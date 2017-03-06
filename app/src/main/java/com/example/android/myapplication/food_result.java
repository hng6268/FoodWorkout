package com.example.android.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import static android.R.attr.defaultValue;

public class food_result extends Drawer {
    String food, workout;
    int cal , worktime;
    private Drawable background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_food_result);
        Intent intent = getIntent();
        food = intent.getStringExtra("food");
        workout = intent.getStringExtra("workout");
        cal = (int)intent.getDoubleExtra("cal",defaultValue);
        worktime = (int)intent.getDoubleExtra("result",defaultValue)+1;
        //setting the background to haft tranparent
        background = findViewById(R.id.foodpage).getBackground();
        background.setAlpha(127);

    //setting the result  to textview
        TextView foodresult = (TextView)findViewById(R.id.food_result);
        TextView calresult = (TextView)findViewById(R.id.cal_result);
        TextView workresult = (TextView)findViewById(R.id.work_result);
        foodresult.setText(food);
        calresult.setText(cal+"  kcals");
        workresult.setText(worktime + "min \n" + workout);
    }
}
