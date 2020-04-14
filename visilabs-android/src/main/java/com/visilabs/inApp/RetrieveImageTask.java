package com.visilabs.inApp;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;


public class RetrieveImageTask extends AsyncTask<InAppMessage, Void, Bitmap> {

    private AsyncResponse asyncResponse;

    RetrieveImageTask(AsyncResponse listener){
        this.asyncResponse=listener;
    }

    protected Bitmap doInBackground(InAppMessage... notifications) {
        try {
            if (notifications != null && notifications.length > 0) {
                InAppMessage notification = notifications[0];
                return notification.getImageBitmap();
            }
            return null;

        } catch (Exception e) {
            Log.e("Visilabs", "Can not get image.", e);
            return null;
        }
    }

    protected void onPostExecute(Bitmap inAppImage) {
        if (inAppImage == null) {
            return;
        }

        asyncResponse.processFinish(inAppImage);
    }
}