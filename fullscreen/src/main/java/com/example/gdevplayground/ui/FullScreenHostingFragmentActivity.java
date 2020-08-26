package com.example.gdevplayground.ui;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.gdevplayground.R;

public class FullScreenHostingFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // requires WAKE_LOCK permission

        setContentView(R.layout.full_screen_hosting_fragment_activity);

        FragmentManager fm = getSupportFragmentManager();
        FullScreenFragment fullScreenFragment = (FullScreenFragment) fm.findFragmentById(R.id.full_screen_hosting_fragment_container);
        if (fullScreenFragment == null) {
            fullScreenFragment = FullScreenFragment.getInstance();
            fm.beginTransaction().add(R.id.full_screen_hosting_fragment_container, fullScreenFragment).commit();
        }
    }
}
