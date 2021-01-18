package com.relateddigital.visilabs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import com.relateddigital.visilabs.databinding.ActivityInappBinding;

public class InAppActivity extends AppCompatActivity {

    private ActivityInappBinding binding;
    private AlertDialog.Builder alertDialogBuilder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInappBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        alertDialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);

        binding.btnShowAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialogBuilder.setMessage("text")
                        .setCancelable(false)
                        .setPositiveButton("button1", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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

        binding.btnShowActionSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisilabsBottomSheetDialogFragment visilabsBottomSheetDialogFragment = VisilabsBottomSheetDialogFragment.newInstance();
                visilabsBottomSheetDialogFragment.show( getSupportFragmentManager(), "visilabs_dialog_fragment");
            }
        });

        binding.btnShowSpinToWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewDialogFragment.display(getSupportFragmentManager(), "spintowin.html");
            }
        });
    }
}
