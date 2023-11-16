package com.example.capstone1.Activity;

import static android.content.ContentValues.TAG;

import static com.example.capstone1.FcmNotificationSender.Post_Calling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone1.GenarateCharacter;
import com.example.capstone1.GoogleMapService;
import com.example.capstone1.OnAddressReceivedListener;
import com.example.capstone1.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConfirmLocation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMapService googleMapService;
    ImageButton btn_back;
    Button btn_confirm;
    EditText edt_note;
    TextView txt_note, txt_nameAddress, txt_address;
    ProgressBar progressBar;
    ImageView img_marker;
    GenarateCharacter genarateCharacter;

    //Quan Trọng
    String nameList;
    private FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_location);
        progressBar = findViewById(R.id.progressBar);
        img_marker = findViewById(R.id.img_marker);
        btn_back = findViewById(R.id.btn_back2);
        btn_confirm = findViewById(R.id.btn_confirm);
        txt_note = findViewById(R.id.txt_note);
        txt_nameAddress = findViewById(R.id.txt_nameAddress);
        txt_address = findViewById(R.id.txt_address);
        edt_note = findViewById(R.id.edt_note);
        genarateCharacter = new GenarateCharacter();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Khởi tạo SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        // Liên kết với sự kiện OnMapReadyCallback
        mapFragment.getMapAsync(this);
        setEvent();
    }
    private double new_latitude;
    private double new_longitude;
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMapService = new GoogleMapService(googleMap, fusedLocationProviderClient, this);
        googleMapService.myLocation();
        googleMapService.onMarkerClick();
        googleMapService.initializeMap();
        googleMapService.setOnAddressReceivedListener(new OnAddressReceivedListener() {
            @Override
            public void onAddressReceived(String address, String addressName, double latitude, double longitude) {
                // Xử lý địa chỉ và thông tin vị trí ở đây
                Log.d("Address", "Received Address: " + address);
                Log.d("Address", "Received Address Name: " + addressName);
                new_latitude = latitude;
                new_longitude = longitude;
                Log.d("Location", "Received Latitude: " + new_latitude);
                Log.d("Location", "Received Longitude: " + new_longitude);

                txt_nameAddress.setText(googleMapService.trimmedStart(address));
                txt_address.setText(googleMapService.trimmedEnd(address));
            }
        });
    }
    void sendNotification() {
        String fcmServerKey = "AAAA-aEDMr4:APA91bFkulQb-yKqZHCdfMMvTnAWHu6eHSaFsPTkTiM4CN4nux4zGjFOpEnk_NXESGI3i98JmZX0AJj7tqyFsxmhhOU5AP4v0fHmxVNNA6olETuUvwhpCg6ip_0NT3kXa-eWUFeC0rP_";
        String receiverToken = "eImI4cXhSR-bWnQP84GKNe:APA91bHvlmJxjpwJggnIDvEAIlB3KA8bT6OBUDDjdoUFxEWzl-CS3vAQZWRhC5XE7Ca7WTUOGz6g8ltGfB0foaSIXRQOc4_FKkOGmVWbfMBUrZP0b3xwGmU9Sy6bJa6FhEUxiqhVL20R";
        String notificationTitle = nameList +"_"+ user.getUid();
        String notificationBody = "Bạn ơi có người cần bạn giúp đỡ";

        // Gửi thông báo
        Post_Calling(fcmServerKey, receiverToken,notificationBody, notificationTitle);
    }

    private void setEvent() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                img_marker.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar

                confirm();
                sendNotificationToRescuers();
                sendNotification();
                intent.putExtra("KEY_name_list", nameList);
                intent.putExtra("KEY", "1");// Đặt key và giá trị cần truyền
                startActivity(intent);
                finish();
            }
        });
        txt_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_note.setVisibility(View.GONE);
                edt_note.setVisibility(View.VISIBLE);
            }
        });



    }

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    void confirm() {
//        Date currentDate = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
//        String formattedDateTime = dateFormat.format(currentDate);
//
//        String datetime_myuid = "081913112023_uid";
//        Map<String, Object> data = new HashMap<>();
//        data.put("datetime", formattedDateTime);
//        data.put("distance", "");
//        data.put("evaluateid", "");
//        data.put("latitude", new_latitude);
//        data.put("longitude", new_longitude);
//        data.put("myuid", user.getUid());
//        data.put("rescueuid", "");
//        db.collection("RescueInformation").document(datetime_myuid)
//                .set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(ConfirmLocation.this, "Thành công", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        e.printStackTrace();
//                        Toast.makeText(getApplicationContext(), "Thất bại", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    void confirm() {
    Date currentDate = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
    String formattedDateTime = dateFormat.format(currentDate);

    String document = user.getUid();
        nameList= genarateCharacter.getLastFiveCharacters(user.getUid()) + "_" +
            genarateCharacter.convertDateTimeFormat(formattedDateTime);
    // Tạo một ArrayList chứa thông tin
    ArrayList<String> dataArray = new ArrayList<>();
    dataArray.add(formattedDateTime); // datetime
    dataArray.add(""); // distance
    dataArray.add(""); // evaluateid
    dataArray.add(String.valueOf(new_latitude)); // latitude
    dataArray.add(String.valueOf(new_longitude)); // longitude
    dataArray.add(user.getUid()); // myuid
    dataArray.add(""); // rescueuid

    // Ghi ArrayList này vào Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection("RescueInformation").document(document)
            .set(Collections.singletonMap(nameList, dataArray))
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(ConfirmLocation.this, "Thành công", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Thất bại", Toast.LENGTH_SHORT).show();
                }
            });
}
    private void sendNotificationToRescuers() {
        FirebaseMessaging.getInstance().subscribeToTopic("rescue_topic")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Subscribed to rescue_topic");
                    } else {
                        Log.e(TAG, "Subscription failed", task.getException());
                    }
                });

    }

}