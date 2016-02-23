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
            MainActivity.handleNFCPayload(result);
            //TODO: Set location based on this.
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
        Log.d(TAG, "readText(record)");
        byte[] payload = record.getPayload();

        //Get Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        //Get Language Code
        int languageCodeLength = payload[0] & 0063;

        //Get Text
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength -
                1, textEncoding);
    }
}
