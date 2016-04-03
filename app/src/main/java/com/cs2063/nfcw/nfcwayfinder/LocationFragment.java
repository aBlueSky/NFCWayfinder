package com.cs2063.nfcw.nfcwayfinder;

import android.app.Fragment;
import android.app.FragmentTransaction;
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

    FloatingActionButton fabNavigate;
    FloatingActionButton fabViewMap;

    private static final String TAG = "LocationFragment";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "Entered LocationFragment onCreateView");
        View view = inflater.inflate(R.layout.location_fragment, container, false);

        mainActivity = ((MainActivity) getActivity());
        mainActivity.menuMultipleActions.setVisibility(View.VISIBLE);

        ImageView imageView = (ImageView) view.findViewById(R.id.mapView);
        imageView.setImageResource(R.drawable.first_floor3);

        fabNavigate = new FloatingActionButton(mainActivity.getBaseContext());
        fabNavigate.setTitle("Navigate");
        fabNavigate.setIcon(R.drawable.compass_icon);
        fabNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.menuMultipleActions.collapse();

                RoomPickerFragment f = new RoomPickerFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.replace(R.id.fragment_location, f);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        fabViewMap = new FloatingActionButton(mainActivity.getBaseContext());
        fabViewMap.setTitle("View Full Map");
        fabViewMap.setIcon(R.drawable.map_icon);
        fabViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.menuMultipleActions.collapse();

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        mainActivity.menuMultipleActions.addButton(fabNavigate);
        mainActivity.menuMultipleActions.addButton(fabViewMap);
        super.onResume();
    }

    @Override
    public void onPause() {
        mainActivity.menuMultipleActions.removeButton(fabNavigate);
        mainActivity.menuMultipleActions.removeButton(fabViewMap);
        super.onPause();
    }

}