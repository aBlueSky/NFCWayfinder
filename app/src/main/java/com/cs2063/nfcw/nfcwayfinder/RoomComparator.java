package com.cs2063.nfcw.nfcwayfinder;

import java.util.Comparator;

/**
 * Created by Koves on 4/3/2016.
 */
public class RoomComparator implements Comparator<FirebaseManager.WeightedRoom>
{
    //For distance comparison.
    public int compare(FirebaseManager.WeightedRoom a, FirebaseManager.WeightedRoom b)
    {
        if(a.distanceTravelled == b.distanceTravelled)
            return 0;
        else
            return a.distanceTravelled > b.distanceTravelled? 1 : -1;
    }
}
