package com.cs2063.nfcw.nfcwayfinder;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;

import android.util.Base64;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.Firebase;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.lang.reflect.Array;
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

    //Assorted Managers
    public FirebaseManager firebaseManager;

    //Main Activity controls
    public Toolbar myToolbar;
    public boolean accessibilityFlag;
    public FloatingActionsMenu menuMultipleActions;

    //TODO: testing purposes, kill later.
    //private FloatingActionButton actionA;
    //private View actionB;
    //private FloatingActionButton actionC;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreate() called.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar Start ----------------------------------------------------------------------------

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(R.string.app_name);

        //Floating Action Button Start--------------------------------------------------------------

        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        assert menuMultipleActions != null;

        //Floating Action Button End----------------------------------------------------------------

        //mText = (TextView) findViewById(R.id.nfcTagText);//TODO remove this preview of the tag.

        Firebase.setAndroidContext(getApplicationContext());
        firebaseManager = new FirebaseManager(this);//Initialise Firebase Manager.

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
            throw new RuntimeException("Failure to add intent filter data type.", e);
        }
        intentFiltersArray = new IntentFilter[]{tagIntentFilter,};
        mTechLists = new String[][]{new String[]{NfcF.class.getName()}};//Techlist for all NFC.

        //firebaseManager.sendMapToFirebase("ITC", createImageAsString(R.drawable.itc_level_snip));
        DefaultFragment f = new DefaultFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.add(R.id.fragment_location, f);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    protected void onResume()
    {
        //Log.d(TAG, "onResume() called.");
        super.onResume();
        //re-enable foreground dispatch since app is now running.
        if (nfcAdpt != null)
            nfcAdpt.enableForegroundDispatch(this, nfcPendingIntent, intentFiltersArray,
                    mTechLists);
    }

    // This method is called when an NFC tag has been registered by foreground dispatch, send to
    // process upon successful tags.
    private void handleIntent(Intent intent)
    {
        Log.d(TAG, "Handling NFC Intent");
        if (intent == null) return;

        String action = intent.getAction();
        Log.d(TAG, "Action: " + action);
        //We're only handling NDEF.
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
        //disable foreground dispatch since app is in background.
        if (nfcAdpt != null) nfcAdpt.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent)
    {
        Log.d("Foreground dispatch", "Discovered tag with intent: " + intent);
        handleIntent(intent);
    }

    /**
     * After the NFC Adapter is sent the NFCPendingIntent by #handleIntent the text of the NFC
     * tag is handled by this.
     * @param nfcTagContent the string that was contained on the scanned by NFC Adapter
     */
    public void handleNFCPayload(String nfcTagContent)
    {
        //Log.d(TAG, "handleNFCPayload() called.");
        //if (mText != null) mText.setText("Discovered tag " + ++mCount + ": " + nfcTagContent);
        String[] tokens = nfcTagContent.split("-");
        //for (String t: tokens) {
        //    Log.d(TAG, "Token: " + t);
        //}
        firebaseManager.getBuilding(tokens[0], tokens[1], tokens[2], this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //case R.id.action_maps:

            //    return true;

            case R.id.action_access:
                MenuItem access = myToolbar.getMenu().getItem(0);
                if(access.isChecked()) { access.setChecked(false); }
                else { access.setChecked(true); }
                accessibilityFlag = access.isChecked();
                return true;

            case R.id.action_about:
                AboutFragment f = new AboutFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.replace(R.id.fragment_location, f);
                ft.addToBackStack(null);
                ft.commit();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
	}

    public void goToLocationFragment(Room taggedRoom)
    {
        if(taggedRoom == null) return;

        LocationFragment f = new LocationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("RoomNum", taggedRoom.getRoomNumber());
        f.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.fragment_location, f);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed()
    {
        FragmentManager fm = getFragmentManager();
        if(fm.getBackStackEntryCount() > 1) { fm.popBackStack(); }
        else { super.onBackPressed(); }
    }
}