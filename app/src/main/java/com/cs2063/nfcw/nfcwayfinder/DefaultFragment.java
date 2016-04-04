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
public class DefaultFragment extends Fragment {

    private static final String TAG = "DefaultFragment";

    private MainActivity mainActivity;
    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "Entered DefaultFragment onCreateView");

        mainActivity = ((MainActivity) getActivity());
        mainActivity.menuMultipleActions.setVisibility(View.GONE);

        view = inflater.inflate(R.layout.default_fragment, container, false);

        return view;
    }
}
