package com.cs2063.nfcw.nfcwayfinder;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

/**
 * Created by beaul on 2016-03-28.
 */
public class RoomPickerFragment extends Fragment {
    private static final String TAG = "RoomPickerFragment";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "Entered RoomPickerFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_secondary, container, false);
        ((MainActivity)getActivity()).menuMultipleActions.setVisibility(View.VISIBLE);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        //        android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.destination);
        //textView.setAdapter(adapter);

        return view;
    }
}
