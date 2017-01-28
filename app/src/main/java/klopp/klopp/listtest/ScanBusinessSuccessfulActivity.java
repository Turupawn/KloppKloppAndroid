package klopp.klopp.listtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import klopp.klopp.listtest.customfonts.MyRegularText;

public class ScanBusinessSuccessfulActivity extends AppCompatActivity {

    Business business;
    NetworkImageView mNetworkImageView;
    ImageLoader mImageLoader;

    MyRegularText exit_button;

    ScanBusinessSuccessfulActivity scan_business_successful_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_successful);

        scan_business_successful_activity=this;

        exit_button = (MyRegularText)findViewById(R.id.button_exit);
        mNetworkImageView = (NetworkImageView)findViewById(R.id.business_image);

        business = BusinessActivity.business_list.get(Integer.parseInt(getIntent().getStringExtra("business_index")));


        mImageLoader = MySingleton.getInstance(BusinessActivity.main_activity).getImageLoader();
        mNetworkImageView.setImageUrl(BusinessActivity.main_activity.getString(R.string.base_url)+business.image_url, mImageLoader);

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan_business_successful_activity.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scan_successful, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
