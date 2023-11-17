package com.example.capstone1.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.capstone1.Adapter.CustomerAdapter;
import com.example.capstone1.Data.DataUser;
import com.example.capstone1.R;

import java.util.ArrayList;

public class RescueCallingList extends AppCompatActivity {

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getStringExtra("title");
            String body = intent.getStringExtra("body");

            // Xử lý dữ liệu nhận được từ service ở đây
            // Ví dụ: Hiển thị thông báo trong MainActivity
            Toast.makeText(getApplication(), title + " -> " + body, Toast.LENGTH_SHORT).show();
            Log.e("LOG",title + " -> " + body );
        }
    };


    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("LOG", "Đã bấm vào thông báo");
        if (intent.getBooleanExtra("notification_clicked", false)) {
            String title = intent.getStringExtra("title");
            String key_user = intent.getStringExtra("key_user");
            Log.d("LOG", "Notification Clicked - Title: " + title + ", Key_User: " + key_user);
            Toast.makeText(getApplication(), title + key_user, Toast.LENGTH_SHORT).show();
        }
    }
    RecyclerView recyclerView;
    ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_calling_list);

        recyclerView = findViewById(R.id.recyclerView);
        img_back = findViewById(R.id.img_back);

        ArrayList<DataUser> userList = new ArrayList<>();

        // Adding 5 users to the ArrayList
        userList.add(new DataUser("UID1", "Alice", "1234567890", "123 Main St", "Description 1", "9:00 AM", "ABC123", "Sedan", "img1.jpg"));
        userList.add(new DataUser("UID2", "Bob", "9876543210", "456 Elm St", "Description 2", "10:30 AM", "DEF456", "SUV", "img2.jpg"));
        userList.add(new DataUser("UID3", "Charlie", "1112223333", "789 Oak St", "Description 3", "12:00 PM", "GHI789", "Truck", "img3.jpg"));
        userList.add(new DataUser("UID4", "David", "4445556666", "321 Pine St", "Description 4", "2:00 PM", "JKL012", "Convertible", "img4.jpg"));
        userList.add(new DataUser("UID5", "Eva", "7778889999", "654 Cedar St", "Description 5", "4:30 PM", "MNO345", "Hatchback", "img5.jpg"));

        CustomerAdapter adapter = new CustomerAdapter(getApplicationContext(),userList);
        recyclerView.setAdapter(adapter);

        setEvent();
    }

    void setEvent() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RescueCallingList.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    public void onBackPressed() {
        Intent intent = new Intent(RescueCallingList.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter("com.example.capstone1.MyFirebaseMessagingService");
        registerReceiver(messageReceiver, intentFilter);
    }
}