package klopp.klopp.listtest;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        main_activity = this;

        setContentView(R.layout.activity_main);

        adapter=new MyAdapter(this,
                R.layout.thelinelayoutfile,
                listItems);
        setListAdapter(adapter);
    }

    public void addItems(View v) {
        listItems.clear();
        adapter.notifyDataSetChanged();

        String url = getString(R.string.base_url) + "/api/v1/users/sign_in";

        JSONObject user_param = new JSONObject();
        JSONObject params = new JSONObject();
        try {
            user_param.put("password", "111111");
            user_param.put("email", "a@a.com");
            params.put("user",user_param);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Jsontest", params.toString());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, params.toString(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: handle success
                try {
                    String auth_token = response.getJSONObject("user").getString("authentication_token");
                    Log.d("Auth token", auth_token);

                    getBusinesses(auth_token);
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

    void getBusinesses(String auth_token)
    {
        String url = getString(R.string.base_url) + "/api/v1/examples/get_businesses?user_token="+auth_token+"&user_email=a@a.com";

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: handle success
                try {
                    JSONArray businesses = response.getJSONArray("businesses");

                    for(int i=0;i<businesses.length();i++) {
                        String list_text = businesses.getJSONObject(i).getString("name");
                        list_text+=": ";
                        list_text+=businesses.getJSONObject(i).getString("description");
                        list_text+=": ";
                        list_text+=businesses.getJSONObject(i).getJSONObject("image").getString("url");
                        listItems.add(list_text);
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
