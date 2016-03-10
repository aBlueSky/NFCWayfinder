package com.cs2063.nfcw.nfcwayfinder;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Koves on 2/21/2016.
 */
public class RoomContent
{
    private static final String TAG = "RoomContent";
    public static final List<Room> ITEMS = new ArrayList<Room>();

    //TODO: Remove this debug list that preloads the adapter to have items.
    static {
        for(int i = 0; i < 3; i++)
        {
            Room r = new Room();
            r.roomNumber = "" + i;
            ITEMS.add(r);
        }
    }
}
