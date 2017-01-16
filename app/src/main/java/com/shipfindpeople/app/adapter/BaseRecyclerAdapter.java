package com.shipfindpeople.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.shipfindpeople.app.R;

import java.util.ArrayList;

/**
 * Created by sonnd on 10/8/2016.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROGRESS = 0;
    private boolean mUseDefaultClickListener = false;

    public void setUseDefaultClickListener(boolean useDefault){
        this.mUseDefaultClickListener = useDefault;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            vh = getCustomViewHolder(parent);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_bar_layout, parent, false);

            vh = new ProgressViewHolder(itemView);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final T element = getArrayList().get(position);
        bindMyViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return getTotal();
    }

    @Override
    public int getItemViewType(int position) {
        return getArrayList().get(position) != null ? VIEW_ITEM : VIEW_PROGRESS;
    }

    public abstract void bindMyViewHolder(RecyclerView.ViewHolder holder, int position);
    public abstract int getTotal();
    public abstract ArrayList<T> getArrayList();
    public abstract void setArrayList(ArrayList<T> data);
    public abstract RecyclerView.ViewHolder getCustomViewHolder(ViewGroup parent);

    protected static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        }
    }
}
