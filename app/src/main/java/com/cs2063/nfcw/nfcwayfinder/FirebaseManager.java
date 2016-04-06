package com.cs2063.nfcw.nfcwayfinder;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

//import com.firebase.client.Firebase;

/**
 * Created by Koves on 2/22/2016.
 */
public class FirebaseManager
{
    private final static String TAG = "FirebaseManager";
    private Firebase firebase;
    public HashMap<String, Room> roomMap;
    public ArrayList<String> visibleRoomIDs;
    private ArrayList<Edge> edgeList;
    private MainActivity mainActivity;

    public FirebaseManager(MainActivity mIn)
    {
        mainActivity = mIn;
        firebase = new Firebase("https://nfcwayfinder.firebaseio.com/");
        roomMap = new HashMap<>();
        visibleRoomIDs = new ArrayList<String>();
        edgeList = new ArrayList<Edge>();
    }

    //Based on the building parameter, retrieve from Firebase all relevant rooms and then swap them.
    public void getBuilding(final String building, final String level, final String roomNumber,
                            final MainActivity mainActivity)
    {
        //Log.d(TAG, "getBuilding() called.");
        firebase.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //Log.d(TAG, "onDataChange() called.");
                roomMap.clear();
                edgeList.clear();
                visibleRoomIDs.clear();

                DataSnapshot buildingSnapshot = dataSnapshot.child("Buildings").child(building);
                //Log.d(TAG, "Snapshot for building: " + building);

                for (DataSnapshot levels : buildingSnapshot.child("Levels").getChildren())
                {
                    //Log.d(TAG, "Rooms: ");
                    String level = levels.getKey();
                    for (DataSnapshot room : levels.child("Rooms").getChildren())
                    {
                        String roomNumber = room.getKey();
                        String roomName = room.child("Name").getValue().toString();
                        int x = Integer.parseInt(room.child("X").getValue().toString());
                        int y = Integer.parseInt(room.child("Y").getValue().toString());
                        int type = Integer.parseInt(room.child("type").getValue().toString());
                        String info = room.child("Info").getValue().toString();
                        if (type == 1)
                        {
                            visibleRoomIDs.add(roomNumber);
                        }
                        roomMap.put(roomNumber, new Room(roomNumber, roomName, level, building,
                                x / 2, y / 2, type, info));
                        //Log.d(TAG, "Building: " + building + "\tLevel: " + level + "\tRoom: " + roomNumber
                        //        + "\tRoom Name: " + roomName + "\tX-Y: " + x / 2 + "-" + y / 2);
                    }

                    //Log.d(TAG, "Edges: ");
                    if (levels.child("Paths").getValue() != null)
                    {
                        GenericTypeIndicator<ArrayList<String>> t = new
                                GenericTypeIndicator<ArrayList<String>>()
                                {
                                };
                        ArrayList<String> listOfConcatenatedEdges = levels.child("Paths").getValue
                                (t);
                        for (String concatenatedEdge : listOfConcatenatedEdges)
                        {
                            //Log.d(TAG, "Edge is: " + concatenatedEdge);
                            String[] edgeEnds = concatenatedEdge.split("-");
                            Room firstEnd = (roomMap.containsKey(edgeEnds[0]) ? roomMap.get(edgeEnds[0])
                                                                              : null);
                            Room secondEnd = (roomMap.containsKey(edgeEnds[1]) ? roomMap.get(edgeEnds[1])
                                                                               : null);
                            if (firstEnd != null && secondEnd != null)
                            {
                                Edge edge = new Edge(firstEnd, secondEnd, true);
                                edgeList.add(edge);
                                firstEnd.neighbours.add(edge);
                                secondEnd.neighbours.add(edge);
                            }
                        }
                    }
                    else
                    {
                        //Log.d(TAG, "Edge list for level " + level + " is empty.");
                    }
                }

                //Elevation related edges handled now that all floors have rooms initialised.
                for (DataSnapshot path : buildingSnapshot.child("Interconnect").getChildren())
                {
                    String concatenatedEdge = path.getKey();
                    int accessFlag = Integer.parseInt(path.getValue().toString());
                    String[] edgeEnds = concatenatedEdge.split("-");
                    //Log.d(TAG, "Interconnected edge: " + concatenatedEdge + "isAccessible: " +
                    //        (accessFlag == 1));
                    Room firstEnd = (roomMap.containsKey(edgeEnds[0]) ? roomMap.get(edgeEnds[0])
                                                                      : null);
                    Room secondEnd = (roomMap.containsKey(edgeEnds[1]) ? roomMap.get(edgeEnds[1])
                                                                       : null);
                    if (firstEnd != null && secondEnd != null)
                    {
                        Edge e = new Edge(firstEnd, secondEnd, (accessFlag == 1));
                        edgeList.add(e);
                        firstEnd.neighbours.add(e);
                        secondEnd.neighbours.add(e);
                    }
                }

                //Trigger next fragment transaction.
                mainActivity.goToLocationFragment(roomMap.containsKey(roomNumber) ? roomMap.get
                        (roomNumber) : null);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError)
            {
            }
        });
    }

    public class WeightedRoom
    {
        WeightedRoom parent;
        Room room;
        int g;
        int h;
        int f;

        public WeightedRoom(Room r, WeightedRoom parent)
        {
            this.room = r;
            this.parent = parent;
        }
    }

    public int estimateDistance(Room room1, Room room2)
    {
        return Math.abs(room1.x - room2.x) + Math.abs(room1.y - room2.y);
    }

    public ArrayList<Room> findPath(Room start, Room goal) {
        Set<WeightedRoom> open = new HashSet<>();
        Set<WeightedRoom> closed = new HashSet<>();

        int goalFloor = Integer.parseInt(goal.getLevel());

        WeightedRoom wStart = new WeightedRoom(start, null);
        wStart.g = 0;
        wStart.h = estimateDistance(start, goal);
        wStart.f = wStart.h;

        open.add(wStart);

        while (open.size() > 0) {
            WeightedRoom current = null;

            for (WeightedRoom wRoom : open) {
                if (current == null || wRoom.f < current.f) {
                    current = wRoom;
                }
            }

            //Log.d(TAG, "" + current.room.roomName);

            if (current.room.getRoomNumber() == goal.getRoomNumber()) {
                //Success
                ArrayList<Room> path = new ArrayList<>();
                path.add(current.room);
                while (current.parent != null) {
                    current = current.parent;
                    path.add(0, current.room);
                }

                return path;
            }

            open.remove(current);
            closed.add(current);

            ArrayList<Room> neighbors = current.room.getNeighbors();
            ArrayList<WeightedRoom> wNeighbors = new ArrayList<>();
            for (Room room : neighbors) {
                if(room.type == 3 && mainActivity.accessibilityFlag) { continue; }
                else { wNeighbors.add(new WeightedRoom(room, current)); }
            }
            for (WeightedRoom wRoom : wNeighbors) {
                int nextG = current.g + estimateDistance(current.room, wRoom.room);

                if (nextG < wRoom.g) {
                    open.remove(wRoom);
                    closed.remove(wRoom);
                }

                if (!open.contains(wRoom) && !closed.contains(wRoom)) {
                    wRoom.g = nextG;
                    wRoom.h = estimateDistance(wRoom.room, goal) + 10000 * Math.abs(goalFloor - Integer.parseInt(wRoom.room.getLevel()));
                    wRoom.f = wRoom.g + wRoom.h;
                    open.add(wRoom);
                }
            }
        }

        Log.d(TAG, "Search failed");
        return new ArrayList<>();
    }
}
