package com.cs2063.nfcw.nfcwayfinder;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

/**
 * Created by beaul on 2016-03-28.
 */
public class RoomPickerFragment extends Fragment {
    private static final String TAG = "RoomPickerFragment";

    MainActivity mainActivity;

    FloatingActionButton fabContinue;
    FloatingActionButton fabReturn;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "Entered RoomPickerFragment onCreateView");
        View view = inflater.inflate(R.layout.location_fragment, container, false);
        mainActivity = ((MainActivity) getActivity());
        mainActivity.menuMultipleActions.setVisibility(View.VISIBLE);

        fabContinue = new FloatingActionButton(mainActivity.getBaseContext());
        fabContinue.setTitle("Navigate");
        fabContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });

        fabReturn = new FloatingActionButton(mainActivity.getBaseContext());
        fabReturn.setTitle("Go Back");
        fabReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        //        android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.destination);
        //textView.setAdapter(adapter);

        return view;
    }

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
}
