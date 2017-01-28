package klopp.klopp.listtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import klopp.klopp.listtest.customfonts.MyRegularText;


public class Login extends AppCompatActivity {

    private TextView signup;
    private TextView signin;
    private TextView fb;
    private TextView account;
    private EditText email;
    private EditText password;

    MyRegularText login_button;

    Login login_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup = (TextView)findViewById(R.id.signup);
        signin = (TextView)findViewById(R.id.signin);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);

        login_button = (MyRegularText)findViewById(R.id.buttonsignin);

        login_class = this;

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Login.this, SignUp.class);
                startActivity(it);
                login_class.finish();
            }
        });


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = getString(R.string.base_url) + "/api/v1/users/sign_in";

                JSONObject user_param = new JSONObject();
                JSONObject params = new JSONObject();
                try {
                    user_param.put("password", password.getText());
                    user_param.put("email", email.getText());
                    params.put("user",user_param);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Jsontest", params.toString());

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, params.toString(), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String auth_token = response.getJSONObject("user").getString("authentication_token");
                            String username = response.getJSONObject("user").getString("username");

                            SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.preferences_file), MODE_PRIVATE).edit();
                            editor.putString(getString(R.string.email_preferences_key), email.getText().toString());
                            editor.putString(getString(R.string.username_preferences_key), username);
                            editor.putString(getString(R.string.token_preferences_key), auth_token);
                            editor.commit();

                            Intent it = new Intent(Login.this, BusinessActivity.class);
                            startActivity(it);
                            login_class.finish();

                        }catch(Exception e)
                        {
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        MyRegularText login_error_message = (MyRegularText)findViewById(R.id.login_error_message);
                        login_error_message.setText("Error al iniciar sesión.");
                    }
                });
                Volley.newRequestQueue(Login.this).add(jsonRequest);
            }
        });
    }
}
