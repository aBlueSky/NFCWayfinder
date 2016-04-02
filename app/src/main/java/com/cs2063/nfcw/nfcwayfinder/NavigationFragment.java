package com.cs2063.nfcw.nfcwayfinder;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;

/**
 * Created by Koves on 3/21/2016.
 */
public class NavigationFragment extends Fragment
{
    MainActivity mainActivity;

    FloatingActionButton fabContinue;
    FloatingActionButton fabReturn;

    private static final String TAG = "NavigationFragment";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "Entered LocationFragment onCreateView");
        return inflater.inflate(R.layout.location_fragment, container, false);
    }
    /*
    @Override
    public void onResume() {
        mainActivity.menuMultipleActions.addButton(fabContinue);
        mainActivity.menuMultipleActions.addButton(fabReturn);
        super.onResume();
    }

    @Override
    public void onPause() {
        mainActivity.menuMultipleActions.removeButton(fabContinue);
        mainActivity.menuMultipleActions.removeButton(fabReturn);
        super.onPause();
    }
    */
}