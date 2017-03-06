package com.example.android.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//class to store value (weight, gender, workout) from firebase
class Post {

    public String weight;
    public String gender;
    public String workout;

    public Post ( ) { };
    public Post (String weight, String gender, String workout) {
        this.weight = weight;
        this.gender = gender;
        this.workout = workout;
    }

    public String getWorkout() {
        return workout;
    }

    public void setWorkout(String workout) {
        this.workout = workout;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

public class MenuActivity extends Drawer implements DataCallBack.OnReceivedDatabase {

    private DatabaseReference pDatabase;
    Button btnwork, btnfood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_menu);
        btnwork = (Button)findViewById(R.id.work_btn);
        btnfood = (Button)findViewById(R.id.food_btn);

        pDatabase = FirebaseDatabase.getInstance().getReference().child("profile");

        pDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                onDatabaseInteraction(post.gender,post.weight,post.workout);

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });


    }

    @Override
    public void onDatabaseInteraction(final String gender, final String weight, final String workout) {
        //send gender and weight to work activity on button click
        btnwork.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(MenuActivity.this, work.class);
                i.putExtra("genders", gender);
                i.putExtra("weight", weight);
                startActivity(i);
            }

        });
        //send workout and weight to food activity on button click
        btnfood.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(MenuActivity.this, food.class);
                i.putExtra("genders", gender);
                i.putExtra("workout", workout);
                i.putExtra("weight", weight);
                startActivity(i);
            }

        });

    }
}
