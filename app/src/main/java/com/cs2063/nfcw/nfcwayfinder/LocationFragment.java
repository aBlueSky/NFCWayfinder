package com.cs2063.nfcw.nfcwayfinder;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

/**
 * Created by Koves on 3/21/2016.
 */
public class LocationFragment extends Fragment
{
    MainActivity mainActivity;

    FloatingActionButton fabNavigate;
    FloatingActionButton fabViewMap;

    private static final String TAG = "LocationFragment";

    private Room currentLocation;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "Entered LocationFragment onCreateView");
        View view = inflater.inflate(R.layout.location_fragment, container, false);
        Bundle bundle = getArguments();
        String roomNumber = bundle.getString("RoomNum");

        mainActivity = ((MainActivity) getActivity());
        mainActivity.menuMultipleActions.setVisibility(View.VISIBLE);

        currentLocation = mainActivity.firebaseManager.roomMap.containsKey(roomNumber)?
                          mainActivity.firebaseManager.roomMap.get(roomNumber):
                          null;

        ImageView imageView = (ImageView) view.findViewById(R.id.mapView);
        if (currentLocation == null)
        {
            imageView.setImageResource(R.drawable.first_floor3);
            TextView lblRoomName = (TextView)view.findViewById(R.id.room_name);
            lblRoomName.setText("Room invalid.");
        }
        else
        {
            int level = Integer.parseInt(currentLocation.getLevel());
            int drawable;
            TextView lblRoomName = (TextView)view.findViewById(R.id.room_name);
            switch (level)
            {
                case 1:
                    drawable = R.drawable.first_floor3;
                    break;
                case 2:
                    drawable = R.drawable.second_floor;
                    break;
                default:
                    drawable = R.drawable.first_floor3;
                    break;
            }
            lblRoomName.setText(""+currentLocation.roomName);

            BitmapFactory.Options myOptions = new BitmapFactory.Options();
            myOptions.inDither = true;
            myOptions.inScaled = false;
            myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawable, myOptions);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLUE);


            Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
            Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);


            Canvas canvas = new Canvas(mutableBitmap);
            canvas.drawCircle(currentLocation.x, currentLocation.y, 25, paint);

            imageView.setAdjustViewBounds(true);
            imageView.setImageBitmap(mutableBitmap);
        }

        fabNavigate = new FloatingActionButton(mainActivity.getBaseContext());
        fabNavigate.setTitle("Navigate");
        fabNavigate.setIcon(R.drawable.compass_icon);
        fabNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.menuMultipleActions.collapse();

                RoomPickerFragment f = new RoomPickerFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.replace(R.id.fragment_location, f);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        fabViewMap = new FloatingActionButton(mainActivity.getBaseContext());
        fabViewMap.setTitle("View Full Map");
        fabViewMap.setIcon(R.drawable.map_icon);
        fabViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.menuMultipleActions.collapse();

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        mainActivity.menuMultipleActions.addButton(fabNavigate);
        mainActivity.menuMultipleActions.addButton(fabViewMap);
        super.onResume();
    }

    @Override
    public void onPause() {
        mainActivity.menuMultipleActions.removeButton(fabNavigate);
        mainActivity.menuMultipleActions.removeButton(fabViewMap);
        super.onPause();
    }
/*
    public class DrawView extends View
    {
        private final Paint mPainter = new Paint();
        private Room room;
        public DrawView(Context context, Room r)
        {
            super(context);
            room = r;
        }

        @Override
        protected synchronized void onDraw(Canvas canvas) {

            // TODO - save the canvas
            canvas.save();

            // TODO - draw the bitmap at it's new location
            //canvas.drawBitmap(mScaledBitmap, mXPos, mYPos, mPainter);

            // TODO - restore the canvas
            canvas.restore();
        }
    }*/
}