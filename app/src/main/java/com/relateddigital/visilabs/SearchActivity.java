package com.relateddigital.visilabs;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.relateddigital.visilabs.databinding.ActivitySearchBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SearchActivity";

    private ActivitySearchBinding binding;

    private final Handler handler = new Handler();
    private Runnable fetchSuggestionsRunnable;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

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

    }

    private void fetchSuggestions(String query) {
        List<String> suggestions = new ArrayList<>();
        suggestions.add("suggestion 1");
        suggestions.add("suggestion 2");
        updateAutoCompleteTextView(suggestions);
        /*

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    List<String> suggestions = parseSuggestions(response);
                    updateAutoCompleteTextView(suggestions);
                },
                error -> {
                    Log.e("AutoComplete", "server error", error);
                });

        queue.add(stringRequest);
         */
    }

    /*
    private List<String> parseSuggestions(String response) {
        List<String> suggestions = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                suggestions.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return suggestions;
    }
*/

    private void updateAutoCompleteTextView(List<String> suggestions) {
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, suggestions);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(0);
    }
}