package klopp.klopp.klopp;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import klopp.klopp.klopp.customfonts.MyRegularText;
import klopp.klopp.klopp.R;

public class ScanRewardSuccessfulActivity extends AppCompatActivity {

    Business business;
    Reward reward;
    NetworkImageView mNetworkImageView;
    ImageLoader mImageLoader;
    MediaPlayer mPlayer;

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

        mPlayer = MediaPlayer.create(scan_reward_successful_activity, R.raw.klopp);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan_reward_successful_activity.finish();
            }
        });

        sendRequest();
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

    void sendRequest()
    {
        String url = getString(R.string.base_url) + "/api/v1/rewards/costumer_request";

        SharedPreferences prefs = getSharedPreferences(getString(R.string.preferences_file), MODE_PRIVATE);
        String token = prefs.getString(getString(R.string.token_preferences_key), null);
        String email = prefs.getString(getString(R.string.email_preferences_key), null);

        JSONObject params = new JSONObject();
        try {
            params.put("user_token", token);
            params.put("user_email", email);
            params.put("reward_id", reward.id);
        }catch (Exception e)
        {
            message.setText("Error");
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, params.toString(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    onRequestSuccsessful();
                }catch(Exception e)
                {
                    message.setText("Error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                message.setText("Error");
            }
        });
        Volley.newRequestQueue(ScanRewardSuccessfulActivity.this).add(jsonRequest);
    }

    void onRequestSuccsessful()
    {
        mPlayer.start();

        mNetworkImageView.setImageUrl(BusinessActivity.main_activity.getString(R.string.base_url) + business.image_url, mImageLoader);
        message.setText("¡Has solicitado un " + reward.name + " exitosamente!");
    }
}
