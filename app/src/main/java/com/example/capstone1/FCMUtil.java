package com.example.capstone1;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

public class FCMUtil {

    private static final String TAG = "FCMUtil";
    private static final String TOPIC_PREFIX = "topic_";

    public static void subscribeToTopic(Context context, String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_PREFIX + topic);
        saveSubscriptionState(context, topic, true);

        // Log
        Log.d(TAG, "Subscribed to topic: " + topic);
    }

    public static void unsubscribeFromTopic(Context context, String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_PREFIX + topic);
        saveSubscriptionState(context, topic, false);

        // Log
        Log.d(TAG, "Unsubscribed from topic: " + topic);
    }

    public static boolean isSubscribedToTopic(Context context, String topic) {
        boolean isSubscribed = getSubscriptionState(context, topic);

        // Log
        Log.d(TAG, "Is subscribed to topic " + topic + ": " + isSubscribed);

        return isSubscribed;
    }

    private static void saveSubscriptionState(Context context, String topic, boolean isSubscribed) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("FCM_SUBSCRIPTIONS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(TOPIC_PREFIX + topic, isSubscribed);
        editor.apply();
    }

    private static boolean getSubscriptionState(Context context, String topic) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("FCM_SUBSCRIPTIONS", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(TOPIC_PREFIX + topic, false);
    }
}