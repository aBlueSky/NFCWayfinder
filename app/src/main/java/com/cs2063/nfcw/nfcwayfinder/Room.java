package com.cs2063.nfcw.nfcwayfinder;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Drew on 20/02/2016.
 */
public class Room implements Comparable
{
    static Room singleton = null;

    // Room attributes
    String building = "None";
    String level = "None";
    String roomNumber = "None";
    String roomName = "None";
    int x = 0;
    int y = 0;
    int distanceTravelled = -1;
    Room parent = null;//only for search use.

    public ArrayList<Edge> neighbours;
    // Empty Constructor
    private Room() {}

    //Full constructor
    public Room(String roomNumber, String roomName, String level, String building, int x, int y)
    {
        this.building = building;
        this.level = level;
        this.roomNumber = roomNumber;
        this.roomName = roomName;
        this.neighbours = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.distanceTravelled = 9999;
    }

    public static Room getSingleton()
    {
        if (singleton == null)
        {
            singleton = new Room();
        }
        return singleton;
    }

    public String getBuilding() {
        return building;
    }

    public String getLevel() { return level; }

    public String getRoomNumber() { return roomNumber; }

    public String getRoomName() {return  roomName;}

    @Override
    //This allows rooms to be compared and ordered against each other by room number.
    public int compareTo(Object room) {
        if(this.roomNumber == ((Room)room).roomNumber)
            return 0;
        else
            return Integer.parseInt(this.roomNumber) > Integer.parseInt(((Room)room).roomNumber) ? 1 : -1;
    }
}
