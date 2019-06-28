package com.example.prachisdemo.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.prachisdemo.R;
import com.example.prachisdemo.database.DatabaseHelper;
import com.example.prachisdemo.models.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText password;
    private EditText confirmpassword;
    private DatabaseHelper databaseHelper;
    private RadioGroup radioGroup;

    private ImageView ivPhoto;
    private DatabaseHelper dbManager;
    private Uri selectedImage;
    private Bitmap photoBitmap;
    private String TAG = "SignupActivity";
    private final int PICK_IMAGE_CAMERA = 0, PICK_IMAGE_GALLERY = 1;

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
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);
        StrictMode.VmPolicy.Builder newbuilder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(newbuilder.build());
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectPhoto(v);
            }
        });
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
                 String Fname = firstname.getText().toString();
                 String Lname = lastname.getText().toString();
                 String Email = email.getText().toString();
                 String Password = password.getText().toString();
                 String Conpassword = confirmpassword.getText().toString();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                 String gender = radioButton.getText().toString();
                photoBitmap = ((BitmapDrawable) ivPhoto.getDrawable()).getBitmap();

                if (!isValidFirstname(Fname)) {
                    firstname.setError("Invalid Firstname");
                } else if (!isValidLastname(Lname)) {
                    lastname.setError("Invalid Lastname");
                } else if (!isValidEmail(Email)) {
                    email.setError("Invalid Email");
                } else if (!isValidPassword(Password)) {
                    password.setError("password must be 5 character long.");
                } else if (!isValidConfirmpassword(Password, Conpassword)) {
                    confirmpassword.setError("password and confirm password not match.");
                } else {
                    if (databaseHelper.checkUser(Email)) {
                        Toast.makeText(SignUpActivity.this, "email already exist", Toast.LENGTH_SHORT).show();
                    } else {
                        User user = new User();
                        user.setFirstName(Fname);
                        user.setLastName(Lname);
                        user.setGender(gender);
                        user.setEmail(Email);
                        user.setPassword(Password);
                        user.setPhoto(photoBitmap);
                        databaseHelper.addUser(user);

                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                        startActivity(intent);
                        Toast.makeText(SignUpActivity.this, "successfully SignUp", Toast.LENGTH_SHORT).show();

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

    /**
     * \
     * Checks runtime permission for API > 23.
     *
     * @param view
     */
    public void onClickSelectPhoto(View view) {
        if (Build.VERSION.SDK_INT < 23) {
            selectImage();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission
                    .CAMERA) == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                ActivityCompat.requestPermissions(SignUpActivity.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            switch (requestCode) {
                case 1:
                    goWithCameraPermission(grantResults);
                    break;
                default:
                    break;
            }
        }
    }


    private void goWithCameraPermission(int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(SignUpActivity.this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    /**
     * Opens PickImageDialog to choose,
     * Camera or Gallery
     */
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    dialog.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    selectedImage = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "image_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, selectedImage);
                    startActivityForResult(intent, PICK_IMAGE_CAMERA);
                } else if (options[item].equals("Choose From Gallery")) {
                    dialog.dismiss();
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(pickPhoto, "Compelete action using"),
                            PICK_IMAGE_GALLERY);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case PICK_IMAGE_CAMERA:
                try {
                    if (selectedImage != null) {
                        Bitmap photoBitmap = BitmapFactory.decodeFile(selectedImage.getPath());
                        setImageData(photoBitmap);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "PICK_FROM_CAMERA" + e);
                }
                break;

            case PICK_IMAGE_GALLERY:
                try {
                    if (resultCode == RESULT_OK) {
                        Uri uri = imageReturnedIntent.getData();
                        if (uri != null) {
                            Bitmap bitmap = null;
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            setImageData(bitmap);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "PICK_FROM_GALLERY" + e);
                }
                break;
        }
    }

    private void setImageData(Bitmap bitmap) {
        try {
            if (bitmap != null) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                Glide.with(this)
                        .load(out.toByteArray())
                        .asBitmap()
                        .into(ivPhoto);
            } else {
                Toast.makeText(this, "Unable to select image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "setImageData" + e);
        }
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
