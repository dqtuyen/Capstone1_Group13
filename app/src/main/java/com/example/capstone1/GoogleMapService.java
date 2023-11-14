package com.example.capstone1;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

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
