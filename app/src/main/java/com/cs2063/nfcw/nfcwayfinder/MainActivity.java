package com.cs2063.nfcw.nfcwayfinder;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private static final String MIME_TEXT_PLAIN = "text/plain";

    //NFC Variables
    private NfcAdapter nfcAdpt;
    private PendingIntent nfcPendingIntent;
    private IntentFilter[] intentFiltersArray;
    private String[][] mTechLists;//what is mTechLists for?

    //RecyclerView Variables
    private View rv;
    private RoomRecyclerViewAdapter rvAdapter;

    //Assorted Managers
    private FirebaseManager firebaseManager;
    private RoomDataModel roomDataModelManager;

    //TODO: Remove eventually.
    // Debug text views for preview of JSON and NFC operations.
    private static TextView mText;//text view for debugging NFC tag.
    private static int mCount = 0;//debug output count of NFC tags.

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreate() called.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mText = (TextView) findViewById(R.id.nfcTagText);//TODO remove this preview of the tag.

        roomDataModelManager = new RoomDataModel(getApplicationContext());//Initialise RoomDataModel
        Firebase.setAndroidContext(getApplicationContext());
        firebaseManager = new FirebaseManager();//Initialise Firebase Manager.

        nfcAdpt = NfcAdapter.getDefaultAdapter(this);
        // Create a generic PendingIntent that will be deliver to this activity.
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagIntentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try
        {
            tagIntentFilter.addDataType(MIME_TEXT_PLAIN);
        }
        catch (IntentFilter.MalformedMimeTypeException e)
        {
            throw new RuntimeException("fail", e);
        }
        intentFiltersArray = new IntentFilter[]{tagIntentFilter,};
        mTechLists = new String[][]{new String[]{NfcF.class.getName()}};//Techlist for all NFC.

        //Setup debug recyclerview
        rv = findViewById(R.id.room_list);
        assert rv != null;
        setupRecyclerView((RecyclerView) rv);
    }

    @Override
    protected void onResume()
    {
        Log.d(TAG, "onResume() called.");
        super.onResume();
        if (nfcAdpt != null)
            nfcAdpt.enableForegroundDispatch(this, nfcPendingIntent, intentFiltersArray,
                    mTechLists);
    }

    // This method is called when an NFC tag has been registered by foreground dispatch, send to
    // process upon successful tags.
    private void handleIntent(Intent intent)
    {
        Log.d(TAG, "Handling nfc intent.");
        if (intent == null) return;

        String action = intent.getAction();
        Log.d(TAG, "Action: " + action);
        //We're only handling NDEF at the moment.
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
        {
            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type))
            {
                Log.d(TAG, "MIME type: " + MIME_TEXT_PLAIN);
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                Log.d(TAG, "NDEF tag able to be executed.");
                new NdefReaderTask(this).execute(tag);
            }
            else
                Log.d(TAG, "Wrong MIME type.");
        }
        else
            Log.d(TAG, "Action not handled.");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (nfcAdpt != null) nfcAdpt.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent)
    {
        Log.d("Foreground dispatch", "Discovered tag with intent: " + intent);
        handleIntent(intent);
    }

    public void handleNFCPayload(String nfcTagContent)
    {
        Log.d(TAG, "handleNFCPayload() called.");
        if (mText != null) mText.setText("Discovered tag " + ++mCount + ": " + nfcTagContent);
        String[] tokens = nfcTagContent.split("-");
        for (String t: tokens)
        {
            Log.d(TAG, "Token: " + t);
        }
        firebaseManager.getBuilding(tokens[0], rvAdapter);
    }

    private void setupRecyclerView(RecyclerView rv)
    {
        Log.d(TAG, "setupRecyclerView() called.");
        rv.setAdapter(rvAdapter = new RoomRecyclerViewAdapter(RoomContent.ITEMS));
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}