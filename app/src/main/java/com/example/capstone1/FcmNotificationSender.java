package com.example.capstone1;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FcmNotificationSender {

    public static void sendFcmNotification(String fcmServerKey, String receiverToken, String title, String body) {
        try {
            URL fcmUrl = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection httpURLConnection = (HttpURLConnection) fcmUrl.openConnection();

                System.out.println("httpURLConnection: " + httpURLConnection.toString());

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Authorization", "key=" + fcmServerKey);

//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("to", receiverToken);
//            JSONObject notificationData = new JSONObject();
//            notificationData.put("title", title);
//            notificationData.put("body", body);
//            jsonObject.put("notification", notificationData);

            JSONObject jsonObject = new JSONObject();
            // Add the "data" object
            JSONObject dataObject = new JSONObject();
            dataObject.put("user_name", title);
            dataObject.put("description", body);
            jsonObject.put("data", dataObject);
            jsonObject.put("to", receiverToken);
            // Add the "to" field


            // Print the JSON payload for logging (optional)
            System.out.println("Request Payload: " + jsonObject.toString());



            String data = jsonObject.toString();

            OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
            writer.write(data);
            writer.flush();
            writer.close();

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode != 200) {
                throw new Exception("Failed to send FCM notification: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void Post_Calling(String fcmServerKey, String receiverToken, String description ,String title) {
        JSONObject jsonObject = new JSONObject();
        // Add the "data" object
        JSONObject dataObject = new JSONObject();
        try {
            dataObject.put("user_name", title);
            dataObject.put("description", description);
            jsonObject.put("data", dataObject);
            jsonObject.put("to", receiverToken);
            HttpUrl URl = HttpUrl.parse("https://fcm.googleapis.com/fcm/send")
                    .newBuilder()
                    .build();
        RequestBody body = RequestBody.create(String.valueOf(jsonObject.toString()), MediaType.parse("application/json; charset=utf-8"));
            System.out.println("body: " + jsonObject.toString());

//            RequestBody body = new FormBody.Builder()
//                    .build();
            Request request = new Request.Builder()
                    .url(URl)
                    .post(body)
                    .addHeader("Authorization","key="+fcmServerKey)
                    .addHeader("Content-Type","application/json")
                    .build();


            OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    int statusCode = response.code();
                    JSONObject data = null;
                    if(statusCode == 200)
                    {
                        try {
                            data = new JSONObject(response.body().string());
                        } catch (JSONException e) {
                            Log.d("APIResponeError",e.getMessage());
                        }
                    }else {
                        Log.d("APIRespone", response.code() + response.message());
                    }
                }
            });
        } catch (JSONException e) {
            Log.d("APIResponeError",e.getMessage());
        }
    }

}
