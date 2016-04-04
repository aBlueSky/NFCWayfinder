package com.cs2063.nfcw.nfcwayfinder;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by beaul on 2016-04-04.
 */
public class MapFragment extends Fragment
{
    private static final String TAG = "MapFragment";

    private MainActivity mainActivity;
    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "Entered MapFragment onCreateView");

        view = inflater.inflate(R.layout.map_fragment, container, false);

        mainActivity = ((MainActivity) getActivity());
        mainActivity.menuMultipleActions.setVisibility(View.GONE);

        drawMap(R.drawable.first_floor);
        drawMap(R.drawable.second_floor);

        return view;
    }

    private Canvas drawMap(int drawable)
    {
        ImageView map = new ImageView(view.getContext());
        map.setAdjustViewBounds(true);
        map.setMinimumHeight(200);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.map_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        lp.setMargins(0, 10, 0, 0);
        ll.addView(map, lp);
        map.setAdjustViewBounds(true);

        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawable, myOptions);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);

        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        map.setAdjustViewBounds(true);
        map.setImageBitmap(mutableBitmap);

        return new Canvas(mutableBitmap);
    }
}