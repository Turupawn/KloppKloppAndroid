package klopp.klopp.klopp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import klopp.klopp.klopp.R;
import klopp.klopp.klopp.customfonts.MyRegularText;


public class Login extends AppCompatActivity {

    private TextView signup;
    private TextView signin;
    private TextView fb;
    private TextView account;
    private EditText email;
    private EditText password;
    MyRegularText login_error_message;

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
        login_error_message = (MyRegularText)findViewById(R.id.login_error_message);

        login_class = this;

        SharedPreferences prefs = getSharedPreferences(getString(R.string.preferences_file), MODE_PRIVATE);
        String email_pref = prefs.getString(getString(R.string.email_preferences_key), null);
        String token_pref = prefs.getString(getString(R.string.token_preferences_key), null);

        if(email_pref!=null && token_pref!=null)
        {
            Intent it = new Intent(Login.this, BusinessActivity.class);
            startActivity(it);
            login_class.finish();
        }

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

                login_error_message.setText("Iniciando sesión...");
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

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, params.toString(), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("status").equals("ok"))
                            {
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
                            }else
                            {
                                if(response.getString("error").equals("User does not exists"))
                                    login_error_message.setText(getString(R.string.incorrect_email_message));
                                else if(response.getString("error").equals("Incorrect password"))
                                    login_error_message.setText(R.string.incorrect_password_message);
                                else
                                    login_error_message.setText(response.getString("error"));
                            }

                        }catch(Exception e)
                        {
                            login_error_message.setText(R.string.login_error_message);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        login_error_message.setText(R.string.login_error_message);
                    }
                });
                Volley.newRequestQueue(Login.this).add(jsonRequest);
            }
        });
    }
}
