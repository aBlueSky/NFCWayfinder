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

/**
 * Created by Drew on 20/02/2016.
 */
public class DataModel {
    private static final String TAG = "DataModel";
    private ArrayList<Room> roomArray = new ArrayList<>();
    private Context context;

    // Initializer to read our data source (JSON file) into an array of complex objects
    public DataModel(Context context) {

        this.context = context;

        //TODO: Without this line, recyclerview will not load, need to remove/make static?
        getJSON();
    }

    private void getJSON() {
        Log.d(TAG, "getJSON() called.");
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();

        try {
            br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.context
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

    private void processJSON(StringBuffer sb) {
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

                // Add new Room to complexes ArrayList
                roomArray.add(room);
                RoomContent.addItem(room);
                Log.d(TAG, "[complex:"+ room.complex+"; building:"+ room
                        .building+"; level:"+ room.level+"; room:"+ room.roomNumber+"]");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Getter method for complexes ArrayList
    public ArrayList<Room> getComplexes() { return roomArray; }
}
