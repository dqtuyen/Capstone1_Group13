package com.example.capstone1;

        import android.app.Notification;
        import android.app.NotificationChannel;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.media.RingtoneManager;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Handler;
        import android.os.Looper;
        import android.util.Log;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.core.app.NotificationCompat;
        import androidx.localbroadcastmanager.content.LocalBroadcastManager;

        import com.example.capstone1.Account.Intro;
        import com.example.capstone1.Account.Welcome;
        import com.example.capstone1.Activity.ConfirmLocation;
        import com.example.capstone1.Activity.MainActivity;
        import com.example.capstone1.Activity.RescueCallingList;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.messaging.FirebaseMessagingService;
        import com.google.firebase.messaging.RemoteMessage;

        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.List;
        import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "my_channel_01";

    public static final String TAG = MyFirebaseMessagingService.class.getName ();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        System.out.println("From: " + remoteMessage.getFrom());

        if (remoteMessage.getNotification() != null) {
            System.out.println("Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        Map<String, String> stringMap = remoteMessage.getData();
        String title = stringMap.get("user_name");
        String body = stringMap.get("description");
        sendNotification (title, body);
        sendNotification(title, body, "");

    }

    @Override
    public void onNewToken(String token) {
        // Lấy token của thiết bị
        super.onNewToken(token);

        Log.e(TAG, token);
        // Gửi token này lên server
    }
    private void sendNotification(String from, String body) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(MyFirebaseMessagingService.this.getApplicationContext(), from + " -> " + body,Toast.LENGTH_SHORT).show();
                sendDataToMainActivity(from,body);
            }
        });
    }
    //
    private void sendNotification(String key, String messageBody, String name) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent intent2 = new Intent("com.example.capstone1.MyFirebaseMessagingService");
        intent2.putExtra("notification_clicked", true); // Gửi cờ thông báo đã được bấm
        intent2.putExtra("key_user", key);
        sendBroadcast(intent2);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);



        String channelId = "My channel ID";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_app)
                        .setContentTitle("Có người cần bạn cứu hộ")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendDataToMainActivity(String title, String body) {
        Intent intent = new Intent("com.example.capstone1.MyFirebaseMessagingService");
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        sendBroadcast(intent);
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

}