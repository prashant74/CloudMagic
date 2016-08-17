package com.example.prashant.cloudmagic.task;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.example.prashant.cloudmagic.models.EmailDetail;
import com.example.prashant.cloudmagic.models.Participants;
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

/**
 * Created by Prashant on 17/08/2016.
 */
public class DetailEmailTask extends AsyncTask<String, Void, EmailDetail> {

    private final String LOG_TAG = FetchEmailTask.class.getSimpleName();
    DetailEmailResponseListener listener;
    private String dummy = "{\"subject\":\"With regard to our conversation earlier today\",\"participants\":[{\"name\":\"Socrates\",\"email\":\"socrates@gmail.com\"},{\"name\":\"Confucius\",\"email\":\"confucius@gmail.com\"}],\"preview\":\"With regard to our conversation earlier today\",\"isRead\":false,\"isStarred\":false,\"id\":1,\"body\":\"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas egestas leo sit amet justo vehicula tincidunt. Nullam a ipsum vitae ipsum feugiat sodales gravida quis nibh. Integer sed condimentum mauris. Maecenas venenatis purus quis nisl consequat, ac iaculis mi congue. Nunc ac turpis vel sapien maximus iaculis. Nunc in mollis lectus. Cras sem nisi, auctor eu malesuada consectetur, bibendum et tortor. Ut interdum nisl sed magna ullamcorper, ut pulvinar ligula luctus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.Etiam in tempor est. Praesent egestas rutrum ex sed ornare. Duis mi neque, vehicula et vehicula ut, convallis nec felis. Morbi mattis vitae massa sed rhoncus. Integer pulvinar tincidunt eros. Etiam ornare metus ac viverra placerat. Integer vel nunc et quam varius accumsan at in nibh. Fusce at luctus odio, vitae dictum mauris. Nulla facilisi. Cras non lorem porta, porta neque quis, ornare urna. Proin eleifend pretium neque id vestibulum. Ut condimentum mauris nec ipsum mollis molestie. Vivamus at erat lorem. In odio dui, molestie a accumsan sit amet, semper sed arcu. Sed ornare sed justo a ultrices. Fusce non consectetur lacus.\",\"ts\":1471419641}";

    public DetailEmailTask(Context context) {
        listener = (DetailEmailResponseListener) context;
    }

    private String getEmailDetail(String id) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority("127.0.0.1:8088")
                .appendPath("api")
                .appendPath("message")
                .appendPath(id);
        String myUrl = builder.build().toString();
        Log.d(LOG_TAG, myUrl);
        InputStream inputStream;
        BufferedReader bufferedReader = null;
        HttpURLConnection conn = null;
        String jsonResponse = null;
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
        jsonResponse = dummy;
        return jsonResponse;
    }

    private EmailDetail parseEmailDetails(String jsonResponse) {
        EmailDetail email = new EmailDetail();
        try {
            if (TextUtils.isEmpty(jsonResponse)) return null;
            JSONObject emailObject = new JSONObject(jsonResponse);
            email.setId(emailObject.getString(Constants.KEY_ID));
            email.setSubject(emailObject.getString(Constants.KEY_EMAIL_SUBJECT));
            email.setStarred(emailObject.getBoolean(Constants.KEY_IS_STARRED));
            email.setTs(emailObject.getLong(Constants.KEY_TIME_STAMP));
            email.setBody(emailObject.getString(Constants.KEY_BODY));
            JSONArray participantsJsonArray = emailObject.getJSONArray(Constants.KEY_PARTICIPANTS);
            Participants particpiantsArray[] = new Participants[participantsJsonArray.length()];
            for (int j = 0; j < participantsJsonArray.length(); j++) {
                JSONObject participantObject = participantsJsonArray.getJSONObject(j);
                Participants participants = new Participants();
                participants.setName(participantObject.getString(Constants.KEY_NAME));
                participants.setEmail(participantObject.getString(Constants.KEY_EMAIL));
                particpiantsArray[j] = participants;
            }
            email.setParticipants(particpiantsArray);
        } catch (JSONException e) {
            Log.d(LOG_TAG, "Exception Occurred: " + e.toString());
        }
        return email;
    }

    @Override
    protected EmailDetail doInBackground(String... params) {
        String id = params[0].toString();
        String apiResponse = getEmailDetail(id);
        return parseEmailDetails(apiResponse);
    }

    @Override
    protected void onPostExecute(EmailDetail detail) {
        listener.onEmailDetailResponse(detail);
    }

    public interface DetailEmailResponseListener {
        void onEmailDetailResponse(EmailDetail detail);
    }
}
