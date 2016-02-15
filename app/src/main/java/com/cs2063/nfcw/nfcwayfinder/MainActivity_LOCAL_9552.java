package com.cs2063.nfcw.nfcwayfinder;

import android.app.Activity;
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
    }
}
