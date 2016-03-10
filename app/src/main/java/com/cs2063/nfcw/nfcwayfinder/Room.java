package com.cs2063.nfcw.nfcwayfinder;

/**
 * Created by Drew on 20/02/2016.
 */
public class Room implements Comparable
{

    // Room attributes
    String building = "None";
    String level = "None";
    String roomNumber = "None";

    // Empty Constructor
    public Room() {}

    //TODO: Remove, this is a debug constructor.
    public Room(String id)
    {
        roomNumber = id;
    }

    //Full constructor
    public Room(String roomNumber, String level, String building)
    {
        this.building = building;
        this.level = level;
        this.roomNumber = roomNumber;
    }

    public String getBuilding() {
        return building;
    }

    public String getLevel() { return level; }

    public String getRoomNumber() { return roomNumber; }

    @Override
    //This allows rooms to be compared and ordered against each other.
    public int compareTo(Object room) {
        if(this.roomNumber == ((Room)room).roomNumber)
            return 0;
        else
            return Integer.parseInt(this.roomNumber) > Integer.parseInt(((Room)room).roomNumber) ? 1 : -1;
    }
}
