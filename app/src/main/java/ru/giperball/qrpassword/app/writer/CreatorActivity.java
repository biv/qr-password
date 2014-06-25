package ru.giperball.qrpassword.app.writer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import ru.giperball.qrpassword.app.PasswordDialog;
import ru.giperball.qrpassword.app.QrPasswordMode;
import ru.giperball.qrpassword.app.R;

public class CreatorActivity extends Activity {
    private static final int QR_CODE_PREVIEW_WIDTH = 200;
    private static final int QR_CODE_PREVIEW_HEIGHT = 200;
    private static final int QR_CODE_IMAGE_WIDTH = 1000;
    private static final int QR_CODE_IMAGE_HEIGHT = 1000;
    private static final int MAX_NUMBER_OF_FILES = 1000;
    private static final String QR_CODE_IMAGE_FILE_NAME_PATTERN = "qrpassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);
        Button showButton = (Button)findViewById(R.id.show_button);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    handleShowButtonClick();
                } catch (Exception e) {
                    Log.e(CreatorActivity.class.getSimpleName(), "Can't show preview", e);
                }
            }
        });
        Button saveButton = (Button)findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    handleSaveButtonClick();
                    Toast.makeText(CreatorActivity.this, R.string.file_saved, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e(CreatorActivity.class.getSimpleName(), "Can't save qr code to file", e);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.creator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_set_writer_password_menu_item:
                PasswordDialog passwordDialog = new PasswordDialog(this, QrPasswordMode.WRITER_PASSWORD, null);
                passwordDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleShowButtonClick() throws Exception {
        String data = getDataToEncode();
        Bitmap bitmap = createBitmap(data, QR_CODE_PREVIEW_WIDTH, QR_CODE_PREVIEW_HEIGHT);
        ImageView imageView = (ImageView) findViewById(R.id.qr_code_image_view);
        imageView.setImageBitmap(bitmap);
    }

    private void handleSaveButtonClick() throws Exception {
        String data = getDataToEncode();
        Bitmap bitmap = createBitmap(data, QR_CODE_IMAGE_WIDTH, QR_CODE_IMAGE_HEIGHT);
        //Create directory for files if it doesn't already exist
        File pictureDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File qrPasswordDirectory = new File(pictureDirectory, "QrPasswords");
        if (!qrPasswordDirectory.exists()) {
            if (!qrPasswordDirectory.mkdir()) {
                Log.e(CreatorActivity.class.getSimpleName(), "Can't create directory for images");
                throw new Exception("Can't create directory for images");
            }
        }
        //Select name for file
        File qrCodeImageFile = null;
        for (int n = 0; n < MAX_NUMBER_OF_FILES; n++) {
            String filename = makeFileName(n);
            File testFile = new File(qrPasswordDirectory, filename);
            if (!testFile.exists()) {
                qrCodeImageFile = testFile;
                break;
            }
        }
        if (qrCodeImageFile == null) {
            throw new Exception("No appropriate name for file");
        }
        //Write image to file
        OutputStream outputStream = new FileOutputStream(qrCodeImageFile);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (Exception e) {
            throw new Exception("Can't write image to file", e);
        } finally {
            outputStream.close();
        }
        //Write exif to created file
        Map<String, String> exifData = getDataForExif();
        ExifInterface exifInterface = new ExifInterface(qrCodeImageFile.getAbsolutePath());
        for (Map.Entry<String, String> entry: exifData.entrySet()) {
            exifInterface.setAttribute(entry.getKey(), entry.getValue());
        }
        exifInterface.saveAttributes();
    }

    private String getDataToEncode() {
        EditText privateDataEditText0 = (EditText) findViewById(R.id.private_data_encoding_edit_text0);
        String privateData = privateDataEditText0.getText().toString();
        EditText privateDataEditText1 = (EditText) findViewById(R.id.private_data_encoding_edit_text1);
        String privateData4Check = privateDataEditText1.getText().toString();
        if (!privateData.equals(privateData4Check)) {
            Toast.makeText(CreatorActivity.this, R.string.different_private_data, Toast.LENGTH_SHORT).show();
            return null;
        }
        return privateData;
    }

    private Map<String, String> getDataForExif() {
        Map<String, String> dataForExif = new HashMap<String, String>(2);
        EditText publicEditText = (EditText)findViewById(R.id.public_data_encoding_edit_text);
        String publicData = publicEditText.getText().toString();
        dataForExif.put("public_data", publicData);
        EditText privateEditText = (EditText)findViewById(R.id.private_data_encoding_edit_text0);
        String privateData = privateEditText.getText().toString();
        dataForExif.put("private_data", privateData);
        return dataForExif;
    }

    private Bitmap createBitmap(String data, int width, int height) throws Exception{
        try {
            return QrEncoder.getBitmap(data, width, height);
        } catch (WriterException e) {
            Log.e(CreatorActivity.class.getName(), "Qr code generating error", e);
            throw new Exception("Qr code generating error", e);
        }
    }

    private String makeFileName(int index) {
        return QR_CODE_IMAGE_FILE_NAME_PATTERN + index + ".jpg";
    }
}
