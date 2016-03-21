package com.cs2063.nfcw.nfcwayfinder;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Koves on 3/21/2016.
 */
public class FragmentSecondary extends Fragment
{
    private static final String TAG = "FragmentSecondary";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "Entered DefaultFragment onCreateView");
        return inflater.inflate(R.layout.fragment_secondary, container, false);
    }
}