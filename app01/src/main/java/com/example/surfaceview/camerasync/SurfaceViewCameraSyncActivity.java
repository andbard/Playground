package com.example.surfaceview.camerasync;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.playground.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * https://source.android.com/devices/graphics/arch-sv-glsv?hl=en:
 * <br>
 * When you render with an external buffer source, such as GL context or a media decoder,
 * you need to copy buffers from the buffer source to display the buffers on the screen.
 * Using a SurfaceView enables you to do that.
 * <br>
 * To receive callbacks when the surface is created or destroyed, use the SurfaceHolder interface.
 * <br>
 * Rendering with SurfaceView is beneficial in cases where you need to render to a separate surface,
 * such as when you render with the Camera API or an OpenGL ES context.
 * When you render with SurfaceView, SurfaceFlinger directly composes buffers to the screen.
 * Without a SurfaceView, you need to composite buffers to an offscreen surface, which then gets composited to the screen,
 * so rendering with SurfaceView eliminates extra work.
 * After rendering with SurfaceView, use the UI thread to coordinate with the activity lifecycle and make adjustments to the size or position of the view if needed.
 * Then, the Hardware Composer blends the app UI and the other layers.
 * <br>
 * The new surface is the producer side of a BufferQueue, whose consumer is a SurfaceFlinger layer.
 * You can update the surface with any mechanism that can feed a BufferQueue,
 * such as surface-supplied Canvas functions,
 * attaching an EGLSurface and drawing on the surface with GLES,
 * or configuring a media decoder to write the surface.
 */
public class SurfaceViewCameraSyncActivity extends Activity implements SurfaceHolder.Callback {
    private static final String TAG = SurfaceViewCameraSyncActivity.class.getSimpleName();

    TextView testView;

    Camera camera;
    SurfaceView surfaceView;
    // To receive callbacks when the surface is created or destroyed, use the SurfaceHolder interface.
    // By default, the newly created surface is placed behind the app UI surface.
    // You can override the default Z-ordering to put the new surface on top.
    SurfaceHolder surfaceHolder;

    android.hardware.Camera.PictureCallback rawCallback;
    android.hardware.Camera.ShutterCallback shutterCallback;
    android.hardware.Camera.PictureCallback jpegCallback;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.surfaceview_camerasync_activity);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        // the SurfaceHolder instance allows us to control the surface size and format, edit the pixels in the surface, and monitor changes to the surface
        surfaceHolder = surfaceView.getHolder();
        // set a SurfaceHolder.Callback so we get notified when the underlying surface is created and destroyed
        surfaceHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        jpegCallback = new android.hardware.Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {
                    outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));
                    outStream.write(data);
                    outStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
                Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_LONG).show();
                refreshCamera();
            }
        };
    }

    public void captureImage(View v) throws IOException {
        //take the picture
        camera.takePicture(null, null, jpegCallback);
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            Log.w(TAG, "preview surface does not exist");
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            Log.w(TAG, "Exception trying to stop a not existing preview");
        }

        // set preview size and make any resize, rotate or reformatting changes here start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // now that the size is known, set up the camera parameters and begin the preview
        refreshCamera();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // open the camera service
            camera = Camera.open();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return;
        }
        // retrieve and set the camera parameters
        Camera.Parameters param;
        param = camera.getParameters();
        param.setPreviewSize(352, 288);
        camera.setParameters(param);
        try {
            // the Surface has been created, now tell the camera where to draw the preview.
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // stop preview and release camera
        camera.stopPreview();
        camera.release();
        camera = null;
    }

}
