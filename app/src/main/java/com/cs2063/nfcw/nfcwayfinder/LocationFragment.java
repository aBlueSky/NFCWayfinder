package com.cs2063.nfcw.nfcwayfinder;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    private static final String TAG = "LocationFragment";

    private MainActivity mainActivity;
    private View view;

    FloatingActionButton fabNavigate;
    FloatingActionButton fabViewMap;

    private Room currentLocation;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "Entered LocationFragment onCreateView");
        view = inflater.inflate(R.layout.location_fragment, container, false);
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
            //imageView.setImageResource(R.drawable.first_floor);
            imageView.setVisibility(View.GONE);
            TextView lblRoomName = (TextView)view.findViewById(R.id.room_name);
            lblRoomName.setText("Room invalid.");
        }
        else
        {
            int level = Integer.parseInt(currentLocation.getLevel());
            int drawable;
            TextView lblRoomName = (TextView)view.findViewById(R.id.room_name);
            TextView lblRoomInfo = (TextView)view.findViewById(R.id.room_info);
            switch (level)
            {
                case 1:
                    drawable = R.drawable.first_floor;
                    break;
                case 2:
                    drawable = R.drawable.second_floor;
                    break;
                default:
                    drawable = R.drawable.first_floor;
                    break;
            }
            lblRoomName.setText(""+currentLocation.roomName);
            lblRoomInfo.setText(""+currentLocation.info);

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

            Bitmap locationMarker = BitmapFactory.decodeResource(getResources(), R.drawable.loc_icon);

            Canvas canvas = new Canvas(mutableBitmap);

            int bitmapSize = 72;
            canvas.drawBitmap(locationMarker,
                    currentLocation.x - (bitmapSize / 2),
                    currentLocation.y - (bitmapSize),
                    paint);
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

                Bundle bundle = new Bundle();
                bundle.putString("StartLocationID", currentLocation.roomNumber);
                f.setArguments(bundle);

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

                MapFragment f = new MapFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.replace(R.id.fragment_location, f);
                ft.addToBackStack(null);
                ft.commit();
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
}