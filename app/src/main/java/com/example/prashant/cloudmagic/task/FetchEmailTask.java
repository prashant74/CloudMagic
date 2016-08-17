package com.example.prashant.cloudmagic.task;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.example.prashant.cloudmagic.models.Email;
import com.example.prashant.cloudmagic.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant on 17/08/2016.
 */
public class FetchEmailTask extends AsyncTask<Void, Void, String> {
    private final String LOG_TAG = FetchEmailTask.class.getSimpleName();
    private List<Email> mList;
    EmailResponseListener listener;

    public FetchEmailTask(Context context) {
        listener = (EmailResponseListener) context;
    }

    private String getEmails() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority("127.0.0.1:8088")
                .appendPath("api")
                .appendPath("message");
        String myUrl = builder.build().toString();
        Log.d(LOG_TAG, myUrl);
        InputStream inputStream;
        BufferedReader bufferedReader = null;
        HttpURLConnection conn = null;
        String jsonResponse = "[{\"subject\":\"With regard to our conversation earlier today\",\"participants\":[\"Socrates\",\"Confucius\"],\"preview\":\"With regard to our conversation earlier today\",\"isRead\":false,\"isStarred\":false,\"ts\":1471422179,\"id\":1},{\"subject\":\"Critique of Judgement - 30 copies\",\"participants\":[\"David Hume\"],\"preview\":\"Hello, can you please send a quote for Critique of Judgement - 30 copies, so that I can run it by our procurement department and get things going as soon as possible\",\"isRead\":false,\"isStarred\":true,\"ts\":1471422119,\"id\":2},{\"subject\":\"Artwork for \\\"Collection of Plato's  Dialogues\\\"\",\"participants\":[\"Plato\"],\"preview\":\"Aristotle,\\nI shortlisted a designed from Dribble\",\"isRead\":false,\"isStarred\":false,\"ts\":1471422059,\"id\":3},{\"subject\":\"Critique of Pure Reason - 20 copies\",\"participants\":[\"Bertrand Russel\"],\"preview\":\"Hello,\\n Thanks for the quote. I appreciate the swift response!\",\"isRead\":false,\"isStarred\":false,\"ts\":1471421999,\"id\":4},{\"subject\":\"Top exotic beaches in Greece\",\"participants\":[\"Aristotle\"],\"preview\":\"Plato,\\nThis is what I was talking about for the bachelor party!\",\"isRead\":false,\"isStarred\":true,\"ts\":1471421939,\"id\":5}]";
        try {
            URL url = new URL(myUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int response = conn.getResponseCode();
            if (response != 200) {
                return null;
            }
            inputStream = conn.getInputStream();
            if (inputStream == null) {
                return null;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder stringBuilder = new StringBuilder("");
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            if (stringBuilder.length() == 0) {
                return null;
            }
            jsonResponse = stringBuilder.toString();
            Log.d(LOG_TAG, jsonResponse);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        } finally {
            if (conn != null) {
            }
            conn.disconnect();
        }
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonResponse;
    }

    private void parseEmails(String jsonResponse) {
        try {
            if (TextUtils.isEmpty(jsonResponse)) return;
            mList = new ArrayList<Email>();
            JSONArray recordsArray = new JSONArray(jsonResponse);
            if (recordsArray.length() > 0) {
                for (int i = 0; i < recordsArray.length(); i++) {
                    JSONObject emailObject = recordsArray.getJSONObject(i);
                    Email email = new Email();
                    email.setId(emailObject.getString(Constants.KEY_ID));
                    email.setSubject(emailObject.getString(Constants.KEY_EMAIL_SUBJECT));
                    email.setPreview(emailObject.getString(Constants.KEY_PREVIEW));
                    email.setRead(emailObject.getBoolean(Constants.KEY_IS_READ));
                    email.setStarred(emailObject.getBoolean(Constants.KEY_IS_STARRED));
                    email.setTs(emailObject.getLong(Constants.KEY_TIME_STAMP));
                    JSONArray participantsArray = emailObject.getJSONArray(Constants.KEY_PARTICIPANTS);
                    String particpiantsNameArray[] = new String[participantsArray.length()];
                    for (int j = 0; j < participantsArray.length(); j++) {
                        particpiantsNameArray[j] = participantsArray.getString(j);
                    }
                    email.setParticipants(particpiantsNameArray);
                    email.process();
                    mList.add(email);
                }
            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, "Exception Occurred: " + e.toString());
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        String apiResponse = getEmails();
        parseEmails(apiResponse);
        return null;
    }

    @Override
    protected void onPostExecute(String res) {
        listener.onEmailResponse(true, mList);
    }

    public interface EmailResponseListener {
        void onEmailResponse(boolean isSuccess, List<Email> emailList);
    }
}