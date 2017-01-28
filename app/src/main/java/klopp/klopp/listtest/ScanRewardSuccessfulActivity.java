package klopp.klopp.listtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import klopp.klopp.listtest.customfonts.MyRegularText;

public class ScanRewardSuccessfulActivity extends AppCompatActivity {

    Business business;
    Reward reward;
    NetworkImageView mNetworkImageView;
    ImageLoader mImageLoader;

    MyRegularText exit_button;
    MyRegularText message;

    ScanRewardSuccessfulActivity scan_reward_successful_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_reward_successful);

        scan_reward_successful_activity = this;

        exit_button = (MyRegularText)findViewById(R.id.button_exit);
        message = (MyRegularText)findViewById(R.id.message);
        mNetworkImageView = (NetworkImageView)findViewById(R.id.business_image);

        business = BusinessActivity.business_list.get(Integer.parseInt(getIntent().getStringExtra("business_index")));
        reward = RewardsActivity.rewards_list.get(Integer.parseInt(getIntent().getStringExtra("reward_index")));

        mImageLoader = MySingleton.getInstance(BusinessActivity.main_activity).getImageLoader();
        mNetworkImageView.setImageUrl(BusinessActivity.main_activity.getString(R.string.base_url) + business.image_url, mImageLoader);

        message.setText("¡Has solicitado un " + reward.name + " exitosamente!");

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan_reward_successful_activity.finish();
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
