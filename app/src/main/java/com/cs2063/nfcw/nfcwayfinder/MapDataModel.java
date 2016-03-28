package com.cs2063.nfcw.nfcwayfinder;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
/**
 * Created by Drew on 25/03/2016.
 */
public class MapDataModel {
    private static final String TAG = "MapDataModel";
    private ArrayList<Map> mapArray;
    private static Context context;

    public MapDataModel(Context context)
    {
        Log.d(TAG, "object created.");
        mapArray = new ArrayList<>();
        this.context = context;
    }

    // Getter method for map ArrayList
    public ArrayList<Map> getRooms() { return mapArray; }
}
