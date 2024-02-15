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
import com.relateddigital.visilabs.model.Category;
import com.relateddigital.visilabs.model.Product;
import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.api.VisilabsCallback;
import com.visilabs.api.VisilabsSearchRequest;
import com.visilabs.json.JSONArray;
import com.visilabs.json.JSONException;
import com.visilabs.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SearchActivity";

    private ActivitySearchBinding binding;

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


    private Product createProductFromJsonObject(JSONObject productObject) throws JSONException {
        String name = productObject.getString("Name");
        String url = productObject.getString("Url");
        String imageUrl = productObject.getString("ImageUrl");
        String brandName = productObject.getString("BrandName");
        double price = productObject.getDouble("Price");
        double discountPrice = productObject.getDouble("DiscountPrice");
        String code = productObject.getString("Code");
        String currency = productObject.getString("Currency");
        String discountCurrency = productObject.getString("DiscountCurrency");
        return new Product(name, url, imageUrl, brandName, price, discountPrice, code, currency, discountCurrency);
    }

    private VisilabsCallback getVisilabsCallback() {

        return new VisilabsCallback() {
            @Override
            public void success(VisilabsResponse response) {
                suggestions = new ArrayList<>();
                ArrayList<Product> products = new ArrayList<>();
                ArrayList<Category> categories = new ArrayList<>();
                try {
                    JSONObject jsonObject = response.getJson();

                    // ProductAreaContainer
                    JSONObject productAreaContainer = jsonObject.getJSONObject("ProductAreaContainer");
                    JSONArray productAreaContainerProducts = productAreaContainer.getJSONArray("Products");

                    for (int i = 0; i < productAreaContainerProducts.length(); i++) {
                        JSONObject productObject = productAreaContainerProducts.getJSONObject(i);
                        Product product = createProductFromJsonObject(productObject);
                        products.add(product);
                        suggestions.add(product.name);
                    }
                    String productAreaContainerTitle = productAreaContainer.getString("Title");
                    String productAreaContainerPreTitle = productAreaContainer.getString("PreTitle");
                    String productAreaContainerSearchResultMessage = productAreaContainer.getString("SearchResultMessage");
                    boolean productAreaContainerChangeTitle = productAreaContainer.getBoolean("ChangeTitle");
                    JSONObject productAreaContainerReport = productAreaContainer.getJSONObject("report");
                    String productAreaContainerReportClick = productAreaContainerReport.getString("click");


                    Log.d(LOG_TAG, "ProductAreaContainer Title: " + productAreaContainerTitle);
                    Log.d(LOG_TAG, "ProductAreaContainer PreTitle: " + productAreaContainerPreTitle);
                    Log.d(LOG_TAG, "ProductAreaContainer SearchResultMessage: " + productAreaContainerSearchResultMessage);
                    Log.d(LOG_TAG, "ProductAreaContainer ChangeTitle: " + productAreaContainerChangeTitle);
                    Log.d(LOG_TAG, "ProductAreaContainer Products: " + products);


                    // CategoryContainer
                    JSONObject categoryContainer = jsonObject.getJSONObject("CategoryContainer");
                    JSONArray categoryContainerPopularCategories = categoryContainer.getJSONArray("PopularCategories");
                    for (int i = 0; i < categoryContainerPopularCategories.length(); i++) {
                        JSONObject categoryObject = categoryContainerPopularCategories.getJSONObject(i);
                        JSONArray categoryProductsArray = categoryObject.getJSONArray("Products");
                        ArrayList<Product> categoryProducts = new ArrayList<>();

                        for (int j = 0; j < categoryProductsArray.length(); j++) {
                            JSONObject productObject = categoryProductsArray.getJSONObject(i);
                            Product product = createProductFromJsonObject(productObject);
                            categoryProducts.add(product);
                        }
                        String categoryName = categoryObject.getString("Name");
                        Category category = new Category(categoryName, categoryProducts);
                        categories.add(category);
                    }

                    String categoryContainerTitle = categoryContainer.getString("Title");
                    boolean categoryContainerIsActive = categoryContainer.getBoolean("IsActive");
                    Log.d(LOG_TAG, "CategoryContainer Title: " + categoryContainerTitle);
                    Log.d(LOG_TAG, "CategoryContainer IsActive: " + categoryContainerIsActive);
                    Log.d(LOG_TAG, "CategoryContainer Categories: " + categories);


                    // BrandContainer
                    JSONObject brandContainer = jsonObject.getJSONObject("BrandContainer");
                    JSONArray brandContainerPopularBrands = brandContainer.getJSONArray("PopularBrands");
                    for (int i = 0; i < brandContainerPopularBrands.length(); i++) {
                        JSONObject brandObject = brandContainerPopularBrands.getJSONObject(i);

                    }

                    String brandContainerTitle = brandContainer.getString("Title");
                    boolean brandContainerIsActive = brandContainer.getBoolean("IsActive");
                    Log.d(LOG_TAG, "BrandContainer Title: " + brandContainerTitle);
                    Log.d(LOG_TAG, "BrandContainer IsActive: " + brandContainerIsActive);






                    Log.d(LOG_TAG, "Suggestions: " + suggestions);
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