package com.cs2063.nfcw.nfcwayfinder;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by beaul on 2016-03-13.
 */
public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "Entered MainFragment onCreateView");
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
