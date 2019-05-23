package com.example.prachisdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {
    EditText username, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username = findViewById(R.id.edt_username);
        email = findViewById(R.id.edt_email);


        Button btn_update = findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Username = username.getText().toString();
                final String Email = email.getText().toString();

                if (!isValidUsername(Username)) {
                    username.setError("Invalid Username");
                } else if (!isValidEmail(Email)) {
                    email.setError("Invalid Emial");
                } else {
                    Intent intent = new Intent(ProfileActivity.this, NavigationDrawer.class);
                    startActivity(intent);
                }
            }
        });

    }


    //validating username
    private boolean isValidUsername(String uername) {
        return username.length() > 0;
    }


    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}