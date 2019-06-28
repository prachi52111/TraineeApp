package com.example.prachisdemo.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {
    EditText email, firstname, lastname, gender;
    private RadioGroup radioGroup;
    DatabaseHelper databaseHelper;
    RadioButton  rdbMale,rdbFemale;
    private ImageView ivPhoto;
    private Uri selectedImage;
    private String TAG = "ProfileActivity";
    private final int PICK_IMAGE_CAMERA = 0, PICK_IMAGE_GALLERY = 1, REQUEST_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firstname = findViewById(R.id.edt_fname);
        lastname = findViewById(R.id.edt_lname);
        email = findViewById(R.id.edt_email);
        radioGroup = findViewById(R.id.rdg_gender);
        rdbMale= findViewById(R.id.rdb_male);
        rdbFemale= findViewById(R.id.rdb_male);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOpenImage();
            }
        });

        databaseHelper = new DatabaseHelper(this);


        ivPhoto = (ImageView) findViewById(R.id.iv_photo);
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOpenImage();
            }
        });




        Button btn_update = findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Fname = firstname.getText().toString();
                final String Lname = lastname.getText().toString();
                final String Email = email.getText().toString();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);
                final String gender = radioButton.getText().toString();
                Bitmap imageBtimap = ((BitmapDrawable) ivPhoto.getDrawable()).getBitmap();

                if (!isValidFirstname(Fname)) {
                    firstname.setError("Invalid Firstname");
                } else if (!isValidLastname(Lname)) {
                    lastname.setError("Invalid Lastname");
                } else {
                    User user = new User();
                    user.setFirstName(Fname);
                    user.setLastName(Lname);
                    user.setEmail(Email);
                    user.setGender(gender);
                    user.setPhoto(imageBtimap);
                    databaseHelper.updateUser(user);


                    Intent intent = new Intent(ProfileActivity.this, NavigationDrawer.class);
                    startActivity(intent);
                    Toast.makeText(ProfileActivity.this, "Successfully Update", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setDefaultProfile();

    }

    private void setDefaultProfile() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        String mEmail = sharedPreferences.getString("email", "");
        Log.e("email", mEmail);
        User profileList = databaseHelper.getLoggedInUserDetails(mEmail);
        firstname.setText(profileList.getFirstName());
        lastname.setText(profileList.getLastName());
        email.setText(mEmail);
        ivPhoto.setImageBitmap(profileList.getPhoto());
        if(profileList.getGender().equalsIgnoreCase("male"))
            rdbMale.setChecked(true);
        else
            rdbFemale.setChecked(true);
    }

    /**
     * \
     * Checks runtime permission for API > 23.
     */
    public void onOpenImage() {
        if (Build.VERSION.SDK_INT < 23) {
            selectImage();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission
                    .CAMERA) == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest
                        .permission.CAMERA, Manifest
                        .permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }
    }

    /**
     * \
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            switch (requestCode) {
                case REQUEST_CODE:
                    goWithCameraPermission(grantResults);
                    break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * \
     * Request for Camera permission.
     *
     * @param grantResults
     */
    private void goWithCameraPermission(int[] grantResults) {
        if (grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else if (grantResults[1] == PackageManager.PERMISSION_DENIED ||
                grantResults[0] == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest
                    .permission.WRITE_EXTERNAL_STORAGE, Manifest
                    .permission.CAMERA}, REQUEST_CODE);
        }
    }

    /**
     * Opens PickImageDialog to choose,
     * Camera or Gallery
     */
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
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
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
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


    /**
     * saving image as bitmap in imageview.
     *
     * @param bitmap
     */
    private void setImageData(Bitmap bitmap) {
        try {
            if (bitmap != null) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
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


}