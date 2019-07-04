package com.example.prachisdemo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.prachisdemo.R;

import androidx.appcompat.app.AppCompatActivity;

public class MyServicesActivity extends AppCompatActivity implements View.OnClickListener{

    //button objects
    private Button buttonStart;
    private Button buttonStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service);

        //getting buttons from xml
        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);

        //attaching onclicklistener to buttons
        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonStart) {
            //start the service here
            startService(new Intent(this,MyService.class));
        } else if (view == buttonStop) {
            //stop the service here
            stopService(new Intent(this,MyService.class));
        }
    }
}
