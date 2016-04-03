package com.cs2063.nfcw.nfcwayfinder;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by beaul on 2016-03-28.
 */
public class RoomPickerFragment extends Fragment {
    private static final String TAG = "RoomPickerFragment";

    MainActivity mainActivity;
    ArrayList<String> roomStringList;
    HashMap<String, String> nameToRoomIDs;
    FloatingActionButton fabContinue;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "Entered RoomPickerFragment onCreateView");
        View view = inflater.inflate(R.layout.room_picker_fragment, container, false);
        mainActivity = ((MainActivity) getActivity());
        mainActivity.menuMultipleActions.setVisibility(View.VISIBLE);

        Bundle fromPreviousFragment = getArguments();

        final AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.destination);

        nameToRoomIDs = new HashMap<>();
        roomStringList = new ArrayList<>();

        fabContinue = new FloatingActionButton(mainActivity.getBaseContext());
        fabContinue.setTitle("Continue");
        fabContinue.setIcon(R.drawable.forward_icon);
        fabContinue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mainActivity.menuMultipleActions.collapse();

                String selectedDestination = textView.getText().toString();
                if (roomStringList.contains(selectedDestination))
                {
                    NavigationFragment f = new NavigationFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                    ft.replace(R.id.fragment_location, f);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else
                {
                    Toast toast = Toast.makeText(mainActivity.getApplicationContext(), "Room does" +
                                    " not exist",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        ArrayList<String> roomList = mainActivity.firebaseManager.visibleRoomIDs;
        for (String id:roomList)
        {
            Log.d(TAG, id);
            Room room = mainActivity.firebaseManager.roomMap.get(id);
            Log.d(TAG, "Room name: " + room.roomName + " room id: "+room.roomNumber);
            roomStringList.add(room.roomName);
            nameToRoomIDs.put(room.roomName, id);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity.getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line, roomStringList);
        textView.setAdapter(adapter);

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
