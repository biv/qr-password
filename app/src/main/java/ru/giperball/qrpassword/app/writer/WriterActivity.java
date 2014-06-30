package ru.giperball.qrpassword.app.writer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import ru.giperball.qrpassword.app.Encryptor;
import ru.giperball.qrpassword.app.PasswordChecker;
import ru.giperball.qrpassword.app.PasswordDialog;
import ru.giperball.qrpassword.app.PasswordHolder;
import ru.giperball.qrpassword.app.QrPasswordMode;
import ru.giperball.qrpassword.app.R;
import ru.giperball.qrpassword.app.exceptions.ExceptionHandler;
import ru.giperball.qrpassword.app.exceptions.VisibleException;

public class WriterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writer);
        Button showButton = (Button)findViewById(R.id.show_button);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    handleShowButtonClick();
                } catch (Exception e) {
                    ExceptionHandler.handleException(WriterActivity.this, e);
                }
            }
        });
        Button saveButton = (Button)findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    handleSaveButtonClick();
                    Toast.makeText(WriterActivity.this, R.string.FILE_SAVED, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    ExceptionHandler.handleException(WriterActivity.this, e);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.writer, menu);
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
        Bitmap bitmap = QrCodeBitmapCreator.createSmall(data);
        ImageView imageView = (ImageView) findViewById(R.id.qr_code_image_view);
        imageView.setImageBitmap(bitmap);
    }

    private void handleSaveButtonClick() throws Exception {
        String data = getDataToEncode();
        Bitmap bitmap = QrCodeBitmapCreator.createBig(data);
        ImageSaver imageSaver = new ImageSaver(this);
        imageSaver.saveImage(bitmap, getDataForExif());
    }

    private String getDataToEncode() throws Exception {
        EditText privateDataEditText0 = (EditText) findViewById(R.id.private_data_encoding_edit_text0);
        String privateData = privateDataEditText0.getText().toString();
        EditText privateDataEditText1 = (EditText) findViewById(R.id.private_data_encoding_edit_text1);
        String privateData4Check = privateDataEditText1.getText().toString();
        if (!privateData.equals(privateData4Check)) {
            throw new VisibleException(R.string.DIFFERENT_PRIVATE_DATA);
        }
        String password = PasswordHolder.getWriterPasswordHolder().getPassword();
        PasswordChecker.checkPassword(password);
        return Encryptor.encrypt(privateData, password);
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
}
