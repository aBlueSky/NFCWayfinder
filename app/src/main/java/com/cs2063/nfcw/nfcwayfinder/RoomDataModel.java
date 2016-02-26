package com.cs2063.nfcw.nfcwayfinder;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Drew on 20/02/2016.
 */
public class RoomDataModel
{
    private static final String TAG = "RoomDataModel";
    private ArrayList<Room> roomArray;
    private static Context context;

    public RoomDataModel(Context context)
    {
        Log.d(TAG, "object created.");
        roomArray = new ArrayList<>();
        this.context = context;
    }

    // Getter method for complexes ArrayList
    public ArrayList<Room> getRooms() { return roomArray; }
}
