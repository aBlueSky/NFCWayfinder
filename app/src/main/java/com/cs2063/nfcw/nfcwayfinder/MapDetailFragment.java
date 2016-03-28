package com.cs2063.nfcw.nfcwayfinder;

import android.app.Activity;
//import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * Created by Drew on 25/03/2016.
 */
public class MapDetailFragment extends Fragment{
    public static final String BUILDING = "building";
    public static final String FLOOR = "floor";
    public static final String ROOM = "room";

    private String building;
    private String floor;
    private String room;

    public MapDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(FLOOR)) {

            building = getArguments().getString(BUILDING);
            floor = getArguments().getString(FLOOR);
            room = getArguments().getString(ROOM);

            Activity activity = this.getActivity();
            //CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            //if (appBarLayout != null) {
            //    appBarLayout.setTitle("Map Details");
            //}
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (room != null && floor != null) {
            ((TextView) rootView.findViewById(R.id.map_detail)).setText("Building: " + building + "\n" +
                    "Floor: " + floor + "\n" +
                    "Room: " + room);
        }

        return rootView;
    }
}
