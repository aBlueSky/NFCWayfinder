package com.cs2063.nfcw.nfcwayfinder;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Koves on 3/21/2016.
 */
public class NavigationFragment extends Fragment
{
    private static final String TAG = "NavigationFragment";

    private MainActivity mainActivity;
    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "Entered LocationFragment onCreateView");
        view = inflater.inflate(R.layout.navigation_fragment, container, false);

        mainActivity = ((MainActivity) getActivity());
        mainActivity.menuMultipleActions.setVisibility(View.GONE);

        Bundle args = getArguments();
        Room startLocation = mainActivity.firebaseManager.roomMap.get(args.getString
                ("StartLocationID"));
        Room destinationLocation = mainActivity.firebaseManager.roomMap.get(args.getString
                ("DestinationLocationID"));

        new GraphSearchTask().execute(startLocation, destinationLocation);

        TextView txtStartLocation = (TextView) view.findViewById(R.id.start_room_name);
        TextView txtDestinationLocation = (TextView) view.findViewById(R.id.end_room_name);

        txtStartLocation.setText(startLocation.roomName);
        txtDestinationLocation.setText(destinationLocation.roomName);

        return view;
    }

    private Canvas drawMap(int drawable)
    {
        ImageView map = new ImageView(view.getContext());
        map.setAdjustViewBounds(true);
        map.setMinimumHeight(200);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.nav_layout);
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

    private void drawMarker(Room markedRoom, Canvas canvas)
    {
        Paint paint = new Paint();

        Bitmap locationMarker = BitmapFactory.decodeResource(getResources(), R.drawable.loc_icon);

        int bitmapSize = 72;
        canvas.drawBitmap(locationMarker,
                markedRoom.x - (bitmapSize / 2),
                markedRoom.y - (bitmapSize),
                paint);
    }

    private void drawLine(Room r1, Room r2, Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        canvas.drawLine(r1.x, r1.y, r2.x, r2.y, paint);
    }

    private void drawCircle(Room r, Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        canvas.drawCircle(r.x, r.y, 10, paint);
    }

    private class GraphSearchTask extends AsyncTask<Room, Void, ArrayList<Room>> {
        protected ArrayList<Room> doInBackground(Room... params)
        {
            return mainActivity.firebaseManager.findPath(params[0], params[1]);
        }

        protected void onPostExecute(ArrayList<Room> result)
        {
            if(!result.isEmpty())
            {
                Room start = result.get(0);
                int curFloor = Integer.parseInt(start.getLevel());
                int drawable;
                switch (curFloor)
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

                Canvas canvas = drawMap(drawable);
                Canvas startCanvas = canvas;

                for(int i = 0; i < result.size() - 1; i++)
                {
                    Room r1 = result.get(i);
                    Room r2 = result.get(i + 1);
                    if(Integer.parseInt(r1.getLevel()) == Integer.parseInt(r2.getLevel()))
                    {
                        drawLine(r1, r2, canvas);
                    }
                    else
                    {
                        drawCircle(r1, canvas);
                        switch (Integer.parseInt(r2.getLevel()))
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
                        canvas = drawMap(drawable);
                        drawCircle(r2, canvas);
                    }
                }

                drawMarker(start, startCanvas);
                drawMarker(result.get(result.size() - 1), canvas);
            }
        }
    }
}