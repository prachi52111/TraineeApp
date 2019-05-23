package com.example.prachisdemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prachisdemo.R;
import com.example.prachisdemo.database.DatabaseHelper;
import com.example.prachisdemo.models.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText password;
    private EditText confirmpassword;
    TextView txtSignIn;
    TextView btnSignUp;
    private DatabaseHelper databaseHelper;
    private InputValidation inputValidation;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firstname = (EditText) findViewById(R.id.edt_fname);
        lastname = (EditText) findViewById(R.id.edt_lname);
        email = (EditText) findViewById(R.id.edt_email);
        password = (EditText) findViewById(R.id.edt_pass);
        confirmpassword = (EditText) findViewById(R.id.edt_con_pass);
        radioGroup = findViewById(R.id.rdg_gender);

        firstname.setText("Pracchi");
        lastname.setText("Gabani");
        email.setText("prachi.gabani@gmail.com");
        password.setText("123123");
        confirmpassword.setText("123123");

        databaseHelper = new DatabaseHelper(this);

        Button btn_sign_up = findViewById(R.id.btn_sign_up);
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Fname = firstname.getText().toString();
                final String Lname = lastname.getText().toString();
                final String Email = email.getText().toString();
                final String Password = password.getText().toString();
                final String Conpassword = confirmpassword.getText().toString();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                final String gender = radioButton.getText().toString();

                if (!isValidFirstname(Fname)) {
                    firstname.setError("Invalid Firstname");
                } else if (!isValidLastname(Lname)) {
                    lastname.setError("Invalid Lastname");
                } else if (!isValidEmail(Email)) {
                    email.setError("Invalid Email");
                } else if (!isValidPassword(Password)) {
                    password.setError("Invalid Password");
                } else if (!isValidConfirmpassword(Password, Conpassword)) {
                    confirmpassword.setError("Invalid Confirm password");
                } else {
                    if (databaseHelper.checkUser(Email)){
                        Toast.makeText(SignUpActivity.this, "email already exist", Toast.LENGTH_SHORT).show();
                    }else {
                        User user = new User();
                        user.setFirstName(Fname);
                        user.setLastName(Lname);
                        user.setGender(gender);
                        user.setEmail(Email);
                        user.setPassword(Password);
                        databaseHelper.addUser(user);

                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                        startActivity(intent);
                        Toast.makeText(SignUpActivity.this, "successfully SignUp", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(SignUpActivity.this, "performed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        TextView txt_sign_in = findViewById(R.id.txt_sign_in);
        txt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });


    }


    //validating Firstname
    private boolean isValidFirstname(String firstname) {
        return firstname.length() > 0;
    }

    //validating Lastname
    private boolean isValidLastname(String lastname) {
        return lastname.length() > 0;
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

    private boolean isValidConfirmpassword(String password, String conpassword) {
        if (conpassword.equals(password)) {
            return true;
        }
        return false;
    }
}
