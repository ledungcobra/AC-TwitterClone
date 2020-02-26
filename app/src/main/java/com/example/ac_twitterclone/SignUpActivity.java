package com.example.ac_twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail,edtPassword,edtUsername;
    private Button btnLogin,btnSignUp;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        edtEmail = findViewById(R.id.edtEmail_SignUp);
        edtPassword = findViewById(R.id.edtPassword_SignUp);
        edtUsername = findViewById(R.id.edtUsername_SignUp);

        btnLogin = findViewById(R.id.btnLogin_SignUp);
        btnSignUp = findViewById(R.id.btnSignUp_SignUp);

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        if(ParseUser.getCurrentUser()!=null){

            transitionToUserActivity();

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignUp_SignUp:

                final ParseUser parseUser = new ParseUser();
                if(edtUsername.getText().toString().equals("")||
                        edtPassword.getText().toString().equals("")||
                        edtEmail.getText().toString().equals("")
                ){

                    showToast("All field must be completed",FancyToast.WARNING);



                }else{

                    parseUser.setUsername(edtUsername.getText().toString());
                    parseUser.setPassword(edtPassword.getText().toString());
                    parseUser.setEmail(edtEmail.getText().toString());

                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){

                                showToast(parseUser.getUsername().toString()
                                        +"have been signed up",FancyToast.SUCCESS);
                                transitionToUserActivity();



                                    }else{

                                showToast(e.getMessage(),FancyToast.ERROR);


                            }
                        }
                    });

                }

                break;
            case R.id.btnLogin_SignUp:

                transitionToLoginActivity();

                break;

        }
    }

    private void showToast(String message, int type) {

        FancyToast
                .makeText(SignUpActivity.this
                        ,message
                        ,FancyToast.LENGTH_SHORT,type
                        ,false)
                .show();

    }
    private void transitionToUserActivity(){

        Intent intent = new Intent(this,TwitterUserActivity.class);

        startActivity(intent);
        finish();

    }

    private void transitionToLoginActivity(){

        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();

    }
}
