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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "Entered DefaultFragment onCreateView");
        ((MainActivity)getActivity()).menuMultipleActions.setVisibility(View.GONE);
        return inflater.inflate(R.layout.default_fragment, container, false);
    }
}
