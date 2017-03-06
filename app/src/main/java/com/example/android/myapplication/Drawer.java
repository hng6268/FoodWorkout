package com.example.android.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Drawer extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    String[] items = new String[]{"Home","Menu"};



    public ListView drawerList;
    private Toolbar toolbar;

    private ActionBarDrawerToggle drawerToggle;



    protected void onCreate(Bundle savedInstanceState, int resLayoutID)
    {
        super.onCreate(savedInstanceState);
        setContentView(resLayoutID);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

    //set the title for the actionbar when the drawer is open or close
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer , 0,0)
        {
            public void onDrawerClosed(View view)
            {
                getSupportActionBar().setTitle(R.string.app_name);
            }

            public void onDrawerOpened(View drawerView)
            {
                getSupportActionBar().setTitle(R.string.menu);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeButtonEnabled(true);

        drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
        //Items is an ArrayList or array with the items you want to put in the Navigation Drawer.
        //check which index of the listview is clicked and intent to particular activity
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                Intent i = null;

                switch(pos){
                    case 0:
                        i = new Intent(Drawer.this, Profile.class);
                        break;
                    case 1:
                        i = new Intent(Drawer.this, MenuActivity.class);
                        break;
                }
                startActivity(i);
            }

        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);

        drawerToggle.syncState();

    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        drawerToggle.onConfigurationChanged(newConfig);

    }

}
