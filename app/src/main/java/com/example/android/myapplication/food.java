package com.example.android.myapplication;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class food extends Drawer implements DataCallBack.OnReceivedFood{
    //databse reference to food table
    private DatabaseReference tDatabase;
    //database reference to work table
    private DatabaseReference wDatabase;
    private ListView tListView;
    String selectedfood,gender,workout;
    double weight,cal,result_time;



    EditText inputSearch;
    ArrayAdapter<String> arrayAdapter;

    private ArrayList<Long> tFood = new ArrayList<>();
    private ArrayList<String> tkey = new ArrayList<>();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_food);
        //storing value that sent from menuactivity
        Intent intent = getIntent();
        workout = intent.getStringExtra("workout");
        gender = intent.getStringExtra("genders");
        weight = Double.parseDouble(intent.getStringExtra("weight"));

        //address the reference to food table
        tDatabase = FirebaseDatabase.getInstance().getReference().child("food");
        //address the reference to work table based one the value passed over
        wDatabase = FirebaseDatabase.getInstance().getReference().child("workout").child(gender).child(workout);
        tListView = (ListView) findViewById(R.id.list_view);

        inputSearch = (EditText) findViewById(R.id.inputSearch);

        // Adding items to listview
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, tkey);
        tListView.setAdapter(arrayAdapter);

        wDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double kcal = dataSnapshot.getValue(double.class);
                onDatabaseInteraction(kcal);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        tDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                long food = dataSnapshot.getValue(long.class);
                tFood.add(food);

                String key = dataSnapshot.getKey();
                tkey.add(key);

                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                long food = dataSnapshot.getValue(long.class);
                String key = dataSnapshot.getKey();

                int index = tkey.indexOf(key);

                tFood.set(index, food);

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
                food.this.arrayAdapter.getFilter().filter(cs);
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

    @Override
    public void onDatabaseInteraction(final Double kcal) {
        //listview active when item click
        tListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,int position, long arg3) {
                        selectedfood = tListView.getItemAtPosition(position).toString();
                        cal = tFood.get(position);
                        result_time = cal / ((kcal * weight )/60);
                        //passing value to food_result activity
                        Intent i = new Intent(food.this, food_result.class);
                        i.putExtra("food",selectedfood);
                        i.putExtra("cal",cal);
                        i.putExtra("workout", workout);
                        i.putExtra("result", result_time);
                        startActivity(i);
                    }
                }
        );
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
