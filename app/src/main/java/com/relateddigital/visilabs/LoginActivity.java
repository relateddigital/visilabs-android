package com.relateddigital.visilabs;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.relateddigital.visilabs.databinding.ActivityLoginBinding;
import com.visilabs.Visilabs;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private String exVisitor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        exVisitor = Math.random() + "test@gmail.com";
        binding.tvExvisitorId.setText(exVisitor);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("token", "getInstanceId failed", task.getException());
                                    return;
                                }
                                String token = task.getResult();
                                HashMap<String, String> parameters = new HashMap<>();
                                parameters.put("OM.exVisitorID", "test9876@euromsg.com");
                                parameters.put("OM.sys.TokenID", token);
                                parameters.put("OM.sys.AppID", "visilabs-android-test");
                                Visilabs.CallAPI().customEvent("Login", parameters, LoginActivity.this);

                                Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().clear().apply();

                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("OM.sys.AppID", "visilabs-android-test");
                Visilabs.CallAPI().customEvent("Logout", parameters);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_LONG).show();

            }
        });
    }
}
