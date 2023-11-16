package com.example.capstone1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.capstone1.Data.DataLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GoogleMapService {
    private GoogleMap googleMap;
    private Marker lastClickedMarker;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private OnAddressReceivedListener onAddressReceivedListener;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Activity activity;
    public GoogleMapService(GoogleMap map, FusedLocationProviderClient fusedLocationProviderClient, Activity activity) {
        this.googleMap = map;
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        this.activity = activity;
    }
    public GoogleMapService(GoogleMap map) {
        this.googleMap = map;
    }

    //Phương thức click vào marker
    public void onMarkerClick() {
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
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
    }

    //Phương thức lấy vị trí hiện tại
    public void myLocation() {
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(activity, location -> {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));
                        }
                    });

        } else {
            // Yêu cầu quyền truy cập vị trí ở đây nếu chưa có quyền.
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }
    public void setOnAddressReceivedListener(OnAddressReceivedListener listener) {
        this.onAddressReceivedListener = listener;
    }

    //Phương thức lấy vị trí khi di chuyển camera
    public void initializeMap() {
        //...
        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng center = googleMap.getCameraPosition().target;
                getAddressFromLocation(center.latitude, center.longitude);
            }
        });
        //...
    }
    //Phương thức lấy địa chỉ từ một vị trí trên bản đồ
    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String addressLine = address.getAddressLine(0);
                String addressName = address.getFeatureName();
                if (onAddressReceivedListener != null) {
                    onAddressReceivedListener.onAddressReceived(addressLine, addressName, latitude, longitude);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Phương thức addMarkers hàng loạt
    public void addMarkers(List<DataLocation> locationList, GoogleMap mMap) {
        for (DataLocation location : locationList) {
            LatLng markerLocation = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(markerLocation)
                    .title(location.getName_address())
                    .snippet(location.getUID());
            mMap.addMarker(markerOptions);
        }
    }

    //Phương thức tìm kiếm vị trí dựa theo từ khóa
    public void searchAddress(Context context, String query) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            // Sử dụng Geocoder để tìm kiếm địa chỉ
            List<Address> addressList = geocoder.getFromLocationName(query, 1);

            if (addressList != null && addressList.size() > 0) {
                // Lấy địa chỉ đầu tiên từ danh sách
                Address address = addressList.get(0);

                // Lấy thông tin chi tiết về địa chỉ
                String locality = address.getLocality(); // Thành phố
                String country = address.getCountryName(); // Quốc gia
                String addressLine = address.getAddressLine(0); // Địa chỉ đầy đủ
                double latitude = address.getLatitude(); // Vĩ độ
                double longitude = address.getLongitude(); // Kinh độ

                // Hiển thị thông tin địa chỉ
                String result = "Locality: " + locality + "\nCountry: " + country +
                        "\nAddress: " + addressLine + "\nLatitude: " + latitude +
                        "\nLongitude: " + longitude;

                Log.d("AddressInfo", result);

                // Hiển thị thông tin địa chỉ (tùy chọn)
                // Tạo LatLng từ thông tin địa chỉ
                LatLng markerLocation = new LatLng(latitude, longitude);

                // Tạo MarkerOptions
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(markerLocation)
                        .title(locality) // Sử dụng thành phố làm tiêu đề marker (có thể thay đổi)
                        .snippet(addressLine); // Sử dụng địa chỉ đầy đủ làm mô tả marker (có thể thay đổi)

                // Hiển thị marker trên bản đồ
                googleMap.clear();
                googleMap.addMarker(markerOptions);

                // Đưa camera đến vị trí của marker và phóng to mức zoom mong muốn
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 14));

                // Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            } else {
                Log.e("AddressInfo", "Address not found");
                // Hiển thị thông báo lỗi (tùy chọn)
                // Toast.makeText(context, "Address not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String trimmedEnd(String address_line) {
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

    public String trimmedStart(String address_line) {
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
}
