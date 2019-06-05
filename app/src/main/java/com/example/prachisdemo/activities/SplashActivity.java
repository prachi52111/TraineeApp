package com.example.prachisdemo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.prachisdemo.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int secondsDelayed = 3;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (isuserlogin()) {
                    startActivity(new Intent(SplashActivity.this, NavigationDrawer.class));
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

