package com.cs2063.nfcw.nfcwayfinder;

/**
 * Created by Drew on 20/02/2016.
 */
public class Room implements Comparable
{

    // Room attributes
    String complex = "None";
    String building = "None";
    String level = "None";
    String roomNumber = "None";

    // Empty Constructor
    public Room() {}

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

    @Override
    public int compareTo(Object room) {
        if(this.roomNumber == ((Room)room).roomNumber)
            return 0;
        else
            return Integer.parseInt(this.roomNumber) > Integer.parseInt(((Room)room).roomNumber) ? 1 : -1;
    }
}
