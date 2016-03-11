package com.cs2063.nfcw.nfcwayfinder;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;

public class MainActivity extends Activity
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

    private static TextView mText;//text view for debugging NFC tag.//TODO: REMOVE b/c DEBUG
    private static int mCount = 0;//debug output count of NFC tags.//TODO: REMOVE b/c DEBUG

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreate() called.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mText = (TextView) findViewById(R.id.nfcTagText);//TODO: REMOVE b/c DEBUG

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
            throw new RuntimeException("Failure to add intent filter data type.", e);
        }
        intentFiltersArray = new IntentFilter[]{tagIntentFilter,};
        mTechLists = new String[][]{new String[]{NfcF.class.getName()}};//Techlist for all NFC.

        //TODO: Replace once the map function has been added/ move to a separate menu?
        rv = findViewById(R.id.room_list);
        assert rv != null;
        setupRecyclerView((RecyclerView) rv);

        //firebaseManager.sendMapToFirebase("ITC", createImageAsString(R.drawable.itc_level_snip));
    }

    @Override
    protected void onResume()
    {
        Log.d(TAG, "onResume() called.");
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
        Log.d(TAG, "Handling nfc intent.");
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
        Log.d(TAG, "handleNFCPayload() called.");
        if (mText != null) mText.setText("Discovered tag " + ++mCount + ": " + nfcTagContent);
        String[] tokens = nfcTagContent.split("-");
        for (String t: tokens)
        {
            Log.d(TAG, "Token: " + t);
        }
        firebaseManager.getBuilding(tokens[0], rvAdapter, this);
    }

    /**
     * Set up a recycler view and adapter to display the content read by NFC tags and retrieved
     * from Firebase from #handleNFCPayload .
     * @param rv
     */
    private void setupRecyclerView(RecyclerView rv)
    {
        Log.d(TAG, "setupRecyclerView() called.");
        //rvAdapter is what is updated with new room items.
        rv.setAdapter(rvAdapter = new RoomRecyclerViewAdapter(RoomContent.ITEMS));
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    //Convert a drawable image into a string to be added to firebase.
    public String createImageAsString(int drawableIndex)
    {
        Log.d(TAG, "createImageAsString() called.");
        Bitmap bmp =  BitmapFactory.decodeResource(getResources(),
                drawableIndex);//your image
        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bmp.recycle();
        byte[] byteArray = bYtE.toByteArray();
        String imageAsString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.d(TAG, "Map As String = " + imageAsString);
        return  imageAsString;
    }

    //Convert a string pulled from firebase to an image png/bitmap
    public void createStringAsImage(String imageAsString)
    {
        Log.d(TAG, "createStringAsImage() called.");
        byte[] decodedString = Base64.decode(imageAsString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ImageView imageView = (ImageView) findViewById(R.id.mapView);
        imageView.setImageBitmap(decodedByte);
    }
}