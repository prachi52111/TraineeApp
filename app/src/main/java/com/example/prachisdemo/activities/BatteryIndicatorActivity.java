package com.example.prachisdemo.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prachisdemo.R;
import com.google.firebase.analytics.FirebaseAnalytics;

public class BatteryIndicatorActivity extends Activity {
	private static final String USER_PROPERTY = "favorite_food";
	private FirebaseAnalytics mFirebaseAnalytics;
	private TextView mTextView;

        //Create Broadcast Receiver Object along with class definition
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
          //When Event is published, onReceive method is called
		public void onReceive(Context c, Intent i) {
              //Get Battery %
			int level = i.getIntExtra("level", 0);
              //Find the progressbar creating in main.xml
			ProgressBar pb = (ProgressBar) findViewById(R.id.progressbar);
              //Set progress level with battery % value
			pb.setProgress(level);
              //Find textview control created in main.xml
			TextView tv = (TextView) findViewById(R.id.textfield);
              //Set TextView with text
			tv.setText("Battery Level: " + Integer.toString(level) + "%");
		}

	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
           //Set layout we created
		setContentView(R.layout.activity_battery_indicator);
           //Register the receiver which triggers event
           //when battery charge is changed
		registerReceiver(mBatInfoReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));

//analytic code

		mTextView = findViewById(R.id.textview);

		mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
		mFirebaseAnalytics.setUserProperty("favorite_food", "Pizza");
		mTextView.setText(String.format("UserProperty: %s", USER_PROPERTY));
	}

	@Override
	protected void onResume() {
		super.onResume();
		// screen name must be <= 36 characters
		mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);
	}

	public void sendPredefineEvent(View view) {
		Bundle bundle = new Bundle();
		bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "12345");
		bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Nougat");
		bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Image");
		bundle.putString(FirebaseAnalytics.Param.CURRENCY, "THB");
		bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, "111");
		bundle.putString(FirebaseAnalytics.Param.VALUE, "300");
		mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
		mTextView.setText(R.string.sent_predefine);
	}

	public void sendCustomEvent(View view) {
		Bundle params = new Bundle();
		params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Image");

		// custom parameter must be <= 24 characters
		// custom value must be <= 36 characters
		params.putString("image_name", "android.png");
		params.putString("full_text", "Android 7.0 Nougat");

		// custom event must be <= 32 characters
		mFirebaseAnalytics.logEvent("share_image", params);
		mTextView.setText(R.string.sent_custom);
	}
}