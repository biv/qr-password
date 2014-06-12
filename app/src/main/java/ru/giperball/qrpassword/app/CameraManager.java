package ru.giperball.qrpassword.app;

import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Manage working with camera
 */
public class CameraManager {

    private SurfaceHolder surfaceHolder;
    private Camera camera;

    public CameraManager(final SurfaceHolder surfaceHolder) {
        surfaceHolder.addCallback(new PreviewSurfaceCallback());
    }

    public void startCamera() {
        camera = Camera.open();
        if (camera == null) {
            throw new RuntimeException("Camera not found");
        }
        startCameraPreview();
    }

    public void stopCamera() {
        stopCameraPreview();
        camera.release();
        camera = null;
    }

    private void startCameraPreview() {
        if (surfaceHolder != null && camera != null) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (IOException e) {
                camera.release();
                throw new RuntimeException("Can't start camera preview");
            }
        }
    }

    private void stopCameraPreview() {
        if (camera == null) {
            return;
        }
        camera.stopPreview();
        try {
            camera.setPreviewDisplay(null);
        } catch (IOException e) {
            camera.release();
            throw new RuntimeException("Can't stop camera preview");
        }
    }

    private class PreviewSurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            surfaceHolder = holder;
            startCameraPreview();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            stopCameraPreview();
            surfaceHolder = null;
        }
    }
}