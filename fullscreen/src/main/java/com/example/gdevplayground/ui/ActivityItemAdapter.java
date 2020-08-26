package com.example.gdevplayground.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gdevplayground.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityItemAdapter extends RecyclerView.Adapter<ActivityItemAdapter.ViewHolder> {
    private static final String TAG = ActivityItemAdapter.class.getSimpleName();

    private List<Class> activityList;
    private OnActivityItemListener listener;

    public ActivityItemAdapter(List<Class> activityList, OnActivityItemListener listener) {
        if (activityList == null) {
            this.activityList = new ArrayList<>();
        } else {
            this.activityList = activityList;
        }
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView tv = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);
        return new ViewHolder(tv, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tv.setText(activityList.get(position).getSimpleName());
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv;
        private OnActivityItemListener listener;

        public ViewHolder(@NonNull View view, final OnActivityItemListener listener) {
            super(view);
            view.setOnClickListener(this);
            tv = (TextView) view;
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.activity_item_tv: {
                    int pos = getAdapterPosition();
                    if (listener != null) {
                        listener.onClick(pos);
                    } else {
                        Log.w(TAG, "cannot notify onClick (pos="+pos+"): OnActivityItemListener instance is null");
                    }
                    break;
                }
                default: {
                    Log.d(TAG, "onClick("+view+")");
                }
            }
        }
    }

    public interface OnActivityItemListener {
        void onClick(int position);
    }
}
