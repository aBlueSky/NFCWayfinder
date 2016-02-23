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
    public static final Map<String, Room> ITEM_MAP =
            new HashMap<String, Room>();

    private static final int COUNT = 3;

    static {
        for(int i = 0; i < COUNT; i++)
        {
            Room r = new Room();
            r.roomNumber = "" + i;
            ITEMS.add(r);
            ITEM_MAP.put(r.roomNumber, r);
        }
    }
}
