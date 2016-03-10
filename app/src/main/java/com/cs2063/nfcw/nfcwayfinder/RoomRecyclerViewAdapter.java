package com.cs2063.nfcw.nfcwayfinder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Koves on 2/21/2016.
 */
public class RoomRecyclerViewAdapter extends
        RecyclerView.Adapter<RoomRecyclerViewAdapter.ViewHolder>
{
    private static final String TAG = "RoomRecyclerViewAdapter";
    private final List<Room> mValues;
    public RoomRecyclerViewAdapter(List<Room> items)
    {
        mValues = items;
    }

    public void swap(List<Room> rooms)
    {
        Log.d(TAG, "swap() called.");
        mValues.clear();
        mValues.addAll(rooms);
        notifyDataSetChanged();//notify anything using the adapter to update their views.
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mRoomNumberView.setText(mValues.get(position).getRoomNumber());
        holder.mRoomLevelView.setText(mValues.get(position).getLevel());
        holder.mRoomBuildingView.setText(mValues.get(position).getBuilding());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked!");
                /* TODO: Landscape support with tablets.
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(CourseDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                    CourseDetailFragment fragment = new CourseDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.course_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, CourseDetailActivity.class);
                    intent.putExtra(CourseDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                    context.startActivity(intent);
                }
                */
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mRoomNumberView;
        public final TextView mRoomLevelView;
        public final TextView mRoomBuildingView;
        public Room mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mRoomNumberView = (TextView) view.findViewById(R.id.room_number);
            mRoomLevelView = (TextView) view.findViewById(R.id.room_level);
            mRoomBuildingView = (TextView) view.findViewById(R.id.room_building);
        }

        //TODO: unsure if anything other that super.toString() is needed.
        @Override
        public String toString() {
            return super.toString() + " '" + mRoomNumberView.getText() + mRoomLevelView.getText() + "'";
        }
    }
}
