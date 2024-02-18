package com.relateddigital.visilabs;

import android.content.Context;
import android.os.Bundle;
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
import com.relateddigital.visilabs.model.*;
import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.api.VisilabsCallback;
import com.visilabs.api.VisilabsSearchRequest;
import com.visilabs.json.JSONArray;
import com.visilabs.json.JSONException;
import com.visilabs.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SearchActivity";

    Map<String, String> searchResults = new HashMap<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchBinding binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                searchResults.keySet().toArray(new String[0]));
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

        binding.autoCompleteTextView.setOnItemClickListener((parent, view1, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            Visilabs.CallAPI().trackSearchRecommendationClick(searchResults.get(selected));
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(view1.getApplicationWindowToken(), 0);
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


    private void updateAutoCompleteTextView(Map<String, String> searchResults) {
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                searchResults.keySet().toArray(new String[0]));
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

                searchResults = new HashMap<>();
                ArrayList<Product> products = new ArrayList<>();
                ArrayList<Category> categories = new ArrayList<>();
                ArrayList<Brand> brands = new ArrayList<>();
                ArrayList<PopularSearch> popularSearches = new ArrayList<>();

                try {
                    JSONObject jsonObject = response.getJson();

                    // ProductAreaContainer
                    JSONObject productAreaContainer = jsonObject.getJSONObject("ProductAreaContainer");
                    JSONArray productAreaContainerProducts = productAreaContainer.getJSONArray("Products");
                    String productAreaContainerTitle = productAreaContainer.getString("Title");
                    String productAreaContainerPreTitle = productAreaContainer.getString("PreTitle");
                    String productAreaContainerSearchResultMessage = productAreaContainer.getString("SearchResultMessage");
                    boolean productAreaContainerChangeTitle = productAreaContainer.getBoolean("ChangeTitle");
                    JSONObject productAreaContainerReport = productAreaContainer.getJSONObject("report");
                    String productAreaContainerReportClick = productAreaContainerReport.getString("click");

                    for (int i = 0; i < productAreaContainerProducts.length(); i++) {
                        JSONObject productObject = productAreaContainerProducts.getJSONObject(i);
                        Product product = createProductFromJsonObject(productObject);
                        products.add(product);
                        searchResults.put(product.name, productAreaContainerReportClick);
                    }


                    Log.d(LOG_TAG, "ProductAreaContainer Title: " + productAreaContainerTitle);
                    Log.d(LOG_TAG, "ProductAreaContainer PreTitle: " + productAreaContainerPreTitle);
                    Log.d(LOG_TAG, "ProductAreaContainer SearchResultMessage: " + productAreaContainerSearchResultMessage);
                    Log.d(LOG_TAG, "ProductAreaContainer ChangeTitle: " + productAreaContainerChangeTitle);
                    Log.d(LOG_TAG, "ProductAreaContainer Products: " + products);
                    Log.d(LOG_TAG, "ProductAreaContainer ReportClick: " + productAreaContainerReportClick);


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
                    JSONObject categoryContainerReport = categoryContainer.getJSONObject("report");
                    String categoryContainerReportClick = categoryContainerReport.getString("click");

                    Log.d(LOG_TAG, "CategoryContainer Title: " + categoryContainerTitle);
                    Log.d(LOG_TAG, "CategoryContainer IsActive: " + categoryContainerIsActive);
                    Log.d(LOG_TAG, "CategoryContainer Categories: " + categories);
                    Log.d(LOG_TAG, "CategoryContainer ReportClick: " + categoryContainerReportClick);


                    // BrandContainer
                    JSONObject brandContainer = jsonObject.getJSONObject("BrandContainer");
                    JSONArray brandContainerPopularBrands = brandContainer.getJSONArray("PopularBrands");
                    for (int i = 0; i < brandContainerPopularBrands.length(); i++) {
                        JSONObject brandObject = brandContainerPopularBrands.getJSONObject(i);
                        String brandName = brandObject.getString("Name");
                        String url = brandObject.getString("Url");
                        Brand brand = new Brand(brandName, url);
                        brands.add(brand);
                    }

                    String brandContainerTitle = brandContainer.getString("Title");
                    boolean brandContainerIsActive = brandContainer.getBoolean("IsActive");
                    JSONObject brandContainerReport = brandContainer.getJSONObject("report");
                    String brandContainerReportClick = brandContainerReport.getString("click");

                    Log.d(LOG_TAG, "BrandContainer Title: " + brandContainerTitle);
                    Log.d(LOG_TAG, "BrandContainer IsActive: " + brandContainerIsActive);
                    Log.d(LOG_TAG, "BrandContainer PopularBrands: " + brands);
                    Log.d(LOG_TAG, "BrandContainer ReportClick: " + brandContainerReportClick);


                    // SearchContainer
                    JSONObject searchContainer = jsonObject.getJSONObject("SearchContainer");
                    JSONArray searchContainerPopularBrands = searchContainer.getJSONArray("PopularSearches");
                    for (int i = 0; i < searchContainerPopularBrands.length(); i++) {
                        JSONObject searchObject = searchContainerPopularBrands.getJSONObject(i);
                        String name = searchObject.optString("Name");
                        String url = searchObject.optString("Url");
                        PopularSearch popularSearch = new PopularSearch(name, url);
                        popularSearches.add(popularSearch);
                    }

                    String searchContainerTitle = searchContainer.getString("Title");
                    boolean searchContainerIsActive = searchContainer.getBoolean("IsActive");
                    String searchContainerSearchUrlPrefix = searchContainer.getString("SearchUrlPrefix");
                    JSONObject searchContainerReport = searchContainer.getJSONObject("report");
                    String searchContainerReportClick = searchContainerReport.getString("click");

                    Log.d(LOG_TAG, "SearchContainer Title: " + searchContainerTitle);
                    Log.d(LOG_TAG, "SearchContainer IsActive: " + searchContainerIsActive);
                    Log.d(LOG_TAG, "SearchContainer SearchUrlPrefix: " + searchContainerSearchUrlPrefix);
                    Log.d(LOG_TAG, "SearchContainer PopularSearches: " + popularSearches);
                    Log.d(LOG_TAG, "SearchContainer ReportClick: " + searchContainerReportClick);



                    Log.d(LOG_TAG, "Suggestions: " + searchResults);
                    updateAutoCompleteTextView(searchResults);

                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }
            }

            @Override
            public void fail(VisilabsResponse response) {
                Log.e(LOG_TAG, response.getErrorMessage());
                ArrayList<String> suggestions = new ArrayList<>();
                suggestions.add("ERROR");
                updateAutoCompleteTextView(searchResults);
            }
        };
    }


}