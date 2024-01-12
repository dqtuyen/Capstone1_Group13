package com.example.capstone1.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.capstone1.Data.DataCallingForRescue;
import com.example.capstone1.Data.DataLocation;
import com.example.capstone1.GenarateCharacter;
import com.example.capstone1.GoogleMapService;
import com.example.capstone1.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewInformation extends AppCompatActivity implements OnMapReadyCallback {
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
    String RESlongitude, RESlatitude, RESaddress, uidCustomer, idField, RESname, RESimg, RESinfoCar, RESphone, RESdescription, RESavgstar;

    String CUSlongitude, CUSlatitude, CUSaddress, CUSuidCustomer, CUSidField, CUSname, CUSimg, CUSinfoCar, CUSphone, CUSdescription, CUSavgstar;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static int CHECK = 0;
    private String role = Role.RESCUE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_information);
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
            RESimg = intent.getStringExtra("RESIMG");
            CUSlatitude = intent.getStringExtra("CUSlatitude");
            CUSlongitude = intent.getStringExtra("CUSlongitude");
            CUSdescription = intent.getStringExtra("CUSdescription");
            CUSaddress = intent.getStringExtra("CUSaddress");
            RESname = intent.getStringExtra("RESNAME");

            String resnumbercar = intent.getStringExtra("RESNUMBERCAR");
            String restypecar = intent.getStringExtra("RESTYPECAR");
            RESinfoCar = resnumbercar + " • " + restypecar;
            RESavgstar = intent.getStringExtra("RESAVGSTAR");

        } else {

        }
        setDataForRescue();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        // Liên kết với sự kiện OnMapReadyCallback
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        GenarateCharacter genarateCharacter = new GenarateCharacter();
        String new_address = genarateCharacter.trimmedCustom(CUSaddress);
        locationList.add(new DataLocation(Double.parseDouble(CUSlatitude), Double.parseDouble(CUSlongitude), new_address, "Tôi "+" đang ở đây!"));
    }

    private void setDataForRescue() {
        Glide.with(ViewInformation.this)
                .load(RESimg)
                .circleCrop() // Áp dụng cắt ảnh thành hình tròn
                .into(img_avt);

        txt_name.setText(RESname);
        txt_info.setText(RESinfoCar);
        txt_avgstar.setVisibility(View.GONE);
        img_btn_star.setVisibility(View.GONE);

        String formattedTime = sdf.format(calendar.getTime());
        txt_time_dangden.setText(formattedTime);
        txt_address.setText(CUSaddress);
        txt_note.setText(CUSdescription);


        btn_confirm.setText("Đánh giá người cứu hộ");
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewInformation.this, Rescue_Evaluate.class);
                intent.putExtra("RESname", RESname);
                intent.putExtra("RESimg", RESimg);
                intent.putExtra("RESinfoCar", RESinfoCar);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setEventForRescue() {

    }

    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMapService = new GoogleMapService(googleMap, fusedLocationProviderClient, this);
        googleMapService.myLocation();

        mMap = googleMap;
        googleMapService.addMarkerAndShowInformation(locationList, mMap, RESimg);
        LatLng currentLocation = new LatLng(Double.parseDouble(CUSlatitude), Double.parseDouble(CUSlatitude));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));


    }
}