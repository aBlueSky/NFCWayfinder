package com.cs2063.nfcw.nfcwayfinder;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by beaul on 2016-04-03.
 */
public class AboutFragment extends Fragment
{
    private static final String TAG = "AboutFragment";

    private MainActivity mainActivity;
    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "Entered AboutFragment onCreateView");

        view = inflater.inflate(R.layout.about_fragment, container, false);

        mainActivity = ((MainActivity) getActivity());
        mainActivity.menuMultipleActions.setVisibility(View.GONE);

        return view;
    }
}
