package com.cs2063.nfcw.nfcwayfinder;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Drew on 25/03/2016.
 */
public class MapListActivity extends AppCompatActivity{
    private boolean mTwoPane;
    private MapDataModel dataModel;
    private List<Map> mMapList;
    private int downloadTime = 4;      // Download time simulation
    private Button mBgButton;
    private final String TAG = "MapListActivity";

    public RelativeLayout mFrame;
    public GestureDetector mGestureDetector;
    public ArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_main);

        mFrame = (RelativeLayout) findViewById(R.id.frameLayout);
        ArrayList list = new ArrayList();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, list);

        mBgButton = (Button) findViewById(R.id.btnDestination);

        if(findViewById(R.id.map_detail_container) != null) {
            mTwoPane = true;
            MapDetailFragment mapDetailFragment = (MapDetailFragment) fragmentManager.findFragmentByTag("Detail");
            if (mapDetailFragment == null) {
                mapDetailFragment = new MapDetailFragment();
                Bundle args = new Bundle();
                mapDetailFragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.map_detail_container, mapDetailFragment, "Detail").commit();
            }
        }

        mBgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "I'm working!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        if (findViewById(R.id.map_detail_container) != null) {
            mTwoPane = true;
        }

        setupGestureDetector();
    }

    private void setupGestureDetector() {

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                final ListView list = (ListView) findViewById(R.id.room_list);
                for (int i = 0; i < list.getCount(); i++) {
                    list.setItemChecked(i, true);
                }
                SparseBooleanArray checkedItemPositions = list.getCheckedItemPositions();
                int itemCount = list.getCount();

                for(int i=itemCount-1; i >= 0; i--){
                    if(checkedItemPositions.get(i)){
                        adapter.remove(list.getItemAtPosition(i));
                    }
                }
                checkedItemPositions.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(mMapList));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Map> mValues;

        public SimpleItemRecyclerViewAdapter(List<Map> data) {
            mValues = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.map_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mMap = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).building);

            Context context = getApplicationContext();
            ContextCompat.getDrawable(context, R.drawable.ludlow_hall_level1);
            ContextCompat.getDrawable(context, R.drawable.ludlow_hall_level2);
            ContextCompat.getDrawable(context, R.drawable.ludlow_hall_level3);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String building = holder.mMap.building;
                    String floor = holder.mMap.floor;
                    String room = holder.mMap.room;
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MapDetailFragment.BUILDING, building);
                        arguments.putString(MapDetailFragment.FLOOR, floor);
                        arguments.putString(MapDetailFragment.ROOM, room);
                        MapDetailFragment fragment = new MapDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.map_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MapDetailActivity.class);
                        intent.putExtra(MapDetailFragment.BUILDING, building);
                        intent.putExtra(MapDetailFragment.FLOOR, floor);
                        intent.putExtra(MapDetailFragment.ROOM, room);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Map mMap;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
