package com.cs2063.nfcw.nfcwayfinder;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by Koves on 2/21/2016.
 */
public class NdefReaderTask extends AsyncTask<Tag, Void, String>
{
    public static final String TAG = "NdefReaderTask";
    MainActivity mainActivity;

    public NdefReaderTask(MainActivity activity)
    {
        super();
        mainActivity = activity;
    }

    @Override
    protected String doInBackground(Tag... params)
    {
        Tag tag = params[0];

        Ndef ndef = Ndef.get(tag);
        if(ndef == null) return null; //NDEF not supported from this tag.

        NdefMessage ndefMessage = ndef.getCachedNdefMessage();

        NdefRecord[] records = ndefMessage.getRecords();
        for(NdefRecord record: records)
        {
            if(record.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(record.getType(),
                    NdefRecord.RTD_TEXT))
            {
                try
                {
                    return readText(record);
                }
                catch (UnsupportedEncodingException e)
                {
                    Log.e(TAG, "Unsupported encoding", e);
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result)
    {
        if(result != null)
        {
            Log.d(TAG, "Read content: " + result);
            mainActivity.handleNFCPayload(result);
        }
    }

    private String readText(NdefRecord record) throws UnsupportedEncodingException
    {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */
        //Log.d(TAG, "readText()");
        String eight = "UTF-8";
        String sixteen = "UTF-16";

        byte[] payload = record.getPayload();

        //Get Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? eight : sixteen;

        //Get Language Code
        int languageCodeLength = payload[0] & 0063;

        //Get Text
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength -
                1, textEncoding);
    }
}
