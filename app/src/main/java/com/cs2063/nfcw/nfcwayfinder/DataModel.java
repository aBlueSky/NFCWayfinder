package com.cs2063.nfcw.nfcwayfinder;

import android.content.Context;

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

    private ArrayList<Complex> complexesArray = new ArrayList<>();
    private Context context;

    // Initializer to read our data source (JSON file) into an array of complex objects
    public DataModel(Context context) {

        this.context = context;

        getJSON();
    }

    private void getJSON() {
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();

        try {
            br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.context.getString(R.string.json).getBytes())));
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
        String jsonString = sb.toString();

        // Finally, parse resultant JSON
        try {

            // Create a JSON Object from file contents String
            JSONObject jsonObject = new JSONObject(jsonString);

            // Create a JSON Array from the JSON Object
            // This array is the "complexes" array
            JSONArray jsonArray = jsonObject.getJSONArray("complexes");

            for (int i=0; i < jsonArray.length(); i++) {

                // Create a JSON Object from individual JSON Array element
                JSONObject elementObject = jsonArray.getJSONObject(i);

                // Create a Complex Object to contain JSON Object data
                Complex complex = new Complex();

                // Get data from individual JSON Object and store to Complex Object
                complex.complex = elementObject.getString("complex");
                complex.building = elementObject.getString("building");
                complex.level = elementObject.getString("level");
                complex.roomNumber = elementObject.getString("roomNumber");

                // Add new Complex to complexes ArrayList
                complexesArray.add(complex);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    // Getter method for complexes ArrayList
    public ArrayList<Complex> getComplexes() { return complexesArray; }
}
