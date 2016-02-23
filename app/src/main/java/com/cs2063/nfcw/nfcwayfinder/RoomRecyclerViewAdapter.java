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
//TODO: Move RoomContent to here, so that notifyDataSetChanged() and notifyItemRemoved(position)
// notifyItemRangeChanged(position, list.size()) can be used for dynamic list.
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
        mValues.clear();
        mValues.addAll(rooms);
        notifyDataSetChanged();
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
        holder.mIdView.setText(mValues.get(position).getRoomNumber());
        holder.mCondensedFieldView.setText(mValues.get(position).getCondensedFields());
        holder.mComplexView.setText(mValues.get(position).getComplex());

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
        public final TextView mIdView;
        public final TextView mCondensedFieldView;
        public final TextView mComplexView;
        public Room mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mCondensedFieldView = (TextView) view.findViewById(R.id.content);
            mComplexView = (TextView) view.findViewById(R.id.complex);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCondensedFieldView.getText() + "'";
        }
    }
}
