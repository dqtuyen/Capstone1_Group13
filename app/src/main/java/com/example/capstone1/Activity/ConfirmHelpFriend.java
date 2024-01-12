package com.example.capstone1.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.capstone1.Adapter.StreetAdapter;
import com.example.capstone1.Data.DataLocation;
import com.example.capstone1.Fragment.MapsFragment;
import com.example.capstone1.GoogleMapService;
import com.example.capstone1.R;
import com.example.capstone1.StreetNameSuggesterDaNang;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;

public class ConfirmHelpFriend extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private PlacesClient placesClient;
    private Marker lastClickedMarker;
    private GoogleMapService googleMapService;
    SearchView search;
    EditText edt_test;
    List<DataLocation> locationList = new ArrayList<>();
    StreetNameSuggesterDaNang suggester;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Geocoder geocoder;

    RecyclerView recyclerView;
    private StreetAdapter adapter;
    Button test;
    String name, phone, numbercar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_help_friend);

        Intent intent = getIntent();

        // Kiểm tra xem Intent có dữ liệu không
        if(intent != null) {
            // Lấy dữ liệu từ Intent sử dụng các phương thức getXXXExtra()
            name = intent.getStringExtra("name");
            phone = intent.getStringExtra("phone");
            numbercar = intent.getStringExtra("numbercar");
        }
            // Sử dụng dữ liệu nhận được ở đây
            // Ví dụ: Hiển thị dữ liệu trong TextView

        MapsFragment fragment = new MapsFragment();

        // Sử dụng FragmentManager để thêm hoặc thay thế Fragment trong Activity
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment) // R.id.fragment_container là ID của Layout dùng để chứa Fragment trong Activity
                .commit();

        fragment.receiveDataFromActivity(name, phone, numbercar);
    }
//

}