package com.example.android.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class Profile extends Drawer implements DataCallBack.OnReceivedDatabase {
    private DatabaseReference wDatabase;
    private DatabaseReference pDatabase;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btnSave;
    private EditText bEdit;
    private Spinner dynamicSpinner;
    private ArrayList<String> wkey = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String gender="male", workout;

    //method that check whether the touch is outside the Edittext location or not  if yes close the soft keyboard
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        if (ev.getAction() == MotionEvent.ACTION_DOWN &&
                !getLocationOnScreen(bEdit).contains(x, y)) {
            InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            input.hideSoftInputFromWindow(bEdit.getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    //Method that caculates 4 corner of EditText
    protected Rect getLocationOnScreen(EditText mEditText) {
        Rect mRect = new Rect();
        int[] location = new int[2];

        mEditText.getLocationOnScreen(location);

        mRect.left = location[0];
        mRect.top = location[1];
        mRect.right = location[0] + mEditText.getWidth();
        mRect.bottom = location[1] + mEditText.getHeight();

        return mRect;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_profile);

        //For Weight Button
        btnSave = (Button) findViewById(R.id.save_btn);
        bEdit = (EditText) findViewById(R.id.EditText);

        //For Spinner (Selection workout)
        dynamicSpinner = (Spinner) findViewById(R.id.work_spinner);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wkey);
        dynamicSpinner.setAdapter(adapter);
        //connect to workout database
        wDatabase = FirebaseDatabase.getInstance().getReference().child("workout").child("male");

        //Retrieve workout list from Database
        wDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                wkey.add(key);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                int index = wkey.indexOf(key);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //set reference to connect to firebase particular table/child
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

        // Retrieve User Selection in Spinner
        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                workout = wkey.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        //Add into Database
        pDatabase = FirebaseDatabase.getInstance().getReference().child("profile");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> dataMap = new HashMap<String, String>();

                String weight = bEdit.getText().toString().trim();
                dataMap.put("gender", gender);
                dataMap.put("weight", weight);
                dataMap.put("workout", workout);

                pDatabase.setValue(dataMap);

                Intent i = new Intent(Profile.this, MenuActivity.class);
                startActivity(i);
            }
        });

    }

    //Gender Radio
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioMale:
                if (checked)
                    gender = "male";
                break;
            case R.id.radioFemale:
                if (checked)
                    gender = "female";
                break;
            default:
                gender = "";
                break;
        }
    }
    //callback function .... have profile info at this stage
    @Override
    public void onDatabaseInteraction(final String gender, final String weight, final String workout) {
        switch (gender){
            case "male":
                RadioButton rb_male = (RadioButton)findViewById(R.id.radioMale);
                rb_male.setChecked(true);
                break;
            case "female":
                RadioButton rb_female = (RadioButton)findViewById(R.id.radioFemale);
                rb_female.setChecked(true);
                break;
            default:
                RadioButton rb_m = (RadioButton)findViewById(R.id.radioMale);
                rb_m.setChecked(false);
                RadioButton rb_f = (RadioButton)findViewById(R.id.radioFemale);
                rb_f.setChecked(false);
                break;
        }
       bEdit.setText(weight);
       dynamicSpinner.setSelection(adapter.getPosition(workout));

    }

}
