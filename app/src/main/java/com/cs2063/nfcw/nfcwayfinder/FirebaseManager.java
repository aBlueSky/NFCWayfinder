package com.cs2063.nfcw.nfcwayfinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

//import com.firebase.client.Firebase;

/**
 * Created by Koves on 2/22/2016.
 */
public class FirebaseManager
{
    private final static String TAG = "FirebaseManager";
    private Firebase firebase;
    public HashMap<String, Room> roomMap;
    private ArrayList<Edge> edgeList;

    public FirebaseManager()
    {
        firebase = new Firebase("https://nfcwayfinder.firebaseio.com/");
        roomMap = new HashMap<>();
        edgeList = new ArrayList<Edge>();
    }

    //Based on the building parameter, retrieve from Firebase all relevant rooms and then swap them.
    public void getBuilding(final String building, final MainActivity mainActivity)
    {
        Log.d(TAG, "getBuilding() called.");
        firebase.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.d(TAG, "onDataChange() called.");
                roomMap.clear();
                edgeList.clear();

                DataSnapshot buildingSnapshot = dataSnapshot.child("Buildings").child(building)
                        .child("Levels");
                int numLvls = (int) buildingSnapshot.getChildrenCount();
                Log.d(TAG, "Snapshot for building: " + building);

                for (DataSnapshot levels : buildingSnapshot.getChildren())
                {
                    Log.d(TAG, "Rooms: ");
                    String level = levels.getKey();
                    for (DataSnapshot room : levels.child("Rooms").getChildren())
                    {
                        String roomNumber = room.getKey();
                        String roomName = room.child("Name").getValue().toString();
                        int x = Integer.parseInt(room.child("X").getValue().toString());
                        int y = Integer.parseInt(room.child("Y").getValue().toString());
                        roomMap.put(roomNumber, new Room(roomNumber, roomName, level, building,
                                x/2, y/2));
                        Log.d(TAG, "Building: " + building + "\tLevel: " + level + "\tRoom: " + roomNumber
                                + "\tRoom Name: " + roomName + "\tX-Y: " + x/2 + "-" + y/2);
                    }

                    Log.d(TAG, "Edges: ");
                    if(levels.child("Paths").getValue() != null)
                    {
                        GenericTypeIndicator<ArrayList<String>> t = new
                                GenericTypeIndicator<ArrayList<String>>(){};
                        ArrayList<String> listOfConcatenatedEdges = levels.child("Paths").getValue
                                (t);
                        for (String concatenatedEdge: listOfConcatenatedEdges)
                        {
                            Log.d(TAG, "Edge is: " + concatenatedEdge);
                            String[] edgeEnds = concatenatedEdge.split("-");
                            Room firstEnd = null;
                            Room secondEnd = null;
                            Iterator<String> keySetIterator = roomMap.keySet().iterator();
                            while (keySetIterator.hasNext())
                            {
                                String key = keySetIterator.next();
                                Room room = roomMap.get(key);
                                if (room.roomNumber.equals(edgeEnds[0])) firstEnd = room;
                                if (room.roomNumber.equals(edgeEnds[1])) secondEnd = room;
                            }
                            Log.d(TAG, "Edge '"+ concatenatedEdge + " ' end: " + edgeEnds[0] + " " +
                                "exists: " + (firstEnd != null? true: false));
                            Log.d(TAG, "Edge '"+ concatenatedEdge + " ' end: " + edgeEnds[1] + " " +
                                    "exists: " + (secondEnd != null? true: false));
                            if(firstEnd != null && secondEnd != null)
                            {
                                Edge edge = new Edge(firstEnd, secondEnd);
                                edgeList.add(edge);
                                firstEnd.neighbours.add(edge);
                                secondEnd.neighbours.add(edge);
                            }
                        }
                    }
                    else
                    {
                        Log.d(TAG, "Edge list for level " + level + " is empty.");
                    }

                }
                mainActivity.goToLocationFragment();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError)
            {
            }
        });
    }

    public ArrayList<Room> getPathTo(Room start, Room destination)
    {
        ArrayList<Room> path = new ArrayList<>();
        if (start.compareTo(destination) == 0)
        {
            path.add(start);
            return path;
        }

        for (Edge e: start.neighbours)
        {
            if(e.visited == false)
            {
                e.visited = true;
                ArrayList<Room> childPath = getPathTo(e.otherEnd(start), destination);
                if(childPath != null)
                {
                    path.add(start);
                    path.addAll(childPath);
                    return path;
                }
            }
        }
        return path;
    }
}
