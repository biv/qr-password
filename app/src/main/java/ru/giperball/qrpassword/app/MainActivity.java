package ru.giperball.qrpassword.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import ru.giperball.qrpassword.app.reader.CaptureActivity;
import ru.giperball.qrpassword.app.reader.ReaderPasswordDialog;
import ru.giperball.qrpassword.app.writer.CreatorActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button go2CreateQrCodeButton = (Button)findViewById(R.id.go2create_button);
        go2CreateQrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go2WriterActivity();
            }
        });

        Button go2ReadQrCodeButton = (Button)findViewById(R.id.go2read_button);
        go2ReadQrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PasswordHolder.isReaderPasswordSet()) {
                    go2ReaderActivity();
                } else {
                    ReaderPasswordDialog passwordDialog = new ReaderPasswordDialog(
                            MainActivity.this,
                            new PasswordDialogSubmitListener() {
                                @Override
                                public void onDialogSubmit() {
                                    go2ReaderActivity();
                                }
                            });
                    passwordDialog.show();
                }
            }
        });
    }

    private void go2ReaderActivity() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivity(intent);
    }

    private void go2WriterActivity() {
        Intent intent = new Intent(this, CreatorActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_set_reader_password_menu_item:
                ReaderPasswordDialog passwordDialog = new ReaderPasswordDialog(this, null);
                passwordDialog.show();
                break;
            case R.id.main_menu_set_writer_password_menu_item:
                break;
        }
        return true;
    }
}
