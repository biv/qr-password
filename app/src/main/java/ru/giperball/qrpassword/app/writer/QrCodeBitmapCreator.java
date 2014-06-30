package ru.giperball.qrpassword.app.writer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Create image with qr code from string.
 */
public class QrCodeBitmapCreator {
    private static final int SMALL_BITMAP_WIDTH = 200;
    private static final int SMALL_BITMAP_HEIGHT = 200;
    private static final int BIG_BITMAP_WIDTH = 1000;
    private static final int BIG_BITMAP_HEIGHT = 1000;

    public static Bitmap createSmall(String data) throws Exception {
        return createBitmap(data, SMALL_BITMAP_WIDTH, SMALL_BITMAP_HEIGHT);
    }

    public static Bitmap createBig(String data) throws Exception {
        return createBitmap(data, BIG_BITMAP_WIDTH, BIG_BITMAP_HEIGHT);
    }

    private static Bitmap createBitmap(String data, int width, int height) throws Exception {
        try {
            return getBitmap(data, width, height);
        } catch (WriterException e) {
            Log.e(WriterActivity.class.getName(), "Qr code generating error", e);
            throw new Exception("Qr code generating error", e);
        }
    }

    private static Bitmap getBitmap(String data, int width, int height) throws WriterException{
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height);
        int imageWidth = bitMatrix.getWidth();
        int imageHeight = bitMatrix.getHeight();
        int[]pixels = new int[imageHeight * imageWidth];
        for (int y = 0; y < imageHeight; y++) {
            int offset = y*imageWidth;
            for (int x = 0; x < imageWidth; x++) {
                pixels[x + offset] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
