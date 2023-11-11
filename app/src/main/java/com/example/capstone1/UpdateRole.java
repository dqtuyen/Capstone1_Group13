package com.example.capstone1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.capstone1.Data.DataLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UpdateRole extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mMap;
    private PlacesClient placesClient;
    private Marker lastClickedMarker;
    Spinner spinner_update_role;
    final String[] selectedOption = new String[1];
    FrameLayout frame_map;
    ImageView btn_back;

    EditText edt_address_detail, edt_city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_role);

        edt_address_detail = findViewById(R.id.edt_address_detail);
        edt_city = findViewById(R.id.edt_city);
        btn_back = findViewById(R.id.btn_back);
        spinner_update_role = findViewById(R.id.spinner_update_role);
        frame_map = findViewById(R.id.frame_map);

        // Khởi tạo SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        // Liên kết với sự kiện OnMapReadyCallback
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        setEvent();
        choose_role();
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                        }
                    });


        } else {
            // Yêu cầu quyền truy cập vị trí ở đây nếu chưa có quyền.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }

        // Đặt sự kiện nhấn vào marker
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng markerPosition = marker.getPosition();
                if (marker.equals(lastClickedMarker)) {
                    // Đã nhấn lần thứ hai vào cùng một marker, hủy thông tin
                    if (marker.isInfoWindowShown()) {
                        marker.hideInfoWindow();
                    }
                } else {
                    // Đã nhấn vào một marker khác, hiển thị thông tin và cập nhật biến lastClickedMarker
                    marker.showInfoWindow();
                    lastClickedMarker = marker;
                }
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerPosition, 15);
                googleMap.animateCamera(cameraUpdate);
                return true;
            }
        });
        // Sự kiện di chuyển bản đồ
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                // Lấy tọa độ latitude và longitude khi di chuyển bản đồ kết thúc (người dùng thả tay)
                LatLng center = mMap.getCameraPosition().target;
                double latitude = center.latitude;
                double longitude = center.longitude;
                Log.d("MapMove", "Latitude: " + latitude + ", Longitude: " + longitude);
                // Xử lý tọa độ mới tại đây
                // Sử dụng Geocoder để lấy địa chỉ
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                    if (addresses != null && addresses.size() > 0) {
                        Address address = addresses.get(0);

                        // Lấy thông tin chi tiết địa chỉ
                        String addressLine = address.getAddressLine(0); // Số nhà và tên đường
//                        String city = address.getLocality(); // Thành phố
//                        String state = address.getAdminArea(); // Tỉnh/Quận
//                        String country = address.getCountryName(); // Quốc gia
//                        String postalCode = address.getPostalCode(); // Mã bưu chính


                        Log.d("Address", "Address Line: " + addressLine);
//                        Log.d("Address", "City: " + city);
//                        Log.d("Address", "State: " + state);
//                        Log.d("Address", "Country: " + country);
//                        Log.d("Address", "Postal Code: " + postalCode);

                        edt_address_detail.setText(trimmedStart(addressLine));
                        edt_city.setText(trimmedEnd(addressLine));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    String trimmedStart(String address_line) {
        String fullAddress = address_line;

        // Tách chuỗi thành mảng sử dụng dấu phẩy làm dấu phân cách
        String[] parts = fullAddress.split(",");

        // Kiểm tra xem có ít nhất một phần trước khi lấy phần đầu tiên
        if (parts.length > 0) {
            // Lấy chuỗi trước dấu phẩy đầu tiên
            return parts[0].trim();
        } else {
            // Chuỗi không có dấu phẩy
            System.out.println("Chuỗi không có dấu phẩy trong địa chỉ.");
            return "";
        }
    }

    String trimmedEnd(String address_line) {
        String fullAddress = address_line;

        // Tách chuỗi thành mảng sử dụng dấu phẩy làm dấu phân cách
        String[] parts = fullAddress.split(",");

        // Kiểm tra xem có ít nhất một phần trước khi lấy phần đầu tiên
        if (parts.length > 1) {
            // Lấy chuỗi sau dấu phẩy đầu tiên
            return parts[1].trim() + ", " + parts[2].trim() + ", " + parts[3].trim() + ", " + parts[4].trim();
        } else {
            // Chuỗi không có dấu phẩy hoặc chỉ có một phần (không có phần sau dấu phẩy)
            System.out.println("Chuỗi không có dấu phẩy hoặc chỉ có một phần trong địa chỉ.");
            return "";
        }
    }

    private void choose_role() {
        String[] options = {"Người dùng", "Cứu hộ"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_update_role.setAdapter(adapter);

        spinner_update_role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOption[0] = options[position];
                if (position == 1) {
                    frame_map.setVisibility(View.VISIBLE);
                } else {
                    frame_map.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý sự kiện khi không có tùy chọn nào được chọn
            }
        });

    }

    void setEvent() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}