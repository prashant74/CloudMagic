package com.example.prashant.cloudmagic.task;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Prashant on 17/08/2016.
 */
public class DeleteEmailTask extends AsyncTask<String, Void, Boolean> {

    private final String LOG_TAG = FetchEmailTask.class.getSimpleName();
    DeleteEmailTaskListener listener;

    public DeleteEmailTask(Context context) {
        listener = (DeleteEmailTaskListener) context;
    }

    private boolean deleteEmail(String id) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority("127.0.0.1:8088")
                .appendPath("api")
                .appendPath("message")
                .appendPath(id);
        String myUrl = builder.build().toString();
        HttpURLConnection conn = null;
        try {
            URL url = new URL(myUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.connect();
            int response = conn.getResponseCode();
            if (response != 200) {
                return false;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        } finally {
            if (conn != null) {
            }
            conn.disconnect();
        }
        return true;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String id = params[0].toString();
        return deleteEmail(id);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        listener.onDeleteEmail(success);
    }

    public interface DeleteEmailTaskListener {
        void onDeleteEmail(boolean success);
    }
}
