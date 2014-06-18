package ru.giperball.qrpassword.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
