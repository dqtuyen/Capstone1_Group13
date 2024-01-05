package com.example.capstone1.Activity;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.capstone1.Data.DataCallingForRescue;
import com.example.capstone1.Data.DataLocation;
import com.example.capstone1.GenarateCharacter;
import com.example.capstone1.GlobalData;
import com.example.capstone1.GoogleMapService;
import com.example.capstone1.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Test_Process extends AppCompatActivity implements OnMapReadyCallback {

    ImageView img_dangden, img_daden, img_daxong, img_avt;
    Button btn_confirm;
    ImageButton img_btn_back, img_btn_star;
    TextView txt_title, txt_name, txt_info, txt_avgstar, txt_note, txt_address;
    TextView txt_time_daxong,txt_time_dangden,txt_time_daden;
    View view1, view2;
    private GoogleMapService googleMapService;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    List<DataLocation> locationList = new ArrayList<>();

    DataCallingForRescue dataCallingForRescue;
    Map<Integer, String> info = new HashMap<>();
    String longitude, latitude, address, uidCustomer, idField, name, img, infoCar, phone, description, avgstar;

    String CUSlongitude, CUSlatitude, CUSaddress, CUSuidCustomer, CUSidField, CUSname, CUSimg, CUSinfoCar, CUSphone, CUSdescription, CUSavgstar;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static int CHECK = 0;
    private String role = Role.RESCUE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_process);

        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(findViewById(R.id.sheet));
        behavior.setPeekHeight(230);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        img_dangden = findViewById(R.id.img_dangden);
        img_daden = findViewById(R.id.img_daden);
        img_daxong = findViewById(R.id.img_daxong);
        img_avt = findViewById(R.id.img_avt);
        btn_confirm = findViewById(R.id.btn_confirm);
        img_btn_back = findViewById(R.id.img_btn_back);
        txt_title = findViewById(R.id.txt_title);
        txt_name = findViewById(R.id.txt_name);
        txt_info = findViewById(R.id.txt_info);
        txt_avgstar = findViewById(R.id.txt_avgstar);
        txt_note = findViewById(R.id.txt_note);
        txt_address = findViewById(R.id.txt_address);
        img_btn_star = findViewById(R.id.img_btn_star);
        txt_time_daxong = findViewById(R.id.txt_time_daxong);
        txt_time_daden = findViewById(R.id.txt_time_daden);
        txt_time_dangden = findViewById(R.id.txt_time_dangden);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);

        Intent intent = getIntent();
        if (intent != null) {
            uidCustomer = intent.getStringExtra("UID_CUSTOMER");
            address = intent.getStringExtra("ADDRESS");
            latitude = intent.getStringExtra("LATITUDE");
            longitude = intent.getStringExtra("LONGITUDE");
            idField = intent.getStringExtra("ID_FIELD");
            name = intent.getStringExtra("NAME");
            img = intent.getStringExtra("IMG");
            infoCar = intent.getStringExtra("INFOCAR");
            description = intent.getStringExtra("DESCRIPTION");
            Log.d("LOG",latitude + longitude);
        } else {

        }

//        GlobalData globalData = GlobalData.getInstance();
//        role = globalData.getRole();
//        Log.d("TEST", role);

            setEventForRescue();
            setDataForRescue();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        // Liên kết với sự kiện OnMapReadyCallback
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        GenarateCharacter genarateCharacter = new GenarateCharacter();
        String new_address = genarateCharacter.trimmedCustom(address);
        locationList.add(new DataLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), new_address, name+" đang ở đây!"));
    }

    ///////////// RESCUE ///////////// RESCUE ///////////// RESCUE ///////////// RESCUE ///////////// RESCUE ///////////// RESCUE ///////////// RESCUE
    private void setDataForRescue() {
        Glide.with(Test_Process.this)
                .load(img)
                .circleCrop() // Áp dụng cắt ảnh thành hình tròn
                .into(img_avt);

        txt_name.setText(name);
        txt_info.setText(infoCar);
        txt_avgstar.setVisibility(View.GONE);
        img_btn_star.setVisibility(View.GONE);

        String formattedTime = sdf.format(calendar.getTime());
        txt_time_dangden.setText(formattedTime);
        txt_address.setText(address);
        txt_note.setText(description);
    }

    private void setEventForRescue() {
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHECK++;
                if(CHECK == 1) {
                    img_daden.setImageResource(R.drawable.done);
                    img_daden.setPadding(0, 0, 0, 0);
                    int orangeColor = ContextCompat.getColor(Test_Process.this, R.color.orange);
                    view1.setBackgroundColor(orangeColor);

                    String formattedTime = sdf.format(calendar.getTime());
                    txt_time_daden.setText(formattedTime);
                    btn_confirm.setText("Xác nhận đã xong");
                    txt_title.setText("Đang chuẩn bị tiến hành sửa xe...");
                }
                if(CHECK == 2) {
                    img_daxong.setImageResource(R.drawable.done);
                    img_daxong.setPadding(0, 0, 0, 0);

                    int orangeColor = ContextCompat.getColor(Test_Process.this, R.color.orange);
                    view2.setBackgroundColor(orangeColor);
                    String formattedTime = sdf.format(calendar.getTime());
                    txt_time_daxong.setText(formattedTime);
                    txt_title.setText("Công việc đã hoàn thành...");
                }
            }
        });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMapService = new GoogleMapService(googleMap, fusedLocationProviderClient, this);
        googleMapService.myLocation();

        mMap = googleMap;
        googleMapService.addMarkerAndShowInformation(locationList, mMap);
        LatLng currentLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));


    }
}