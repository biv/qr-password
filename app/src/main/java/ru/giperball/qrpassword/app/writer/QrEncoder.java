package ru.giperball.qrpassword.app.writer;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Encode string to bitmap
 */
public class QrEncoder {

    public static Bitmap getBitmap(String data, int width, int height) throws WriterException{
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
