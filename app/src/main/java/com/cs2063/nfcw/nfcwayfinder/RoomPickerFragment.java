package com.cs2063.nfcw.nfcwayfinder;

import android.app.Fragment;
import android.app.FragmentTransaction;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "Entered RoomPickerFragment onCreateView");
        View view = inflater.inflate(R.layout.room_picker_fragment, container, false);
        mainActivity = ((MainActivity) getActivity());
        mainActivity.menuMultipleActions.setVisibility(View.VISIBLE);

        fabContinue = new FloatingActionButton(mainActivity.getBaseContext());
        fabContinue.setTitle("Continue");
        fabContinue.setIcon(R.drawable.forward_icon);
        fabContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.menuMultipleActions.collapse();

                NavigationFragment f = new NavigationFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.replace(R.id.fragment_location, f);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        //        android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        //AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.destination);
        //textView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        mainActivity.menuMultipleActions.addButton(fabContinue);
        super.onResume();
    }

    @Override
    public void onPause() {
        mainActivity.menuMultipleActions.removeButton(fabContinue);
        super.onPause();
    }

}
