package com.terminatingcode.android.migrainetree.jwetherell_heart_rate_monitor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.terminatingcode.android.migrainetree.R;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * This class extends Activity to handle a picture preview, process the preview
 * for a red values and determine a heart beat.
 *
 * Forked from:
 * https://github.com/phishman3579/android-heart-rate-monitor/blob/master/src/com/jwetherell/
 * heart_rate_monitor/HeartRateMonitor.java
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class HeartRateMonitor extends Activity {

    private static final String TAG = "HeartRateMonitor";
    private static final AtomicBoolean processing = new AtomicBoolean(false);

    private static SurfaceView preview = null;
    private static SurfaceHolder previewHolder = null;
    private static Camera camera = null;
    private static View image;
    private static TextView text = null;

    private static WakeLock wakeLock = null;

    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];

    public static enum TYPE {
        GREEN, RED
    };

    private static TYPE currentType = TYPE.GREEN;

    public static TYPE getCurrent() {
        return currentType;
    }

    private static int beatsIndex = 0;
    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    0);
        }
        setContentView(R.layout.main);


        preview = (SurfaceView) findViewById(R.id.preview);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        image = findViewById(R.id.image);
        text = (TextView) findViewById(R.id.text);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();

        wakeLock.acquire();
        try {
            camera = Camera.open();
        }catch(Exception e){
            e.printStackTrace();
        }

        startTime = System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();

        wakeLock.release();
        if(camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    private static Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {

        /** Check if this device has a camera */
        private boolean checkCameraHardware(Context context) {
            if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                // this device has a camera
                return true;
            } else {
                // no camera on this device
                return false;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null) throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) throw new NullPointerException();

            if (!processing.compareAndSet(false, true)) return;

            int width = size.width;
            int height = size.height;

            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height, width);
            // Log.i(TAG, "imgAvg="+imgAvg);
            if (imgAvg == 0 || imgAvg == 255) {
                processing.set(false);
                return;
            }

            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int i = 0; i < averageArray.length; i++) {
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }

            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
            TYPE newType = currentType;
            if (imgAvg < rollingAverage) {
                newType = TYPE.RED;
                if (newType != currentType) {
                    beats++;
                    // Log.d(TAG, "BEAT!! beats="+beats);
                }
            } else if (imgAvg > rollingAverage) {
                newType = TYPE.GREEN;
            }

            if (averageIndex == averageArraySize) averageIndex = 0;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            // Transitioned from one state to another to the same
            if (newType != currentType) {
                currentType = newType;
                image.postInvalidate();
            }

            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;
            if (totalTimeInSecs >= 10) {
                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 180) {
                    startTime = System.currentTimeMillis();
                    beats = 0;
                    processing.set(false);
                    return;
                }

                // Log.d(TAG,
                // "totalTimeInSecs="+totalTimeInSecs+" beats="+beats);

                if (beatsIndex == beatsArraySize) beatsIndex = 0;
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;

                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                text.setText(String.valueOf(beatsAvg));
                startTime = System.currentTimeMillis();
                beats = 0;
            }
            processing.set(false);
        }
    };

    private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch(Exception e){
                e.printStackTrace();
            } catch (Throwable t) {
                Log.e("PreviewDemo-surfaceCallback", "Exception in setPreviewDisplay()", t);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if(camera != null) {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                Camera.Size size = getSmallestPreviewSize(width, height, parameters);
                if (size != null) {
                    parameters.setPreviewSize(size.width, size.height);
                    Log.d(TAG, "Using width=" + size.width + " height=" + size.height);
                }
                camera.setParameters(parameters);
                camera.startPreview();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignore
        }
    };

    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea < resultArea) result = size;
                }
            }
        }

        return result;
    }
}