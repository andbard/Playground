package com.example.surfaceview.lifecycle;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.surfaceview.camerasync.SurfaceViewCameraSyncActivity;

/**
 * <a, href="https://developer.android.com/reference/android/view/SurfaceView">SurfaceView doc excerpt</a>:
 * <br>
 * One of the purposes of this class is to provide a surface in which a secondary thread can render into the screen.
 * If you are going to use it this way, you need to be aware of some threading semantics:
 * <ul>
 *     <li>All SurfaceView and SurfaceHolder.Callback methods will be called from the thread running the SurfaceView's window (typically the main thread of the application).
 *     They thus need to correctly synchronize with any state that is also touched by the drawing thread.</li>
 *     <li>You must ensure that the drawing thread only touches the underlying Surface while it is valid -- between SurfaceHolder.Callback#surfaceCreated and SurfaceHolder.Callback#surfaceDestroyed.</li>
 * </ul>
 * Note: Starting in platform version Build.VERSION_CODES.N, SurfaceView's window position is updated synchronously with other View rendering. This means that translating and scaling a SurfaceView on screen will not cause rendering artifacts. Such artifacts may occur on previous versions of the platform when its window is positioned asynchronously.
 */
public class SurfaceViewLifecycleActivity extends AppCompatActivity {
    private static final String TAG = SurfaceViewCameraSyncActivity.class.getSimpleName();

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        surfaceView = new SurfaceView(this);
        setContentView(surfaceView);
        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(new SurfaceHolder.Callback2() {
            @Override
            public void surfaceRedrawNeeded(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "surfaceRedrawNeeded");
            }

            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "surfaceCreated");
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                Log.d(TAG, "surfaceChanged");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "surfaceDestroyed");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*
        n.b.: (https://source.android.com/devices/graphics/arch-sv-glsv?hl=en)
        If you tap the power button to blank the screen, you get only onPause() without surfaceDestroyed().
        The surface remains active, and rendering can continue. You can keep getting Choreographer events if you continue to request them.
        If you have a lock screen that forces a different orientation, your activity may be restarted when the device is unblanked.
        Otherwise, you can come out of screen-blank with the same surface as before.

        The lifespan of the thread can be tied to the surface or to the activity, depending on what you want to happen when the screen goes blank.
        The thread can start/stop either on Activity start/stop or on surface create/destroy.

        Having the thread start/stop on Activity start/stop works well with the app lifecycle.
        You start the renderer thread in onResume() and stop it in onStop().
        When creating and configuring the thread, sometimes the surface already exists,
        other times it doesn't (for example, it's still active after toggling the screen with the power button).
        You have to wait for the surface to be created before initializing in the thread.
        You can't initialize in the surfaceCreate() callback because it won't fire again if the surface wasn't recreated.
        Instead, query or cache the surface state, and forward it to the renderer thread.

        Note: Be careful when passing objects between threads. It is best to pass the surface or SurfaceHolder through a Handler message
        (rather than just stuffing it into the thread) to avoid issues on multicore systems. For details, refer to SMP Primer for Android.

        Having the thread start/stop on surface create/destroy works well because the surface and the renderer are logically intertwined.
        You start the thread after the surface is created, which avoids some interthread communication concerns; and surface created/changed messages are simply forwarded.
        To ensure that rendering stops when the screen goes blank and resumes when it un-blanks, tell Choreographer to stop invoking the frame draw callback.
        onResume() resumes the callbacks if the renderer thread is running. However, if you animate based on elapsed time between frames,
        there could be a large gap before the next event arrives; using an explicit pause/resume message can solve this issue.

        Note: For an example of having a thread start/stop on surface create/destory, see Grafika's Hardware scaler activity:
        https://github.com/google/grafika/blob/master/app/src/main/java/com/android/grafika/HardwareScalerActivity.java
        */
        if (isFinishing()) {
            Log.d(TAG, "onPause, isFinishing");
        } else {
            Log.d(TAG, "onPause");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
