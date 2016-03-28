package com.cs2063.nfcw.nfcwayfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
/**
 * Created by Drew on 25/03/2016.
 */
public class MapDetailActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(MapDetailFragment.BUILDING,
                    getIntent().getStringExtra(MapDetailFragment.BUILDING));
            arguments.putString(MapDetailFragment.FLOOR,
                    getIntent().getStringExtra(MapDetailFragment.FLOOR));
            arguments.putString(MapDetailFragment.ROOM,
                    getIntent().getStringExtra(MapDetailFragment.ROOM));
            MapDetailFragment fragment = new MapDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.map_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MapListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
