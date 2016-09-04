package com.terminatingcode.android.migrainetree.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.terminatingcode.android.migrainetree.R;
import com.terminatingcode.android.migrainetree.view.RecordsFragment.OnListFragmentInteractionListener;
import com.terminatingcode.android.migrainetree.model.MigraineRecordItems.RecordItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link RecordItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<RecordItem> mValues;
    private final FragmentListener mListener;

    public MyItemRecyclerViewAdapter(List<RecordItem> items, FragmentListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).content);
        holder.mDetailsView.setText(mValues.get(position).details);

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListDeleteItem(holder.mItem);
                    mValues.remove(holder.mItem);
                    notifyDataSetChanged();
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
        public final TextView mContentView;
        public final TextView mDetailsView;
        public final Button mDeleteButton;
        public RecordItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            mDetailsView = (TextView) view.findViewById(R.id.description);
            mDeleteButton = (Button) view.findViewById(R.id.deleteButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
