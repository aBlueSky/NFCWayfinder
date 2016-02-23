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
public class DataModel {
    private static final String TAG = "DataModel";
    private static ArrayList<Room> roomArray = new ArrayList<>();

    public static void getJSON(Context context) {
        Log.d(TAG, "getJSON() called.");
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();

        try {
            br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(context
                    .getString(R.string.json2).getBytes())));
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        processJSON(sb);
    }

    private static void processJSON(StringBuffer sb) {
        Log.d(TAG, "processJSON() called.");
        String jsonString = sb.toString();
        Log.d(TAG, "JSON: "+jsonString);
        //MainActivity.setmJSONText(jsonString);
        try {

            // Create a JSON Object from file contents String
            JSONObject jsonObject = new JSONObject(jsonString);

            // Create a JSON Array from the JSON Object
            // This array is the "complexes" array
            JSONArray jsonArray = jsonObject.getJSONArray("complexes");

            //clear previous entries in ArrayList.
            roomArray.clear();
            for (int i=0; i < jsonArray.length(); i++) {

                // Create a JSON Object from individual JSON Array element
                JSONObject elementObject = jsonArray.getJSONObject(i);

                // Create a Room Object to contain JSON Object data
                Room room = new Room();

                // Get data from individual JSON Object and store to Room Object
                room.complex = elementObject.getString("complex");
                room.building = elementObject.getString("building");
                room.level = elementObject.getString("level");
                room.roomNumber = elementObject.getString("roomNumber");

                // Add new Room to ArrayList
                roomArray.add(room);
                Log.d(TAG, "[complex:"+ room.complex+"; building:"+ room
                        .building+"; level:"+ room.level+"; room:"+ room.roomNumber+"]");
            }

            //Now that all records were added to temp list, sort.
            Collections.sort(roomArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Getter method for complexes ArrayList
    public static ArrayList<Room> getRooms() { return roomArray; }
}
