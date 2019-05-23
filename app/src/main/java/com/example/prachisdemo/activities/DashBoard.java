package com.example.prachisdemo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.prachisdemo.R;

public class DashBoard extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(DashBoard.this, SignInActivity.class);
                startActivity(intent);
                clearSharedPref();
                startActivity(intent);
                finish();
                return true;
            }
        });


        toastEmail();
    }


    private void toastEmail() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        String email = sharedPreferences.getString("email", "");
        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
    }

    private void clearSharedPref() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
