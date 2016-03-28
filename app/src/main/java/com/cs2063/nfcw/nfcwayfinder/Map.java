package com.cs2063.nfcw.nfcwayfinder;

/**
 * Created by Drew on 25/03/2016.
 */
public class Map {

    // Map attributes
    String building = "";
    String floor = "";
    String room = "";

    // Empty Constructor
    public Map() {

    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getBuilding() {
        return building;
    }

    public String getFloor() {
        return floor;
    }

    public String getRoom() {
        return room;
    }
}
