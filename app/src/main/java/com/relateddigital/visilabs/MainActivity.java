package com.relateddigital.visilabs;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.visilabs.android.Visilabs;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Visilabs.CreateAPI(getApplicationContext());

        Button btnTestPageView = findViewById(R.id.btn_page_view);

        btnTestPageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testPageView();
            }
        });
    }

    public void testPageView() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("token", "getInstanceId failed", task.getException());
                            return;
                        }

                        Visilabs.CreateAPI("x", "x", "http://lgr.visilabs.net",
                                "x", "http://rt.visilabs.net", "Android", getApplicationContext());

                        // Get new Instance ID token
                        String token = task.getResult().getToken();


                        HashMap<String, String> parameters = new HashMap<>();
                        parameters.put("OM.exVisitorID", "melifdmr");
                        parameters.put("OM.sys.TokenID", token);
                        parameters.put("OM.sys.AppID", "visilabs-android-sdk");
                        Visilabs.CallAPI().customEvent("android-visilab-test", parameters);                        // Log and toast
                    }
                });

    }
}
