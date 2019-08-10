package com.example.prachisdemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DialogActivity extends AppCompatActivity {

    private Button btn_dialogcustom, btn_dialogalter;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        btn_dialogalter = findViewById(R.id.btn_dialogalter);
        btn_dialogalter.setOnClickListener(v -> alertDialog());

        btn_dialogcustom = findViewById(R.id.btn_dialogcustom);
        btn_dialogcustom.setOnClickListener(v -> Customdialog());


    }

    private void Customdialog() {
        // create a Dialog component
        final Dialog dialog = new Dialog(context);

        //tell the Dialog to use the dialog.xml as it's layout description
        dialog.setContentView(R.layout.activity_dialog__layout_);
        dialog.setTitle("Android Custom Dialog Box");

        TextView txt = (TextView) dialog.findViewById(R.id.txt_dialog);
        txt.setText("This is an Android custom Dialog Box Example! Enjoy!");

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButton);

        dialogButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this item?").setTitle("Delete");

        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to close this application ?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    finish();
                    Toast.makeText(getApplicationContext(), "you choose yes action for alertbox",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, id) -> {
                    //  Action for 'NO' Button
                    dialog.cancel();
                    Toast.makeText(getApplicationContext(), "you choose no action for alertbox",
                            Toast.LENGTH_SHORT).show();
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("AlertDialogExample");
        alert.show();
    }


}
