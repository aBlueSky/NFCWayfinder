package com.cs2063.nfcw.nfcwayfinder;

import android.content.Context;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.security.Key;

//import com.firebase.client.Firebase;

/**
 * Created by Koves on 2/22/2016.
 */
public class FirebaseManager
{
    private final static String TAG = "FirebaseManager";
    private Firebase firebase;
    private String lastBuildingloaded;

    public FirebaseManager()
    {
        firebase = new Firebase("https://nfcwayfinder.firebaseio.com/");
        lastBuildingloaded = null;
    }

    public String getBuilding(final String building)
    {
        firebase.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(lastBuildingloaded.equals(building)) return;
                lastBuildingloaded = building;
                DataSnapshot buildingSnapshot = dataSnapshot.child("Buildings").child(building)
                        .child("Levels");
                int numLvls = (int) buildingSnapshot.getChildrenCount();
                Log.d(TAG, "Snapshot for building: " + building);
                Log.d(TAG, "Number of levels: " + numLvls);

                for (DataSnapshot levels:buildingSnapshot.getChildren())
                {
                    String level = levels.getKey();
                    for (DataSnapshot room:levels.child("Rooms").getChildren())
                    {
                        String roomNumber = room.getKey();
                        int x = Integer.parseInt(room.child("X").getValue().toString());
                        int y = Integer.parseInt(room.child("Y").getValue().toString());
                        Log.d(TAG, "Building: "+building+"\tLevel: "+level+ "\tRoom: "+ roomNumber
                                + "\tX-Y: " + x + "-" + y);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError)
            {
            }
        });
        return "";
    }
}
