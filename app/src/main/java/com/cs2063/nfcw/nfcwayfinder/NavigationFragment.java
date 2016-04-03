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

    private static final String TAG = "NavigationFragment";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "Entered LocationFragment onCreateView");
        View view = inflater.inflate(R.layout.navigation_fragment, container, false);

        mainActivity = ((MainActivity) getActivity());
        mainActivity.menuMultipleActions.setVisibility(View.GONE);

        return view;
    }
}