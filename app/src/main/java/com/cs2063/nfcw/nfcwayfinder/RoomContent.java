package com.cs2063.nfcw.nfcwayfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Koves on 2/21/2016.
 */
public class RoomContent
{
    public static final List<Room> ITEMS = new ArrayList<Room>();

    public static final Map<String, Room> ITEM_MAP =
            new HashMap<String, Room>();

    private static final int COUNT = 25;

    public static void addItem(Room item)
    {
        ITEMS.add(item);
        ITEM_MAP.put(item.roomNumber, item);
    }

    public static void clearItems()
    {
        ITEMS.removeAll(ITEMS);
        ITEM_MAP.clear();
    }

    private static String makeDetails(int position)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
}
