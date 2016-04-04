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

    public FirebaseManager()
    {
        firebase = new Firebase("https://nfcwayfinder.firebaseio.com/");
        roomMap = new HashMap<>();
        visibleRoomIDs = new ArrayList<String>();
        edgeList = new ArrayList<Edge>();
    }

    //Based on the building parameter, retrieve from Firebase all relevant rooms and then swap them.
    public void getBuilding(final String building, final String level, final String roomNumber,
                            final MainActivity mainActivity)
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
                visibleRoomIDs.clear();

                DataSnapshot buildingSnapshot = dataSnapshot.child("Buildings").child(building);
                Log.d(TAG, "Snapshot for building: " + building);

                for (DataSnapshot levels : buildingSnapshot.child("Levels").getChildren())
                {
                    Log.d(TAG, "Rooms: ");
                    String level = levels.getKey();
                    for (DataSnapshot room : levels.child("Rooms").getChildren())
                    {
                        String roomNumber = room.getKey();
                        String roomName = room.child("Name").getValue().toString();
                        int x = Integer.parseInt(room.child("X").getValue().toString());
                        int y = Integer.parseInt(room.child("Y").getValue().toString());
                        int type = Integer.parseInt(room.child("type").getValue().toString());
                        if (type == 1)
                        {
                            visibleRoomIDs.add(roomNumber);
                        }
                        roomMap.put(roomNumber, new Room(roomNumber, roomName, level, building,
                                x / 2, y / 2, type));
                        Log.d(TAG, "Building: " + building + "\tLevel: " + level + "\tRoom: " + roomNumber
                                + "\tRoom Name: " + roomName + "\tX-Y: " + x / 2 + "-" + y / 2);
                    }

                    Log.d(TAG, "Edges: ");
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
                            Log.d(TAG, "Edge is: " + concatenatedEdge);
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
                        Log.d(TAG, "Edge list for level " + level + " is empty.");
                    }
                }

                //Elevation related edges handled now that all floors have rooms initialised.
                for (DataSnapshot path : buildingSnapshot.child("Interconnect").getChildren())
                {
                    String concatenatedEdge = path.getKey();
                    int accessFlag = Integer.parseInt(path.getValue().toString());
                    String[] edgeEnds = concatenatedEdge.split("-");
                    Log.d(TAG, "Interconnected edge: " + concatenatedEdge + "isAccessible: " +
                            (accessFlag == 1));
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
/*
    public ArrayList<Room> getPathToOriginal(Room start, Room destination)
    {
        ArrayList<Room> path = new ArrayList<>();
        if (start.compareTo(destination) == 0)
        {
            path.add(start);
            return path;
        }

        for (Edge e : start.neighbours)
        {
            if (e.visited == false)
            {
                e.visited = true;
                ArrayList<Room> childPath = getPathToOriginal(e.otherEnd(start), destination);
                if (childPath != null)
                {
                    path.add(start);
                    path.addAll(childPath);
                    return path;
                }
            }
        }
        return path;
    }

    }*/
    public ArrayList<Room> aStar(Room start, Room destination)
    {
        ArrayList<Room> closedList = new ArrayList<Room>();
        PriorityQueue<Room> openList = new PriorityQueue<Room>();

        int depth = 0;

        start.distanceTravelled = depth;
        start.parent = null;
        openList.add(start);
        Log.d(TAG, "Starting node: " + start.roomName);

        while (openList.size() > 0)
        {
            Log.d(TAG, "---------------------------------------"+depth);
            Room current = openList.peek();
            Log.d(TAG, "Inspecting Room: " + current.roomName + " Value: " + current.distanceTravelled);
            depth++;

            if(destination.compareTo(current)==0)
            {
                //Found goal node.
                Log.d(TAG, "**** Found goal node: " + current.roomName);
                ArrayList<Room> results = new ArrayList<>();

                while (current.compareTo(start)==0)
                {
                    Log.d(TAG, current.roomName);
                    results.add(0, current);
                    current = current.parent;
                }
                if(!results.isEmpty()) { results.add(0,start);}
                return results;
            }
            else
            {
                //expand neighbors.
                for (Edge e : current.neighbours)
                {
                    Room n = e.otherEnd(current);
                    if (!e.isAccessFriendly)
                    {
                        //todo add access check
                    }
                    if (!openList.contains(n) && !closedList.contains(n))
                    {
                        //Not seen before.
                        n.distanceTravelled = depth+1;
                        n.parent = current;
                        openList.add(n);
                        Log.d(TAG, "Never seen: " + n.roomName);
                    }
                    else if (openList.contains(n))
                    {
                        //Hasn't been evaluated yet, check to see if shorter path was found.
                        if (n.distanceTravelled > depth + 1)
                        {
                            Log.d(TAG, "Found a shorter path for " + n.roomName + "["+n
                                    .distanceTravelled+"->"+(depth+1)+"]");
                            n.distanceTravelled = depth + 1;
                            n.parent = current;
                        }
                    }
                }
            }
            //remove expanded node from open -> close;
            openList.remove(current);
            closedList.add(current);
        }
        return null;
    }

    public ArrayList<Room> findPath(Room start, Room dest)
    {
        PriorityQueue<WeightedRoom> openList = new PriorityQueue<>();
        ArrayList<WeightedRoom> closedList = new ArrayList<>();

        WeightedRoom wStart = new WeightedRoom(start, 0, null);
        openList.add(wStart);
        int dL = Integer.parseInt(dest.getLevel());
        int depth = 0;

        while(!openList.isEmpty())
        {
            WeightedRoom current = openList.peek();
            Log.d(TAG, current.room.roomName + "");
            openList.remove(current);
            //closedList.add(current);

            ArrayList<Room> neighbors = current.room.getNeighbors();
            ArrayList<WeightedRoom> wNeighbors = new ArrayList<>();
            for (Room r: neighbors)
            {
                r.parent = current.room;
                wNeighbors.add(new WeightedRoom(r, 999999, current.room));
            }
            for (WeightedRoom r: wNeighbors)
            {
                if(r.room.compareTo(dest) == 0)
                {
                    Log.d(TAG, "Arrived at Finish");
                    ArrayList<Room> results = new ArrayList<>();
                    Room temp = r.room;
                    results.add(temp);
                    while(temp.parent != null)
                    {
                        Log.d(TAG, temp.parent.roomName);
                        results.add(0, temp.parent);
                        temp = temp.parent;
                    }
                    Log.d(TAG, "Returning from search alg.");
                    return results;
                }
                int f;
                double g = current.distanceTravelled
                        + Math.sqrt(Math.pow(r.room.x - current.room.x, 2)
                                    + Math.pow(r.room.y - current.room.y, 2));

                int rL = Integer.parseInt(r.room.getLevel());
                int h = (rL == dL?0 : 1000 * (Math.abs(rL - dL)));
                h+=depth * 100;

                f = (int)(g + h);

                if(openList.contains(r))
                {
                    //Update the r in the open list.
                    if(f < r.distanceTravelled){r.distanceTravelled = f;}
                }
                else if(closedList.contains(r))
                {
                    //Update the r in the closed list.
                    if(f < r.distanceTravelled){r.distanceTravelled = f;}
                }
                else
                {
                    r.distanceTravelled = f;
                    openList.add(r);
                    Log.d(TAG, "added " + r.room.roomName + " distance: " + f);
                }
            }
            closedList.add(current);
            depth++;
        }
        return null;
    }

    public class WeightedRoom
    {
        Room room;
        int distanceTravelled;
        Room parent;

        public WeightedRoom(Room r, int distanceTravelled, Room parent)
        {
            this.room = r;
            this.distanceTravelled = distanceTravelled;
            this.parent = parent;
        }
    }
}
