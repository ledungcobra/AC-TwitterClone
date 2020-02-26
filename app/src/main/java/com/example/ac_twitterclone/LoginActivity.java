package com.example.ac_twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUsername,edtPassword;
    private Button btnSignUp,btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Log in");

        edtUsername = findViewById(R.id.edtUsername_Login);
        edtPassword = findViewById(R.id.edtPassword_Login);

        btnLogin = findViewById(R.id.btnLogin_Login);
        btnSignUp = findViewById(R.id.btnSignUp_Login);

        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);






    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case  R.id.btnSignUp_Login:

                transitionToSignUpActivity();


                break;
            case R.id.btnLogin_Login:

                ParseUser.logInInBackground(edtUsername.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user !=null && e == null){

                            showToast(user.getUsername(),FancyToast.SUCCESS);
                            transitionToTwitterUserActivity();



                        }else{

                            showToast(e.getMessage(),FancyToast.ERROR);

                        }

                    }
                });

                break;

        }

    }

    private void transitionToSignUpActivity() {

        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
        finish();


    }

    private void transitionToTwitterUserActivity(){

        Intent intent = new Intent(this,TwitterUserActivity.class);
        startActivity(intent);
        finish();



    }

    private void showToast(String message, int type) {

        FancyToast
                .makeText(LoginActivity.this
                        ,message
                        ,FancyToast.LENGTH_SHORT,type
                        ,false)
                .show();

    }
}
