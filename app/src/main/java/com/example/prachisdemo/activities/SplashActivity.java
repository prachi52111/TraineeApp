package com.example.prachisdemo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.os.Bundle;

import com.example.prachisdemo.R;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int secondsDelayed = 3;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (isuserlogin()) {
                    startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                    finish();
                }
            }
        }, secondsDelayed * 1000);
    }

    private boolean isuserlogin() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean isLogin = sharedPreferences.getBoolean("is_user_login", false);
        return isLogin;
    }
}

