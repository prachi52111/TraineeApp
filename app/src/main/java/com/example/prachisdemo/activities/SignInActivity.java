package com.example.prachisdemo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.prachisdemo.R;
import com.example.prachisdemo.database.DatabaseHelper;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private DatabaseHelper databaseHelper;
    private String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        crashInit();

        email = findViewById(R.id.edt_email);
        password = findViewById(R.id.edt_pass);
        email.setText("prachi.gabani@gmail.com");
        password.setText("123123");

        databaseHelper = new DatabaseHelper(this);

        TextView txt_sign_up = findViewById(R.id.txt_sign_up);
        txt_sign_up.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });


        Button btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_sign_in.setOnClickListener(v -> {
            String Email = email.getText().toString();
            String Password = password.getText().toString();
            printToken();
            Crashlytics.log(Log.ERROR, TAG, "SignIn button clicked.");
            if (!isValidEmail(Email)) {
                email.setError("Invalid Email");
            } else if (!isValidPassword(Password)) {
                password.setError("Invalid Password");
            } else {
                if (databaseHelper.checkUser(Email, Password)) {
                    isUserLoggedIn(Email, true);
                    Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignInActivity.this, "Invalid email or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.edt_email).setOnFocusChangeListener((v, hasFocus) -> {
            if (!isValidEmail(email.getText().toString())) {
                email.setError("Invalid Email");
            }
        });

        TextView txt_forgot_pass = findViewById(R.id.txt_forgot);
        txt_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActiivity.class);
                startActivity(intent);
            }
        });
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 6) {
            return true;
        }
        return false;
    }

    private void isUserLoggedIn(String email, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putBoolean("is_user_login", value);
        editor.apply();
    }

    private void printToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("getInstanceId failed", task.getException().toString());
                        return;
                    }
                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    Log.e("Token--------", token);
                });
    }

    private void crashInit(){
        Crashlytics.log(Log.ERROR, TAG, "onCreate");
        Crashlytics.setInt("MeaningOfLife", 42);
        Crashlytics.setString("LastUIAction", "Test value");
        Crashlytics.setUserIdentifier("123456789");
        Crashlytics.logException(new Exception("Non-fatal exception: something went wrong!"));
    }
}