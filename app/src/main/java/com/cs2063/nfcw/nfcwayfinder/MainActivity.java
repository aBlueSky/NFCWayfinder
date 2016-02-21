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
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity
{
    private static final String TAG = "MainActivity";
    public static final String MIME_TEXT_PLAIN = "text/plain";
    private NfcAdapter nfcAdpt;
    private PendingIntent nfcPendingIntent;
    private IntentFilter[]  intentFiltersArray;
    private String[][] mTechLists;//what is mTechLists for?
    private static TextView mText;
    private static TextView mJSONText;
    private static TextView mJSONDecodedText;
    private static int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate() called.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mText = (TextView) findViewById(R.id.nfcTagText);
        mJSONText = (TextView) findViewById(R.id.jsonText);
        mJSONDecodedText = (TextView) findViewById(R.id.jsonDecodedText);

        nfcAdpt = NfcAdapter.getDefaultAdapter(this);
        // Create a generic PendingIntent that will be deliver to this activity. The NFC stack
        // will fill in the intent with the details of the discovered tag before delivering to
        // this activity.
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // TODO limit the ndef MIME filter to what we need.
        IntentFilter tagIntentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            tagIntentFilter.addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        intentFiltersArray = new IntentFilter[] {
                tagIntentFilter,
        };

        // Setup a tech list for all NfcF tags
        mTechLists = new String[][] { new String[] { NfcF.class.getName() } };

        //debug datamodel
        DataModel dataModel = new DataModel(this.getApplicationContext());
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume() called.");
        super.onResume();
        if (nfcAdpt != null) nfcAdpt.enableForegroundDispatch(this, nfcPendingIntent, intentFiltersArray,
                mTechLists);
    }

    // This method is called when an NFC tag has been registered by foreground dispatch, send to
    // process upon successful tags.
    private void handleIntent(Intent intent)
    {
        Log.d(TAG, "Handling nfc intent.");
        //handle the intent
        if(intent == null) return; //Not actually an intent, dismiss.

        String action = intent.getAction();
        Log.d(TAG, "Action: " + action);
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
        {
            Log.d(TAG, "Action-NDEF-Discovered.");
            String type = intent.getType();
            if(MIME_TEXT_PLAIN.equals(type))
            {
                Log.d(TAG, "MIME type: " + MIME_TEXT_PLAIN);
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                Log.d(TAG, "NDEF tag able to be executed.");
                new NdefReaderTask().execute(tag);
            }
            else
            {
                Log.d(TAG, "Wrong MIME type.");
            }
        }
        else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action))
        {
            Log.d(TAG, "Action-TECH-Discovered.");
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techListStrings = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech: techListStrings)
            {
                if (searchedTech.equals(tech))
                {
                    Log.d(TAG, "tech tag able to be executed.");
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
        else
        {
            Log.d(TAG, "Action not handled.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdpt != null) nfcAdpt.disableForegroundDispatch(this);
    }

    /* Where new intents are registered and able to be sent to be handled. */
    @Override
    public void onNewIntent(Intent intent)
    {
        Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);
        handleIntent(intent);
    }
    public static void setTextPreview(String str)
    {
        if(mText != null) mText.setText("Discovered tag " + ++ mCount + " with intent: " + str);
    }
    public static void setmJSONText(String str)
    {
        if(mJSONText != null) mJSONText.setText(str);
    }
    public static void setmJSONDecodedText(String str)
    {
        if(mJSONDecodedText != null) mJSONDecodedText.setText(str);
    }
}