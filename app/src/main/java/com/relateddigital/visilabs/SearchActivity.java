package com.relateddigital.visilabs;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.relateddigital.visilabs.databinding.ActivitySearchBinding;
import com.relateddigital.visilabs.model.Product;
import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.api.VisilabsCallback;
import com.visilabs.api.VisilabsFavsRequestCallback;
import com.visilabs.api.VisilabsSearchRequest;
import com.visilabs.favs.FavsResponse;
import com.visilabs.inApp.VisilabsActionRequest;
import com.visilabs.json.JSONArray;
import com.visilabs.json.JSONObject;
import com.visilabs.util.VisilabsConstant;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SearchActivity";

    private ActivitySearchBinding binding;

    private final Handler handler = new Handler();
    private Runnable fetchSuggestionsRunnable;

    List<String> suggestions = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
        binding.autoCompleteTextView.setAdapter(adapter);
        binding.autoCompleteTextView.setThreshold(0);
        adapter.notifyDataSetChanged();

        binding.autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fetchSuggestions(s.toString());
                //handler.removeCallbacks(fetchSuggestionsRunnable);
                //fetchSuggestionsRunnable = () -> fetchSuggestions(s.toString());
                //handler.postDelayed(fetchSuggestionsRunnable, 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
            }

        });

    }

    private void fetchSuggestions(String query) {

        try {
            VisilabsSearchRequest searchRequest = Visilabs.CallAPI().buildSearchRecommendationRequest(query, "web");
            searchRequest.executeAsync(getVisilabsCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void updateAutoCompleteTextView(List<String> suggestions) {
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
        autoCompleteTextView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private VisilabsCallback getVisilabsCallback() {

        return new VisilabsCallback() {
            @Override
            public void success(VisilabsResponse response) {
                try {
                    JSONObject jsonObject = response.getJson();
                    JSONObject productAreaContainer = jsonObject.getJSONObject("ProductAreaContainer");
                    String productAreaContainerTitle = productAreaContainer.getString("Title");
                    JSONArray productAreaContainerProducts = productAreaContainer.getJSONArray("Products");
                    ArrayList<Product> products = new ArrayList<>();
                    ArrayList<String> suggestions = new ArrayList<>();
                    for (int i = 0; i < productAreaContainerProducts.length(); i++) {
                        JSONObject productObject = productAreaContainerProducts.getJSONObject(i);
                        String currentProductTitle = productObject.getString("Name");
                        String currentProductUrl = productObject.getString("Url");
                        String currentProductImageUrl = productObject.getString("ImageUrl");
                        String currentProductBrandName = productObject.getString("BrandName");
                        double currentProductPrice = productObject.getDouble("Price");
                        double currentProductDiscountPrice = productObject.getDouble("DiscountPrice");
                        String currentProductCode = productObject.getString("Code");
                        String currentProductCurrency = productObject.getString("Currency");
                        String currentProductDiscountCurrency = productObject.getString("DiscountCurrency");
                        products.add(new Product(currentProductTitle, currentProductUrl, currentProductImageUrl, currentProductBrandName, currentProductPrice, currentProductDiscountPrice, currentProductCode, currentProductCurrency, currentProductDiscountCurrency));
                        suggestions.add(currentProductTitle);
                    }
                    Log.d(LOG_TAG, "ProductAreaContainer Title: " + productAreaContainerTitle);
                    Log.d(LOG_TAG, "ProductAreaContainer Products: " + products);
                    Log.d(LOG_TAG, "ProductAreaContainer Suggestions: " + suggestions);
                    updateAutoCompleteTextView(suggestions);
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }
            }

            @Override
            public void fail(VisilabsResponse response) {
                Log.e(LOG_TAG, response.getErrorMessage());
                ArrayList<String> suggestions = new ArrayList<>();
                suggestions.add("ERROR");
                updateAutoCompleteTextView(suggestions);
            }
        };
    }


}