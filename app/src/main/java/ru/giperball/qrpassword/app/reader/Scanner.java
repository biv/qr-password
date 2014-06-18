package ru.giperball.qrpassword.app.reader;


import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Scan pictures from camera.
 */
public class Scanner {
    private ScannerListener scannerListener;
    private CameraManager cameraManager;
    private final ScannerHandler scannerHandler;
    private Handler decodeHandler;
    private boolean isScanEnabled = false;

    static final int SUCCESSFUL_RESULT = 1;
    static final int ONE_MORE_SHOT = 2;

    public Scanner(ScannerListener scannerListener, CameraManager cameraManager) {
        this.scannerListener = scannerListener;
        this.cameraManager = cameraManager;
        scannerHandler = new ScannerHandler();
        Decoder decoder = new Decoder(scannerHandler);
        new Thread(decoder).start();
        decodeHandler = decoder.getHandler();
    }

    public void startScan() {
        isScanEnabled = true;
        getPictureAndSend2Decode();
    }

    public void stopScan() {
        isScanEnabled = false;
    }

    public interface ScannerListener {
        void onScannerResult(String scanResult);
    }

    private void getPictureAndSend2Decode() {
        cameraManager.getOnePicture(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                Camera.Size previewSize = camera.getParameters().getPreviewSize();
                Message message = decodeHandler.obtainMessage(Decoder.DECODE,
                        previewSize.width, previewSize.height, data);
                message.sendToTarget();
            }
        });
    }

    private class ScannerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESSFUL_RESULT:
                    Log.i(Scanner.class.getName(), "SUCCESS");
                    String decodeResult = (String)msg.obj;
                    scannerListener.onScannerResult(decodeResult);
                    break;
                case ONE_MORE_SHOT:
                    if (isScanEnabled) {
                        getPictureAndSend2Decode();
                    }
                    break;
            }
        }
    }
}
