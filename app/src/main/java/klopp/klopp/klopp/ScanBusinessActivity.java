package klopp.klopp.klopp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanBusinessActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    static String SCANNED_TEXT;
    static int REQUEST_CAMERA_ACCESS = 666;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                this.checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, this.REQUEST_CAMERA_ACCESS);
        } else {
            mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
            setContentView(mScannerView);                // Set the scanner view as the content view
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

        SCANNED_TEXT = rawResult.getText();

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);

        for(int i=0;i<BusinessActivity.business_list.size();i++)
        {
            if(BusinessActivity.business_list.get(i).name.equals(SCANNED_TEXT))
            {
                Intent it = new Intent(ScanBusinessActivity.this, ScanBusinessSuccessfulActivity.class);
                it.putExtra("business_index", i+"");
                startActivity(it);
                this.finish();
                return;
            }
        }

        Intent it = new Intent(ScanBusinessActivity.this, ScanBusinessFailActivity.class);
        startActivity(it);
        this.finish();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == this.REQUEST_CAMERA_ACCESS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
                setContentView(mScannerView);                // Set the scanner view as the content view
            }
        }
    }
}