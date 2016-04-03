package com.cs2063.nfcw.nfcwayfinder;

/**
 * Created by Koves on 4/2/2016.
 */
public class Edge
{
    //Edge Characteristics
    private static Edge singleton = null;
    Room firstEnd;
    Room secondEnd;
    boolean visited;

    private Edge(){}

    public Edge(Room first, Room second)
    {
        firstEnd = first;
        secondEnd = second;
        visited = false;
    }

    public Room otherEnd(Room given)
    {
        if(firstEnd.compareTo(given)==0) return secondEnd;
        if(secondEnd.compareTo(given)==0) return firstEnd;
        return null;
    }

    public static Edge terminalEdge()
    {
        if(singleton == null)
        {
            singleton = new Edge();
        }
        return singleton;
    }
}
