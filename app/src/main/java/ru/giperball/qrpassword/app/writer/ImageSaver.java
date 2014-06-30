package ru.giperball.qrpassword.app.writer;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Save qr code images to storage
 */
public class ImageSaver {
    private static final int MAX_NUMBER_OF_FILES = 100;
    private static final String QR_CODE_IMAGE_FILE_NAME_PATTERN = "qrpassword";
    private final Context context;

    public ImageSaver(Context context) {
        this.context = context;
    }

    public void saveImage(Bitmap bitmap, Map<String, String> exifData) throws Exception {
        File imageFile = getImageFile();
        OutputStream outputStream = new FileOutputStream(imageFile);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (Exception e) {
            throw new Exception("Can't write image to file", e);
        } finally {
            outputStream.close();
        }
        //Write exif to created file
        ExifInterface exifInterface = new ExifInterface(imageFile.getAbsolutePath());
        for (Map.Entry<String, String> entry: exifData.entrySet()) {
            exifInterface.setAttribute(entry.getKey(), entry.getValue());
        }
        exifInterface.saveAttributes();
    }

    private File getImageFile() throws Exception {
        File qrPasswordDirectory = getImageDirectory();
        //Select name for file
        File imageFile = null;
        for (int n = 0; n < MAX_NUMBER_OF_FILES; n++) {
            String filename = makeFileName(n);
            File testFile = new File(qrPasswordDirectory, filename);
            if (!testFile.exists()) {
                imageFile = testFile;
                break;
            }
        }
        if (imageFile == null) {
            throw new Exception("No appropriate name for file");
        }
        return imageFile;
    }

    private File getImageDirectory() throws Exception {
        File pictureDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File qrPasswordDirectory = new File(pictureDirectory, "QrPasswords");
        if (!qrPasswordDirectory.exists()) {
            if (!qrPasswordDirectory.mkdir()) {
                throw new Exception("Can't create directory for images");
            }
        }
        return qrPasswordDirectory;
    }

    private String makeFileName(int index) {
        return QR_CODE_IMAGE_FILE_NAME_PATTERN + index + ".jpg";
    }
}
