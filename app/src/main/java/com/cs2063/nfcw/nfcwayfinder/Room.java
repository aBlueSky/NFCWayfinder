package com.cs2063.nfcw.nfcwayfinder;

/**
 * Created by Drew on 20/02/2016.
 */
public class Room
{

    // Room attributes
    String complex = "None";
    String building = "None";
    String level = "None";
    String roomNumber = "None";

    // Empty Constructor
    public Room() {

    }

    public Room(String id)
    {
        roomNumber = id;
    }

    public String getComplex() { return complex; }

    public String getBuilding() {
        return building;
    }

    public String getLevel() { return level; }

    public String getRoomNumber() { return roomNumber; }

    public String getCondensedFields()
    {
        return "[complex:"+complex+"; building:"+building+"; level:"+level+"]";
    }
}
