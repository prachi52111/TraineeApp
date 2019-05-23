package com.example.prachisdemo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.prachisdemo.R;
import com.example.prachisdemo.database.DatabaseHelper;
import com.example.prachisdemo.models.User;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {
    EditText email, firstname, lastname, gender;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firstname = findViewById(R.id.edt_fname);
        lastname = findViewById(R.id.edt_lname);
        email = findViewById(R.id.edt_email);
        databaseHelper = new DatabaseHelper(this);

        setDefaultProfile();

        Button btn_update = findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Fname = firstname.getText().toString();
                final String Lname = lastname.getText().toString();
                final String Email = email.getText().toString();

                if (!isValidFirstname(Fname)) {
                    firstname.setError("Invalid Firstname");
                } else if (!isValidLastname(Lname)) {
                    lastname.setError("Invalid Lastname");
                } else {
                    Intent intent = new Intent(ProfileActivity.this, DashBoard.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void setDefaultProfile() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        String mEmail = sharedPreferences.getString("email", "");
        Log.e("email", mEmail);
        User profileList = databaseHelper.getLoggedInUserDetails(mEmail);
        firstname.setText(profileList.getFirstName());
        lastname.setText(profileList.getLastName());
       // gender.setText(profileList.getGender());
//        email.setText(profileList.getEmail());
    }


    //validating Firstname
    private boolean isValidFirstname(String firstname) {
        return firstname.length() > 0;
    }

    //validating Lastname
    private boolean isValidLastname(String lastname) {
        return lastname.length() > 0;
    }


}