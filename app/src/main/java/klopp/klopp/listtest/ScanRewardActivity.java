package klopp.klopp.listtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanRewardActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    static String SCANNED_TEXT;
    Reward reward;
    int reward_index;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view

        reward_index = Integer.parseInt(getIntent().getStringExtra("reward_index"));
        reward = RewardsActivity.rewards_list.get(reward_index);
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
            if(reward.business_id == BusinessActivity.business_list.get(i).id
            && BusinessActivity.business_list.get(i).name.equals(SCANNED_TEXT))
            {
                Intent it = new Intent(ScanRewardActivity.this, ScanRewardSuccessfulActivity.class);
                it.putExtra("reward_index", reward_index+"");
                it.putExtra("business_index", i+"");
                startActivity(it);
                this.finish();
                return;
            }
        }

        Intent it = new Intent(ScanRewardActivity.this, ScanBusinessFailActivity.class);
        startActivity(it);
        this.finish();


    }
}
