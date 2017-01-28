package klopp.klopp.listtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanBusinessActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    static String SCANNED_TEXT;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
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
            Log.d("Business check",BusinessActivity.business_list.get(i).name);
            Log.d("Scanned",SCANNED_TEXT);
            if(BusinessActivity.business_list.get(i).name.equals(SCANNED_TEXT))
            {
                Intent it = new Intent(ScanBusinessActivity.this, ScanBusinessSuccessfulActivity.class);
                startActivity(it);
                this.finish();
                return;
            }
        }

        Intent it = new Intent(ScanBusinessActivity.this, ScanBusinessFailActivity.class);
        startActivity(it);
        this.finish();


    }
}