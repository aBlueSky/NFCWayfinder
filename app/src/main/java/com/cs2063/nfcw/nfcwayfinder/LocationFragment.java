package com.cs2063.nfcw.nfcwayfinder;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;

/**
 * Created by Koves on 3/21/2016.
 */
public class LocationFragment extends Fragment
{
    MainActivity mainActivity;

    FloatingActionButton fabContinue;
    FloatingActionButton fabReturn;

    private static final String TAG = "LocationFragment";

    private Room currentLocation;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "Entered LocationFragment onCreateView");
        View view = inflater.inflate(R.layout.location_fragment, container, false);
        Bundle bundle = getArguments();
        String roomNumber = bundle.getString("RoomNum");

        mainActivity = (MainActivity)getActivity();
        currentLocation = mainActivity.firebaseManager.roomMap.containsKey(roomNumber)?
                          mainActivity.firebaseManager.roomMap.get(roomNumber):
                          null;

        ImageView imageView = (ImageView) view.findViewById(R.id.mapView);
        if (currentLocation == null)
        {
            imageView.setImageResource(R.drawable.first_floor3);
        }
        else
        {
            switch (currentLocation.getLevel())
            {
                case "1":
                    break;
                case "2":
                    break;
            }
        }
        return view;
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