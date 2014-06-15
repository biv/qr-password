package ru.giperball.qrpassword.app;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.concurrent.CountDownLatch;

/**
 * Created by biv on 14.06.14.
 */
public class Decoder implements Runnable {
    private Handler handler;
    private Handler outerHandler;
    private final CountDownLatch handlerInitLatch;
    private boolean isRunning;

    public static final int DECODE = 1;
    public static final int QUIT = 0;

    public Decoder(Handler handler) {
        handlerInitLatch = new CountDownLatch(1);
        this.outerHandler = handler;
    }

    public Handler getHandler() {
        try {
            handlerInitLatch.await();
        } catch (InterruptedException e) {
        }
        return handler;
    }
    @Override
    public void run() {
        isRunning = true;
        Looper.prepare();
        handler = new DecodeHandler();
        handlerInitLatch.countDown();
        Looper.loop();
    }

    private class DecodeHandler extends Handler {
        private MultiFormatReader reader = new MultiFormatReader();

        @Override
        public void handleMessage(Message msg) {
            if (!isRunning) {
                return;
            }
            switch (msg.what) {
                case DECODE:
                    byte[] data = (byte[])msg.obj;
                    int width = msg.arg1;
                    int height = msg.arg2;
                    try {
                        String decodeResult = decode(data, width, height);
                        outerHandler.obtainMessage(Scanner.SUCCESSFUL_RESULT, decodeResult).sendToTarget();
                        Log.i(Decoder.class.getName(), "successful decode");
                    } catch (Exception e) {
                        outerHandler.obtainMessage(Scanner.ONE_MORE_SHOT).sendToTarget();
                        Log.i(Decoder.class.getName(), "decode failed: " + e.getMessage());
                    }
                    break;
                case QUIT:
                    isRunning = false;
                    Looper.myLooper().quit();                    
                    break;
            }
        }

        private String decode(byte[] data, int width, int height) throws Exception{
            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height, false);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = reader.decode(bitmap);
            return result.getText();
        }
    }
}
