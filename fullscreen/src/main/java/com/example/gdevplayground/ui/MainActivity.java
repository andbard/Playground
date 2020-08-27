package com.example.gdevplayground.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gdevplayground.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ActivityItemAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DividerItemDecoration divider;

    private List<Class> activityList = new ArrayList<>();

    private ActivityItemAdapter.OnActivityItemListener onActivityItemListener = new ActivityItemAdapter.OnActivityItemListener() {
        @Override
        public void onClick(int position) {
            startActivity(new Intent(MainActivity.this, activityList.get(position)));
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        activityList.add(FullScreenActivity.class); // wake_lock
        activityList.add(FullScreenHostingFragmentActivity.class); // wake_lock
        activityList.add(AndroidStudioGeneratedFullScreenActivity.class); // NO wake_lock

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
}
