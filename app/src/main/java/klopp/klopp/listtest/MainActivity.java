package klopp.klopp.listtest;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends ListActivity {

    static MainActivity main_activity;
    ArrayList<Business> business_list=new ArrayList<>();
    ArrayAdapter<Business> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        main_activity = this;

        setContentView(R.layout.activity_main);

        adapter=new MyAdapter(this,
                R.layout.thelinelayoutfile,
                business_list);
        setListAdapter(adapter);


        SharedPreferences prefs = getSharedPreferences(getString(R.string.preferences_file), MODE_PRIVATE);
        String email = prefs.getString(getString(R.string.email_preferences_key), null);
        String token = prefs.getString(getString(R.string.token_preferences_key), null);
        getBusinesses(email, token);
    }

    void getBusinesses(String email, String auth_token)
    {
        business_list.clear();
        String url = getString(R.string.base_url) + "/api/v1/examples/get_businesses?user_token="+auth_token+"&user_email="+email;

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: handle success
                try {
                    JSONArray businesses = response.getJSONArray("businesses");

                    for(int i=0;i<businesses.length();i++) {
                        business_list.add(new Business(Integer.parseInt(businesses.getJSONObject(i).getString("id")),
                                businesses.getJSONObject(i).getString("name"),
                                businesses.getJSONObject(i).getString("description"),
                                businesses.getJSONObject(i).getJSONObject("image").getString("url")));
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
        Log.d("Clicked:",business_list.get(position).id+"");
        Log.d("Clicked:",business_list.get(position).name);
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
}
