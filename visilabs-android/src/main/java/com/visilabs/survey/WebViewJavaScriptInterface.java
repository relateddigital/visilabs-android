package com.visilabs.survey;

import android.util.Log;
import android.webkit.JavascriptInterface;


import com.google.gson.Gson;
import com.visilabs.Visilabs;
import com.visilabs.mailSub.Report;
import com.visilabs.survey.model.SurveyModel;
import com.visilabs.survey.model.SurveyResult;
import com.visilabs.survey.model.QuestionAnswer;

import java.util.HashMap;

public class WebViewJavaScriptInterface {
    private static final String TAG = "SurveyJSInterface";
    WebViewDialogFragment mWebViewDialogFragment;
    private SurveyCompleteInterface mListener;
    private String mResponse;
    private SurveyModel surveyModel;

    WebViewJavaScriptInterface(WebViewDialogFragment webViewDialogFragment, String response) {
        this.mWebViewDialogFragment = webViewDialogFragment;
        mResponse = response;
        surveyModel = new Gson().fromJson(mResponse, SurveyModel.class);
    }

    /**
     * This method closes SurveyActivity
     */
    @JavascriptInterface
    public void close() {
        this.mWebViewDialogFragment.dismiss();
        mListener.onCompleted();
    }

    /**
     * This method sends the report to the server
     */
    @JavascriptInterface
    public void sendReport(String surveyResultJson) {
        Log.d(TAG, "Anket Sonuçları Alındı: " + surveyResultJson);

        // Önce Visilabs tıklama raporunu gönderelim.
        try {
            Report report = new Report();
            // Not: surveyModel nesnesine bu metod içinde erişiminiz olmalı.
            // Eğer yoksa, onu da JavaScriptInterface'in constructor'ında almanız gerekir.
            report.setImpression(surveyModel.getActiondata().getReport().getImpression());
            report.setClick(surveyModel.getActiondata().getReport().getClick());
            Visilabs.CallAPI().trackActionClick(report);
        } catch (Exception e) {
            Log.e("Survey", "Rapor gönderilirken bir hata oluştu: " + e.getMessage());
        }

        // Şimdi, gelen cevapları işleyelim.
        Gson gson = new Gson();
        try {
            // 1. JavaScript'ten gelen JSON string'ini SurveyResult nesnesine çeviriyoruz.
            SurveyResult result = gson.fromJson(surveyResultJson, SurveyResult.class);

            // 2. Sorular listesinin boş olmadığından emin oluyoruz.
            if (result != null && result.getQuestions() != null && !result.getQuestions().isEmpty()) {

                // 3. FOR DÖNGÜSÜ: Her bir soru-cevap çifti için döngüye giriyoruz.
                for (QuestionAnswer qa : result.getQuestions()) {

                    // 4. Her döngüde yeni bir parametre haritası oluşturuyoruz.
                    HashMap<String, String> parameters = new HashMap<>();

                    // Dinamik parametreler
                    parameters.put("OM.s_group", result.getTitle()); // Anketin başlığı
                    parameters.put("OM.s_cat", qa.getQuestion());     // Mevcut sorunun metni
                    parameters.put("OM.s_page", qa.getAnswer());        // Mevcut soruya verilen cevap

                    // 5. Her soru için customEvent'i ayrı ayrı gönderiyoruz.
                    // Event adı olarak "survey-report" gibi daha açıklayıcı bir isim kullanmak daha iyidir.
                    Visilabs.CallAPI().customEvent("survey-report", parameters);

                    Log.d(TAG, "Custom Event Gönderildi: Soru='" + qa.getQuestion() + "', Cevap='" + qa.getAnswer() + "'");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Anket sonuçları işlenirken veya gönderilirken hata oluştu.", e);
        }
    }


    /**
     * This method gives the initial response to js side.
     *
     * @return String - string version of the json response got from the server
     */
    @JavascriptInterface
    public String getResponse() {
        return mResponse;
    }

    public void setSurveyListeners(SurveyCompleteInterface listener) {
        mListener = listener;
    }

}
