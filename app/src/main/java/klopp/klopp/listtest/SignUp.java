package klopp.klopp.listtest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SignUp extends MainActivity {


    private TextView signup;
    private TextView signin;
    private TextView fb;
    private TextView account;
    private EditText email;
    private EditText password;
    private EditText user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        signup = (TextView)findViewById(R.id.signup);
        signin = (TextView)findViewById(R.id.signin);
        fb = (TextView)findViewById(R.id.fb);
        account = (TextView)findViewById(R.id.account);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        user = (EditText)findViewById(R.id.user);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(SignUp.this, Login.class);
                startActivity(it);
            }
        });
    }
}
