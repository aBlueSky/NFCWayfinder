package com.cs2063.nfcw.nfcwayfinder;

import android.app.Activity;
=======
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
>>>>>>> cb08f6c7f1a010f477640750fa3b78b0e68b93ee
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity
{
    private static final String TAG = "MainActivity";

    private Button btnDestination;
    private Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate() called.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

<<<<<<< HEAD
        //Handle buttons and on click events.
        btnDestination = (Button) findViewById(R.id.btnDestination);
        if(btnDestination != null)
        {
            btnDestination.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "onClick() called.");
                }
            });
        }
=======
        Intent nfcIntent = new Intent(this, getClass());

        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        nfcPendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
        IntentFilter tagIntentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try
        {
            tagIntentFilter.addDataType("text/plain");
            intentFiltersArray = new IntentFilter[]{tagIntentFilter};
        }
        catch (Throwable t) { t.printStackTrace(); }
>>>>>>> cb08f6c7f1a010f477640750fa3b78b0e68b93ee
    }
}
