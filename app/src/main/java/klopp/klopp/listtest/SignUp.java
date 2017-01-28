package klopp.klopp.listtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class SignUp extends AppCompatActivity {


    private TextView signup;
    private TextView signin;
    private TextView fb;
    private TextView account;
    private EditText email;
    private EditText username;
    private EditText password;
    private EditText user;

    SignUp signup_class;

    MyRegularText signup_button;
    MyRegularText signup_error_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signup_class = this;

        signup = (TextView)findViewById(R.id.signup);
        signin = (TextView)findViewById(R.id.signin);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        user = (EditText)findViewById(R.id.user);
        signup_error_message = (MyRegularText) findViewById(R.id.login_error_message);

        signup_button = (MyRegularText)findViewById(R.id.buttonsignup);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(SignUp.this, Login.class);
                startActivity(it);
                signup_class.finish();
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = getString(R.string.base_url) + "/api/v1/users";

                JSONObject user_param = new JSONObject();
                JSONObject params = new JSONObject();
                try {
                    user_param.put("password", password.getText());
                    user_param.put("email", email.getText());
                    user_param.put("username", user.getText());
                    params.put("user", user_param);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Jsontest", params.toString());

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, params.toString(), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String auth_token = response.getJSONObject("data").getString("authentication_token");
                            String username = response.getJSONObject("data").getString("username");

                            SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.preferences_file), MODE_PRIVATE).edit();
                            editor.putString(getString(R.string.email_preferences_key), email.getText().toString());
                            editor.putString(getString(R.string.username_preferences_key), username);
                            editor.putString(getString(R.string.token_preferences_key), auth_token);
                            editor.commit();

                            Intent it = new Intent(SignUp.this, BusinessActivity.class);
                            startActivity(it);
                            signup_class.finish();

                        } catch (Exception e) {
                            e.printStackTrace();

                            signup_error_message.setText("Error al crear usuario.");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        signup_error_message.setText("Error al crear usuario.");
                    }
                });
                Volley.newRequestQueue(SignUp.this).add(jsonRequest);
            }
        });
    }
}
