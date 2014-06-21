package ru.giperball.qrpassword.app.reader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import ru.giperball.qrpassword.app.R;


public class CaptureActivity extends Activity implements Scanner.ScannerListener {
    private CameraManager cameraManager;
    private Scanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        SurfaceView surfaceView = (SurfaceView)findViewById(R.id.camera_preview);
        cameraManager = new CameraManager(surfaceView.getHolder());
        scanner = new Scanner(this, cameraManager);
        ToggleButton captureButton = (ToggleButton)findViewById(R.id.capture_button);
        captureButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    scanner.startScan();
                } else {
                    scanner.stopScan();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraManager.startCamera();
    }

    @Override
    protected void onPause() {
        scanner.stopScan();
        cameraManager.stopCamera();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.capture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_set_reader_password_menu_item:
                ReaderPasswordDialog passwordDialog = new ReaderPasswordDialog(this, null);
                passwordDialog.show();
                break;
        }
        return true;
    }

    @Override
    public void onScannerResult(String scanResult) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.result_dialog_title);
        builder.setMessage(scanResult);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToggleButton captureButton = (ToggleButton)findViewById(R.id.capture_button);
                captureButton.setChecked(false);
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
