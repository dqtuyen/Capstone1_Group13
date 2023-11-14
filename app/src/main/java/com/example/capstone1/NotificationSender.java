package com.example.capstone1;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotificationSender {

    public static void sendNotificationToTopic(String topic, String title, String body) {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = "https://fcm.googleapis.com/fcm/send";

            MediaType mediaType = MediaType.parse("application/json");
            String json = "{"
                    + "\"to\":\"/topics/" + topic + "\","
                    + "\"notification\":{"
                    + "\"title\":\"" + title + "\","
                    + "\"body\":\"" + body + "\""
                    + "}"
                    + "}";

            RequestBody bodyRequest = RequestBody.create(mediaType, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(bodyRequest)
                    .addHeader("Authorization", "key=YOUR_SERVER_KEY") // Thay YOUR_SERVER_KEY bằng Server key của bạn
                    .build();

            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}