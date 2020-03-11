package com.relateddigital.visilabs;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.visilabs.android.Cookie;
import com.visilabs.android.Visilabs;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    String exVisitorId = "testUser";
    TextView tvToken, exvisitorIdTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Visilabs.CreateAPI("676D325830564761676D453D", "356467332F6533766975593D", "http://lgr.visilabs.net",
                "visistore", "http://rt.visilabs.net", "Android", getApplicationContext(),  "http://s.visilabs.net/json", "http://s.visilabs.net/actjson", 30000);

          Visilabs.CreateAPI(getApplicationContext());
        Visilabs.CallAPI().setCookie(new Cookie());

        Button btnTestPageView = findViewById(R.id.btn_page_view);
         exvisitorIdTv = findViewById(R.id.tv_exvisitor_id);
         tvToken = findViewById(R.id.tv_token);

        btnTestPageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testPageView();

                //Toast.makeText(getApplicationContext(), "Event will be sent to RMC", Toast.LENGTH_LONG).show();
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

                            tvToken.setText("Token Alınamadı");
                            return;
                        }
                          // Get new Instance ID token
                        String token = task.getResult().getToken();

                        tvToken.setText("Token :  " + token);
                        exvisitorIdTv.setText("Exvisitor Id : " + exVisitorId);

                        HashMap<String, String> parameters = new HashMap<>();
                        parameters.put("OM.exVisitorID", exVisitorId);
                        parameters.put("OM.sys.TokenID", token);
                        parameters.put("OM.sys.AppID", "visilabs-android");
                        Visilabs.CallAPI().customEvent("android-visilab-test", parameters, MainActivity.this);                        // Log and toast
                    }
                });

    }
}
