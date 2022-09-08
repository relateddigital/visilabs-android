package com.visilabs.util;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersistentTargetManager {

    private static final String LOG_TAG = "PersistentTargetManager";
    static PersistentTargetManager singleton = null;
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Context mContext;

    public static PersistentTargetManager with(Context context) {
        if (singleton == null) {
            synchronized (Prefs.class) {
                if (singleton == null) {
                    singleton = new Builder(context).build();
                }
            }
        }
        return singleton;
    }

    PersistentTargetManager(Context context){
        mContext = context;
    }

    /**
     * Writes customEvent parameters to shared preferences.
     *
     * @param parameters customEvent Paramaters
     */
    public synchronized void saveParameters(final HashMap<String, String> parameters) {
        Date now = new Date();
        for (VisilabsParameter visilabsParameter: VisilabsConstant.VISILABS_PARAMETERS){
            String key = visilabsParameter.getKey();
            String storeKey = visilabsParameter.getStoreKey();
            List<String> relatedKeys = visilabsParameter.getRelatedKeys();
            int count = visilabsParameter.getCount();
            if(parameters.containsKey(key) && parameters.get(key) != null && !parameters.get(key).equals("")){
                String parameterValue = parameters.get(key).trim();
                if(count == 1){
                    if(relatedKeys != null && relatedKeys.size() > 0){
                        String parameterValueToStore = parameterValue;
                        String relatedKey = relatedKeys.get(0);
//                        Prefs.getFromPrefs(mContext, VisilabsConfig.TARGET_PREF, storeKey, null);
                        if(parameters.containsKey(relatedKey) && parameters.get(relatedKey) != null){
                            parameterValueToStore = parameterValueToStore + "|" + parameters.get(relatedKey).trim();
                        }else{
                            parameterValueToStore = parameterValueToStore + "|" + "0";
                        }
                        parameterValueToStore = parameterValueToStore + "|" + sdf.format(now);
                        //Prefs.saveToPrefs(mContext, VisilabsConfig.TARGET_PREF , storeKey, VisilabsEncoder.encode(parameterValueToStore));
                        Prefs.saveToPrefs(mContext, VisilabsConstant.TARGET_PREF , storeKey, parameterValueToStore);
                    }else{
                        //Prefs.saveToPrefs(mContext, VisilabsConfig.TARGET_PREF , storeKey, VisilabsEncoder.encode(parameterValue));
                        Prefs.saveToPrefs(mContext, VisilabsConstant.TARGET_PREF , storeKey, parameterValue);
                    }
                }else if(count > 1){
                    String previousParameterValue = Prefs.getFromPrefs(mContext, VisilabsConstant.TARGET_PREF, storeKey, null);
                    StringBuilder parameterValueToStore = new StringBuilder(parameterValue + "|" + sdf.format(now));
                    if(previousParameterValue != null && previousParameterValue.length() > 0) {
                        //String decodedPreviousParameterValue = VisilabsEncoder.decode(previousParameterValue);
                        //String[] decodedPreviousParameterValueParts = decodedPreviousParameterValue.split("~");
                        String[] previousParameterValueParts = previousParameterValue.split("~");
                        int paramCounter = 1;
                        for (int i = 0; i < previousParameterValueParts.length ; i++){
                            if(paramCounter == 10){
                                break;
                            }
                            String decodedPreviousParameterValuePart =previousParameterValueParts[i];
                            if(decodedPreviousParameterValuePart.split("\\|").length == 2){
                                if(decodedPreviousParameterValuePart.split("\\|")[0].equals(parameterValue)) {
                                    continue;
                                }
                                parameterValueToStore.append("~").append(decodedPreviousParameterValuePart);
                                paramCounter++;
                            }
                        }
                    }
                    //Prefs.saveToPrefs(mContext, VisilabsConfig.TARGET_PREF , storeKey, VisilabsEncoder.encode(parameterValueToStore));
                    Prefs.saveToPrefs(mContext, VisilabsConstant.TARGET_PREF , storeKey, parameterValueToStore.toString());
                }
            }
        }
    }

    public HashMap<String, String> getParameters() {
        HashMap<String, String> parameters = new HashMap<>();
        for (VisilabsParameter visilabsParameter: VisilabsConstant.VISILABS_PARAMETERS) {
            String storeKey = visilabsParameter.getStoreKey();
            String value = Prefs.getFromPrefs(mContext, VisilabsConstant.TARGET_PREF, storeKey, null);
            if(value != null && !value.equals("")){
                parameters.put(storeKey, value);
            }
        }
        return parameters;
    }

    public void clearParameters(){
        for (VisilabsParameter visilabsParameter: VisilabsConstant.VISILABS_PARAMETERS) {
            Prefs.removeFromPrefs(mContext, VisilabsConstant.TARGET_PREF, visilabsParameter.getStoreKey());
        }
        VisilabsLog.i(LOG_TAG, "Parameters cleared.");
    }

    public synchronized void saveShownStory(final String actId, final String title) {
        Map<String, List<String>> shownStories = getShownStories();
        if(!shownStories.containsKey(actId)) {
            shownStories.put(actId, new ArrayList<String>());
        }
        if(!shownStories.get(actId).contains(title)){
            shownStories.get(actId).add(title);
        }
        Prefs.saveToPrefs(mContext, VisilabsConstant.SHOWN_STORIES_PREF,  VisilabsConstant.SHOWN_STORIES_PREF_KEY, new GsonBuilder().create().toJson(shownStories));
    }

    public synchronized Map<String, List<String>> getShownStories() {
        Map<String, List<String>> shownStories = new HashMap<String, List<String>>();
        String shownStoriesJson = Prefs.getFromPrefs(mContext, VisilabsConstant.SHOWN_STORIES_PREF,  VisilabsConstant.SHOWN_STORIES_PREF_KEY, "");
        if (!shownStoriesJson.isEmpty()) {
            Gson gson = new Gson();
            Type shownStoriesType = new TypeToken<Map<String, List<String>>>() {}.getType();
            shownStories = gson.fromJson(shownStoriesJson, shownStoriesType);
        }
        return shownStories;
    }

    public synchronized void clearStoryCache() {
        Prefs.removeFromPrefs(mContext, VisilabsConstant.SHOWN_STORIES_PREF,  VisilabsConstant.SHOWN_STORIES_PREF_KEY);
    }


    private static class Builder {
        private final Context context;

        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context.getApplicationContext();
        }

        /**
         * Create the {@link Prefs} instance.
         */
        public PersistentTargetManager build() {
            return new PersistentTargetManager(context);
        }
    }


}
