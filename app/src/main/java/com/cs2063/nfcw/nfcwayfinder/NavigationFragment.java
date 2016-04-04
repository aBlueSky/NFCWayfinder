package com.cs2063.nfcw.nfcwayfinder;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Koves on 3/21/2016.
 */
public class NavigationFragment extends Fragment
{
    MainActivity mainActivity;

    private static final String TAG = "NavigationFragment";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "Entered LocationFragment onCreateView");
        View view = inflater.inflate(R.layout.navigation_fragment, container, false);

        mainActivity = ((MainActivity) getActivity());
        mainActivity.menuMultipleActions.setVisibility(View.GONE);

        Bundle args = getArguments();
        Room startLocation = mainActivity.firebaseManager.roomMap.get(args.getString
                ("StartLocationID"));
        Room destinationLocation = mainActivity.firebaseManager.roomMap.get(args.getString
                ("DestinationLocationID"));

        TextView txtStartLocation = (TextView) view.findViewById(R.id.start_room_name);
        TextView txtDestinationLocation = (TextView) view.findViewById(R.id.end_room_name);

        txtStartLocation.setText(startLocation.roomName);
        txtDestinationLocation.setText(destinationLocation.roomName);

        ArrayList<Room> results = mainActivity.firebaseManager.findPath(startLocation,
                destinationLocation);
        Log.d(TAG, "Returned from find path().");
        if(results!=null && !results.isEmpty())
        {
            Log.d(TAG, "Result path from " + startLocation + " to " + destinationLocation + ": ");
            for(int i = 0; i < results.size(); i++)
            {
                Room r = results.get(i);
                Log.d("Room", "Room : " + r.roomName);
            }
        }
        return view;
    }
}