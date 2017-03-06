package com.example.android.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DataCallBack extends Fragment {

    private OnReceivedDatabase FireListener;
    private OnReceivedFood FoodListerner;

    public DataCallBack() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_call_back, container, false);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            FireListener = (OnReceivedDatabase) context;
        } catch (RuntimeException e) {
            throw new RuntimeException(context.toString());
        }
        try {
            FoodListerner = (OnReceivedFood) context;
        } catch (RuntimeException e) {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

//callback function that contain 3 value for profile info
    public interface OnReceivedDatabase {
        void onDatabaseInteraction(String gender, String weight, String workout);
    }
//callback function that only contain one value
    public interface OnReceivedFood {
        void onDatabaseInteraction(Double kcal);
    }

}
