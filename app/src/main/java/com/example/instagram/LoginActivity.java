package com.example.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "Login";
    private EditText userName;
    private EditText passWord;
    private ImageView fromMeta;
    private ImageView instagramText;
    private ImageView instagramLogo;
    private Button loginBtn;
    private Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }
        userName = findViewById(R.id.userName);
        passWord = findViewById(R.id.passWord);
        fromMeta = findViewById(R.id.fromMeta);
        instagramLogo = findViewById(R.id.instagramLogo);
        instagramText = findViewById(R.id.instagramText);
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userName.getText().toString();
                String password = passWord.getText().toString();
                signUpUser(username, password);


            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userName.getText().toString();
                String password = passWord.getText().toString();
                loginUser(username, password);

            }
        });
    }

    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                // These toasts don't work
                if (e != null) {
                    Log.e(TAG, "Login failed");
                    Toast.makeText(LoginActivity.this, "Issue with login info!", Toast.LENGTH_SHORT).show();
                    return;
                }
                goMainActivity();
                Log.i(TAG, "Logged in!");
                Toast.makeText(LoginActivity.this, "Login was successful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, Feed.class);
        startActivity(i); // start activity with result, OnBackPressed, ImageView scale types *Study!
        finish();
    }

    private void signUpUser(String username, String password) {
        ParseUser newUser = new ParseUser();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    newUserSignedUpSuccessfully();
                } else {
                    newUserSignUpDidNotSucceed();
                }
            }
        });

    }

    private void newUserSignUpDidNotSucceed() {
        Toast.makeText(LoginActivity.this, "SignUp was unsuccessful", Toast.LENGTH_SHORT).show();

    }

    private void newUserSignedUpSuccessfully() {
        Toast.makeText(LoginActivity.this, "SignUp was successful", Toast.LENGTH_SHORT).show();
        userName.setText("");
        passWord.setText("");
    }
}
