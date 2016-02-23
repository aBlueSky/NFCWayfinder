package com.cs2063.nfcw.nfcwayfinder;

import android.content.Context;
import android.util.Log;

//import com.firebase.client.Firebase;

/**
 * Created by Koves on 2/22/2016.
 */
public class FirebaseManager
{
    private final static String TAG = "FirebaseManager";
    //private static Firebase firebase = new Firebase("https://nfcwayfinder.firebaseio.com/");

    public static void setContext(Context context)
    {
        Log.d(TAG, "Firebase context set.");
        //Firebase.setAndroidContext(context);
    }
}
