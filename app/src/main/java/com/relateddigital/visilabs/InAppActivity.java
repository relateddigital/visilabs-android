package com.relateddigital.visilabs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

public class InAppActivity extends AppCompatActivity {

    AlertDialog.Builder alertDialogBuilder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inapp);

        Button btnShowAlert = findViewById(R.id.btn_show_alert);
        Button btnShowActionSheet = findViewById(R.id.btn_show_action_sheet);
        Button btnShowSpinToWin = findViewById(R.id.btn_show_spin_to_win);

        alertDialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);


        btnShowAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialogBuilder.setMessage("text")
                        .setCancelable(false)
                        .setPositiveButton("button1", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //finish(); //TODO:
                                Toast.makeText(getApplicationContext(),"button1",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(),"close",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = alertDialogBuilder.create();
                alert.setTitle("title");
                alert.show();
            }
        });

        btnShowActionSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisilabsBottomSheetDialogFragment visilabsBottomSheetDialogFragment = VisilabsBottomSheetDialogFragment.newInstance();
                visilabsBottomSheetDialogFragment.show( getSupportFragmentManager(), "visilabs_dialog_fragment");
            }
        });

        btnShowSpinToWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewDialogFragment.display(getSupportFragmentManager(), "spintowin.html");
            }
        });
    }
}
