package ru.giperball.qrpassword.app.writer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import ru.giperball.qrpassword.app.R;

public class CreatorActivity extends Activity {
    private static final int QR_CODE_IMAGE_WIDTH = 200;
    private static final int QR_CODE_IMAGE_HEIGHT = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);
        Button button = (Button)findViewById(R.id.generate_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.creator_activity_data_edit_text);
                String data = editText.getText().toString();
                ImageView imageView = (ImageView)findViewById(R.id.qr_code_image_view);
                Bitmap bitmap;
                try {
                    bitmap = QrEncoder.getBitmap(data, QR_CODE_IMAGE_WIDTH, QR_CODE_IMAGE_HEIGHT);
                    imageView.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    Log.e(CreatorActivity.class.getName(), "Qr code generating error", e);
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
        return super.onOptionsItemSelected(item);
    }
}
