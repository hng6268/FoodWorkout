package com.example.android.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import java.util.ArrayList;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class work extends Drawer {
    private DatabaseReference tDatabase;
    private ListView tListView;
    String gdr, selectedworkout;
    double wgt,kcal_h;


    EditText inputSearch;
    ArrayAdapter<String> arrayAdapter;
    //user gender


    private ArrayList<Double> tkcal = new ArrayList<>();
    private ArrayList<String> tkey = new ArrayList<>();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_food);
        //storing the value sent from menuactivity
        Intent intent= getIntent();
        gdr = intent.getStringExtra("genders");
        wgt = Double.parseDouble(intent.getStringExtra("weight"));

        tListView = (ListView) findViewById(R.id.list_view);

        inputSearch = (EditText) findViewById(R.id.inputSearch);

        // Adding workout list to listview
        tDatabase = FirebaseDatabase.getInstance().getReference().child("workout").child(gdr);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, tkey);
        tListView.setAdapter(arrayAdapter);
        //get selected value in listview and put in variable
        tListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,int position, long arg3) {
                        selectedworkout = tListView.getItemAtPosition(position).toString();
                        kcal_h = tkcal.get(position)* wgt;
                        Intent i = new Intent(work.this, work_result.class);
                        i.putExtra("workout",selectedworkout);
                        i.putExtra("kcal/h",kcal_h);
                        startActivity(i);
                    }
                }
        );

        tDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                double cal = dataSnapshot.getValue(double.class);
                tkcal.add(cal);

                String key = dataSnapshot.getKey();
                tkey.add(key);

                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                double cal = dataSnapshot.getValue(double.class);
                String key = dataSnapshot.getKey();

                int index = tkey.indexOf(key);

                tkcal.set(index, cal);

                arrayAdapter.notifyDataSetChanged();

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

        /**
         * Enabling Search Filter
         * */
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                work.this.arrayAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

    }
    //method that check whether the touch is outside the Edittext location or not  if yes close the soft keyboard
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        if (ev.getAction() == MotionEvent.ACTION_DOWN &&
                !getLocationOnScreen(inputSearch).contains(x, y)) {
            InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            input.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
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
}