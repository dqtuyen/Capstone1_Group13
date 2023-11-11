package com.example.capstone1.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone1.Data.DataLocation;
import com.example.capstone1.R;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    Button btn_updaterole;

    double latitude;
    double longitude;
    String addressLine;

    LinearLayout hint_layout;
    EditText edt_address_name;
    EditText edt_address_detail, edt_city;

    TextView txt_hint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_role);

        edt_address_detail = findViewById(R.id.edt_address_detail);
        edt_city = findViewById(R.id.edt_city);
        btn_back = findViewById(R.id.btn_back);
        spinner_update_role = findViewById(R.id.spinner_update_role);
        frame_map = findViewById(R.id.frame_map);
        btn_updaterole = findViewById(R.id.btn_updaterole);
        edt_address_name = findViewById(R.id.edt_address_name);
        hint_layout = findViewById(R.id.hint_layout);
        txt_hint = findViewById(R.id.txt_hint);
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
                latitude = center.latitude;
                longitude = center.longitude;
                Log.d("MapMove", "Latitude: " + latitude + ", Longitude: " + longitude);
                // Xử lý tọa độ mới tại đây
                // Sử dụng Geocoder để lấy địa chỉ
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                    if (addresses != null && addresses.size() > 0) {
                        Address address = addresses.get(0);

                        // Lấy thông tin chi tiết địa chỉ
                        addressLine = address.getAddressLine(0); // Số nhà và tên đường
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
    int role = 0;
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
                    hint_layout.setVisibility(View.VISIBLE);
                    txt_hint.setVisibility(View.GONE);
                    role++;
                } else {
                    hint_layout.setVisibility(View.GONE);

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

        btn_updaterole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    void updateRoleInFirestore() {
        String uid = user.getUid().toString();

        Map<String, Object> data = new HashMap<>();
        data.put("latitude", latitude);
        data.put("longitude", longitude);
        data.put("address", addressLine);
        data.put("address_name", edt_address_name.getText().toString());


        db.collection("Location").document(uid)
                .set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Update Success", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                });


        Map<String, Object> update_role = new HashMap<>();
        if(role == 1) {
            update_role.put("role", "rescue");
        } else {
            update_role.put("role", "customer");
        }

        db.collection("Users").document(uid).update(update_role)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Update Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
        private void showConfirmationDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận cập nhật");
            builder.setMessage("Bạn có chắc muốn cập nhật thông tin không?");

            // Nút tích cực (Chấp nhận)
            builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Thực hiện cập nhật ở đây
                    updateRoleInFirestore();
                }
            });

            // Nút tiêu cực (Hủy bỏ)
            builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Đóng dialog, không thực hiện cập nhật
                    dialog.dismiss();
                }
            });

            // Hiển thị dialog
            builder.show();
        }
}