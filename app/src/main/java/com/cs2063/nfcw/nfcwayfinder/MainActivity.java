package com.cs2063.nfcw.nfcwayfinder;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity
{
    private static final String TAG = "MainActivity";
    private NfcAdapter nfcAdpt;
    private PendingIntent nfcPendingIntent;
    private IntentFilter[]  intentFiltersArray;
    private String[][] mTechLists;//what is mTechLists for?
    private TextView mText;
    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdpt = NfcAdapter.getDefaultAdapter(this);
        // Create a generic PendingIntent that will be deliver to this activity. The NFC stack
        // will fill in the intent with the details of the discovered tag before delivering to
        // this activity.
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // TODO limit the ndef MIME filter to what we need.
        IntentFilter tagIntentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            tagIntentFilter.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        intentFiltersArray = new IntentFilter[] {
                tagIntentFilter,
        };

        // Setup a tech list for all NfcF tags
        mTechLists = new String[][] { new String[] { NfcF.class.getName() } };
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume() called.");
        super.onResume();
        if (nfcAdpt != null) nfcAdpt.enableForegroundDispatch(this, nfcPendingIntent, intentFiltersArray,
                mTechLists);
    }

    private void handleIntent(Intent intent)
    {
        Log.d(TAG, "Handling nfc intent.");
        //handle the intent
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdpt != null) nfcAdpt.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent)
    {
        Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);
        mText.setText("Discovered tag " + ++mCount + " with intent: " + intent);
    }
}