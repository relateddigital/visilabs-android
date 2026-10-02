package com.relateddigital.visilabs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.relateddigital.visilabs.databinding.ActivityLoginBinding;
import com.visilabs.Visilabs;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                                parameters.put("OM.sys.TokenID", token);
                                parameters.put("OM.sys.AppID", "visilabs-android-test");
                                Visilabs.CallAPI().login("test9876@euromsg.com", parameters, LoginActivity.this);

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

        setupCaptureTests();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCaptureIds();
    }

    // region Capture

    private enum CaptureTest {
        VIEW_ITEM("capture: rmc_view_item"),
        ADD_TO_CART("capture: rmc_add_to_cart"),
        PURCHASE("capture: rmc_purchase (nested items)"),
        LOGIN_AND_CAPTURE("login + capture: rmc_login"),
        ALL_PROPERTY_TYPES("capture: all property types"),
        SET_PERSON_PROPERTIES("setPersonProperties (email)"),
        BATCH("capture: 25 events (batch)"),
        FLUSH("flushCapture"),
        LOGOUT("logout (new anonymous id)");

        final String title;

        CaptureTest(String title) {
            this.title = title;
        }
    }

    private void setupCaptureTests() {
        for (final CaptureTest test : CaptureTest.values()) {
            Button button = new Button(this);
            button.setText(test.title);
            button.setAllCaps(false);
            button.setOnClickListener(v -> runCaptureTest(test));
            binding.llCaptureButtons.addView(button, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        // Mevcut Login / Logout butonlarından sonra güncellemek için dokunmak yeterli.
        binding.tvCaptureIds.setOnClickListener(v -> refreshCaptureIds());
        refreshCaptureIds();
    }

    private void runCaptureTest(CaptureTest test) {
        switch (test) {
            case VIEW_ITEM: {
                Map<String, Object> properties = new HashMap<>();
                properties.put("item_id", 1001);
                properties.put("item_name", "Shoei NXR2");
                properties.put("item_brand", "Shoei");
                properties.put("item_category", "Motor Kaskları");
                properties.put("item_variant", "Mat Siyah / L");
                properties.put("original_price", 2000);
                properties.put("discounted_price", 1500);
                properties.put("discount_rate", 20);
                properties.put("currency", "TRY");
                properties.put("cart_price", 1000);
                properties.put("product_rating", 4.8);
                properties.put("review_count", 126);
                properties.put("stock_quantity", 3);
                properties.put("stock_status", "low_stock");
                properties.put("list_source", "search");
                properties.put("search_term", "kask");
                properties.put("product_campaign", "Summer Sale");
                capture("rmc_view_item", properties);
                break;
            }
            case ADD_TO_CART: {
                Map<String, Object> properties = new HashMap<>();
                properties.put("item_id", 1001);
                properties.put("item_name", "Shoei NXR2");
                properties.put("quantity", 1);
                properties.put("price", 1500);
                properties.put("currency", "TRY");
                properties.put("cart_total", 2500);
                capture("rmc_add_to_cart", properties);
                break;
            }
            case PURCHASE: {
                Map<String, Object> item1 = new HashMap<>();
                item1.put("item_id", 1001);
                item1.put("quantity", 1);
                item1.put("price", 1500);
                Map<String, Object> item2 = new HashMap<>();
                item2.put("item_id", 2002);
                item2.put("quantity", 2);
                item2.put("price", 625.25);
                List<Map<String, Object>> items = Arrays.asList(item1, item2);

                Map<String, Object> properties = new HashMap<>();
                properties.put("order_id", "ORD-" + (1000 + (int) (Math.random() * 9000)));
                properties.put("revenue", 2750.5);
                properties.put("currency", "TRY");
                properties.put("items", items);
                capture("rmc_purchase", properties);
                break;
            }
            case LOGIN_AND_CAPTURE: {
                String exVisitorId = binding.tvExvisitorId.getText().toString().trim();
                if (exVisitorId.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "exVisitorId can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                // login, capture tarafında $identify gönderir (anonim id + exVisitorId birleşir);
                // ardından gelen rmc_login exVisitorId ile gider.
                Visilabs.CallAPI().login(exVisitorId, new HashMap<>());
                Map<String, Object> properties = new HashMap<>();
                properties.put("method", "email");
                capture("rmc_login", properties);
                break;
            }
            case ALL_PROPERTY_TYPES: {
                Map<String, Object> nested = new HashMap<>();
                nested.put("nested", true);
                Map<String, Object> properties = new HashMap<>();
                properties.put("string", "text");
                properties.put("int", 42);
                properties.put("double", 3.14);
                properties.put("bool", true);
                properties.put("date", new Date());
                properties.put("url", Uri.parse("https://www.relateddigital.com"));
                properties.put("list", Arrays.asList(1, "two", 3.0));
                properties.put("map", nested);
                properties.put("nan", Double.NaN); // gönderilmez, logcat'e uyarı düşer
                capture("rmc_property_types", properties);
                break;
            }
            case SET_PERSON_PROPERTIES: {
                String email = binding.etCaptureEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "email can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> set = new HashMap<>();
                set.put("email", email);
                Map<String, Object> setOnce = new HashMap<>();
                setOnce.put("first_seen_app", "VisilabsExample");
                Visilabs.CallAPI().setPersonProperties(set, setOnce);
                Toast.makeText(getApplicationContext(), "$set queued", Toast.LENGTH_SHORT).show();
                break;
            }
            case BATCH: {
                // 20. event'te kuyruk beklemeden gönderilir, kalan 5'i en geç 10 sn içinde gider.
                for (int i = 1; i <= 25; i++) {
                    Map<String, Object> properties = new HashMap<>();
                    properties.put("index", i);
                    Visilabs.CallAPI().capture("rmc_batch_test", properties);
                }
                Toast.makeText(getApplicationContext(), "25 events queued", Toast.LENGTH_SHORT).show();
                break;
            }
            case FLUSH:
                Visilabs.CallAPI().flushCapture();
                Toast.makeText(getApplicationContext(), "Capture queue flushed", Toast.LENGTH_SHORT).show();
                break;
            case LOGOUT:
                Visilabs.CallAPI().logout();
                Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                break;
        }
        refreshCaptureIds();
    }

    private void capture(String event, Map<String, Object> properties) {
        Visilabs.CallAPI().capture(event, properties);
        Toast.makeText(getApplicationContext(), event + " queued", Toast.LENGTH_SHORT).show();
    }

    private void refreshCaptureIds() {
        String distinctId = Visilabs.CallAPI().getDistinctId();
        String anonymousId = Visilabs.CallAPI().getAnonymousId();
        String state = distinctId.equals(anonymousId) ? "anonymous" : "identified";
        binding.tvCaptureIds.setText("distinct_id (" + state + "):\n" + distinctId
                + "\n\nanonymous_id:\n" + anonymousId
                + "\n\nTap to refresh. In debug builds request bodies are logged to logcat (tag: VisilabsCapture).");
    }

    // endregion
}
