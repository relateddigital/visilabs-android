package com.relateddigital.visilabs;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.visilabs.Visilabs;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button btnGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGoToLogin = findViewById(R.id.btn_go_to_login);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("OM.sys.AppID", "visilabs-android-sdk");
        Visilabs.CallAPI().customEvent("android-visilab", parameters);

        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
