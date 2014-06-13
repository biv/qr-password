package ru.giperball.qrpassword.app;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

/**
 * Created by biv on 14.06.14.
 */
public class Decoder implements Runnable {
    private Handler handler;
    private Handler outterHandler;
    private final CountDownLatch handlerInitLatch;
    private boolean isRunning;

    public static final int DECODE = 1;
    public static final int QUIT = 0;

    public Decoder(Handler handler) {
        handlerInitLatch = new CountDownLatch(1);
        this.outterHandler = handler;
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
        @Override
        public void handleMessage(Message msg) {
            if (!isRunning) {
                return;
            }
            switch (msg.what) {
                case DECODE:
                    byte[] data = (byte[])msg.obj;
                    try {
                        String decodeResult = decode(data);
                        outterHandler.obtainMessage(Scanner.SUCCESSFUL_RESULT, decodeResult).sendToTarget();
                    } catch (Exception e) {
                        outterHandler.obtainMessage(Scanner.ONE_MORE_SHOT).sendToTarget();
                    }
                    break;
                case QUIT:
                    isRunning = false;
                    Looper.myLooper().quit();                    
                    break;
            }
        }

        private String decode(byte[] data) throws Exception{
            Log.i(Decoder.class.getName(), "decode");
            Thread.sleep(3000);
            return "PREVED"; //Todo
        }
    }
}
