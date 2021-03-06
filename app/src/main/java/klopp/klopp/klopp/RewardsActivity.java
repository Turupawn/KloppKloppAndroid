package klopp.klopp.klopp;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import klopp.klopp.klopp.R;

public class RewardsActivity extends ListActivity {

    static ArrayList<Reward> rewards_list=new ArrayList<>();
    ArrayAdapter<Reward> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        adapter=new RewardsAdapter(this,
                R.layout.business_row,
                rewards_list);
        setListAdapter(adapter);


        SharedPreferences prefs = getSharedPreferences(getString(R.string.preferences_file), MODE_PRIVATE);
        String email = prefs.getString(getString(R.string.email_preferences_key), null);
        String token = prefs.getString(getString(R.string.token_preferences_key), null);
        int business_id = Integer.parseInt(getIntent().getStringExtra("business_id"));
        setRewards(email, token, business_id);
    }

    void setRewards(String email, String auth_token, int business_id)
    {
        rewards_list.clear();
        String url = getString(R.string.base_url) + "/api/v1/users/get_rewards?user_token="+auth_token+"&user_email="+email+"&business_id="+business_id;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: handle success
                try {
                    JSONArray businesses = response.getJSONArray("rewards");
                    for(int i=0;i<businesses.length();i++) {
                        rewards_list.add(new Reward(Integer.parseInt(businesses.getJSONObject(i).getString("id")),
                                                        businesses.getJSONObject(i).getString("name"),
                                                        Integer.parseInt(businesses.getJSONObject(i).getString("business_id")),
                                                        Integer.parseInt(businesses.getJSONObject(i).getString("klopps")),
                                                        businesses.getJSONObject(i).getJSONObject("image").getString("url"),
                                                        true));
                    }
                    adapter.notifyDataSetChanged();
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //TODO: handle failure
            }
        });

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Business business = BusinessActivity.business_list.get(0);
        boolean user_has_enough_klopps = false;

        for(int i=0;i<BusinessActivity.business_list.size();i++)
        {
            if(BusinessActivity.business_list.get(i).id == rewards_list.get(position).business_id)
            {
                business = BusinessActivity.business_list.get(i);
            }
        }

        if(business != null && business.current_user_klopps >= rewards_list.get(position).klopps)
        {
            user_has_enough_klopps = true;
        }

        if(user_has_enough_klopps)
        {
            Intent it = new Intent(RewardsActivity.this, ScanRewardActivity.class);
            it.putExtra("reward_index", position + "");
            startActivity(it);
        }else
        {
            Resources res = getResources();
            String[] messages = res.getStringArray(R.array.rewards_messages);

            int idx = new Random().nextInt(messages.length);
            messageDialog(messages[idx]);
        }

    }

    public void messageDialog(String message){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rewards, menu);
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
