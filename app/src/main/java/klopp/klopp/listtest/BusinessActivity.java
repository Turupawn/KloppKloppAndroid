package klopp.klopp.listtest;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BusinessActivity extends ListActivity {

    static BusinessActivity main_activity;
    public static ArrayList<Business> business_list = new ArrayList<>();
    ArrayAdapter<Business> adapter;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        main_activity = this;

        setContentView(R.layout.activity_businesses);

        username = (TextView)findViewById(R.id.username);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.preferences_file), MODE_PRIVATE);
        username.setText(prefs.getString(getString(R.string.username_preferences_key), null));

        TextView buttonn = (TextView)findViewById(R.id.changeLocation);
        buttonn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(BusinessActivity.this, ScanBusinessActivity.class);
                startActivity(it);
            }
        });

        adapter=new BusinessAdapter(this,
                R.layout.business_row,
                business_list);
        setListAdapter(adapter);


        String email = prefs.getString(getString(R.string.email_preferences_key), null);
        String token = prefs.getString(getString(R.string.token_preferences_key), null);
        setBusinesses(email, token);
    }

    void setBusinesses(String email, String auth_token)
    {
        business_list.clear();
        String url = getString(R.string.base_url) + "/api/v1/users/get_businesses?user_token="+auth_token+"&user_email="+email;

        Log.d("Orales",url);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: handle success
                try {
                    JSONArray businesses = response.getJSONArray("businesses");

                    for(int i=0;i<businesses.length();i++) {
                        Log.d("Orales",businesses.getJSONObject(i).getJSONObject("business").toString());

                        business_list.add(new Business(Integer.parseInt(businesses.getJSONObject(i).getJSONObject("business").getString("id")),
                                businesses.getJSONObject(i).getJSONObject("business").getString("name"),
                                businesses.getJSONObject(i).getJSONObject("business").getString("description"),
                                businesses.getJSONObject(i).getJSONObject("business").getJSONObject("image").getString("url"),
                                Integer.parseInt(businesses.getJSONObject(i).getString("klopps"))
                        ));
                    }
                    adapter.notifyDataSetChanged();
                }catch(Exception e)
                {

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

        Log.d("Clicked:", position + "");
        Log.d("Clicked:",id+"");
        Log.d("Clicked:", business_list.get(position).id + "");
        Log.d("Clicked:",business_list.get(position).name);

        Intent it = new Intent(BusinessActivity.this, RewardsActivity.class);
        it.putExtra("business_id",business_list.get(position).id+"");
        startActivity(it);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }


    public void requestKlopps(View view) {
        Intent it = new Intent(BusinessActivity.this, ScanBusinessActivity.class);
        startActivity(it);
    }

    public void refresh(View view) {
        Log.d("Orale","Refresh");
    }
}
