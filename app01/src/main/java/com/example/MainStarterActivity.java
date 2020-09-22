package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playground.R;
import com.example.surfaceview.lifecycle.SurfaceViewLifecycleActivity;

import java.util.ArrayList;
import java.util.List;

public class MainStarterActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ActivityItemAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DividerItemDecoration divider;

    private List<Class> activityList = new ArrayList<>();

    private ActivityItemAdapter.OnActivityItemListener onActivityItemListener = new ActivityItemAdapter.OnActivityItemListener() {
        @Override
        public void onClick(int position) {
            startActivity(new Intent(MainStarterActivity.this, activityList.get(position)));
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_starter_activity);

        activityList.add(SurfaceViewLifecycleActivity.class);

        recyclerView = findViewById(R.id.recycler_view);
        // use this setting to improve performance if you know that changes in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        divider = new DividerItemDecoration(recyclerView.getContext(), ((LinearLayoutManager) layoutManager).getOrientation());
        recyclerView.addItemDecoration(divider);

        // specify an adapter (see also next example)
        adapter = new ActivityItemAdapter(activityList, onActivityItemListener);
        recyclerView.setAdapter(adapter);
    }

    public static class ActivityItemAdapter extends RecyclerView.Adapter<ActivityItemAdapter.ViewHolder> {
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
}
