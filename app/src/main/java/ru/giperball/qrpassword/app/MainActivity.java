package ru.giperball.qrpassword.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button go2CreateQrCodeButton = (Button)findViewById(R.id.go2create_button);
        go2CreateQrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleActivityLaunch(QrPasswordMode.WRITER_PASSWORD);
            }
        });

        Button go2ReadQrCodeButton = (Button)findViewById(R.id.go2read_button);
        go2ReadQrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleActivityLaunch(QrPasswordMode.READER_PASSWORD);
            }
        });
    }

    void handleActivityLaunch(QrPasswordMode mode) {
        PasswordHolder passwordHolder = mode.getPasswordHolder();
        final Intent intent = new Intent(this, mode.getActivityClass());
        if (passwordHolder.isPasswordSet()) {
            startActivity(intent);
        } else {
            PasswordDialog passwordDialog = new PasswordDialog(this, mode, new PasswordDialogSubmitListener() {
                @Override
                public void onDialogSubmit() {
                    MainActivity.this.startActivity(intent);
                }
            });
            passwordDialog.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        QrPasswordMode mode = null;
        switch (item.getItemId()) {
            case R.id.main_menu_set_reader_password_menu_item:
                mode = QrPasswordMode.READER_PASSWORD;
                break;
            case R.id.main_menu_set_writer_password_menu_item:
                mode = QrPasswordMode.WRITER_PASSWORD;
                break;
        }
        PasswordDialog passwordDialog = new PasswordDialog(this, mode, null);
        passwordDialog.show();
        return true;
    }
}
