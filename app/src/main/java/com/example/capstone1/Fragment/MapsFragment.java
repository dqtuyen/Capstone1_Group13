package com.example.capstone1.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;



import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private PlacesClient placesClient;
    private Marker lastClickedMarker;

    List<DataLocation> locationList = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationProviderClient;
    List<AutocompletePrediction> predictions;
    //Use fields to define the data types to return.
    List<Place.Field> placeFields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
    );
    // Use the builder to create a FindCurrentPlaceRequest.
    FindCurrentPlaceRequest request =
            FindCurrentPlaceRequest.builder(placeFields).build();

    private List<PlaceLikelihood> placeLikelihoods;
    LatLng latLng;
    String address;
    private String placeId;
    private static final int REQUEST_CODE = 200;

    Button test;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);

                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(requireActivity(), location -> {
                            if (location != null) {
                                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                            }
                        });


            } else {
                // Yêu cầu quyền truy cập vị trí ở đây nếu chưa có quyền.
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            }

            Data();

            for (DataLocation location : locationList) {
                LatLng markerLocation = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(markerLocation)
                        .title(location.getName_address())
                        .snippet(location.getUID());
                mMap.addMarker(markerOptions);
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


            // Sử dụng Geocoder để lấy địa chỉ
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

            try {
                List<Address> addresses = geocoder.getFromLocation(16.067852, 108.180978, 1);

                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);

                    // Lấy thông tin chi tiết địa chỉ
                    String addressLine = address.getAddressLine(0); // Số nhà và tên đường
                    String city = address.getLocality(); // Thành phố
                    String state = address.getAdminArea(); // Tỉnh/Quận
                    String country = address.getCountryName(); // Quốc gia
                    String postalCode = address.getPostalCode(); // Mã bưu chính

                    Log.d("Address", "Address Line: " + addressLine);
                    Log.d("Address", "City: " + city);
                    Log.d("Address", "State: " + state);
                    Log.d("Address", "Country: " + country);
                    Log.d("Address", "Postal Code: " + postalCode);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Sự kiện di chuyển bản đồ
            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    // Lấy tọa độ latitude và longitude khi bản đồ được di chuyển
                    LatLng center = mMap.getCameraPosition().target;
                    double latitude = center.latitude;
                    double longitude = center.longitude;
                    Log.d("MapMove", "Latitude: " + latitude + ", Longitude: " + longitude);
                    // Xử lý tọa độ mới tại đây
                }
            });

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); // "this" refers to the MapsFragment instance
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyCxTUngn0mwDNeFZYz-WtqavuykNLzhx8Y");
        }
        placesClient = Places.createClient(requireContext());


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void onMapReady(GoogleMap googleMap) {
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền truy cập vị trí đã được cấp
            } else {
                // Quyền truy cập vị trí bị từ chối
            }
        }
    }

    private void Data() {


        locationList.add(new DataLocation(16.067301, 108.220337, "Cà Phê Sài Gòn", "UID1"));
        locationList.add(new DataLocation(16.053001, 108.218446, "Bánh Mì Ngọt Ngào", "UID2"));
        locationList.add(new DataLocation(16.047932, 108.225903, "Minimart Đà Nẵng", "UID3"));
        locationList.add(new DataLocation(16.053611, 108.218305, "Biển Xanh Quán", "UID4"));
        locationList.add(new DataLocation(16.048742, 108.230002, "Fitness Plus", "UID5"));
        locationList.add(new DataLocation(16.056794, 108.218841, "Pet Friends", "UID6"));
        locationList.add(new DataLocation(16.051391, 108.226309, "Hoa Phượng Đỏ", "UID7"));
        locationList.add(new DataLocation(16.045812, 108.232472, "Bánh Mì Bình Minh", "UID8"));
        locationList.add(new DataLocation(16.049472, 108.217913, "Fashionista", "UID9"));
        locationList.add(new DataLocation(16.062739, 108.222348, "Nhân Tâm Pharmacy", "UID10"));

    }

    //Sử dụng callback sẽ đảm bảo rằng bạn nhận được vị trí trong lần đầu tiên chạy.
    private void getLastLocation(final LocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(requireActivity(), new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                callback.onLocationReceived(new LatLng(latitude, longitude));
                            } else {
                                callback.onLocationNotFound();
                            }
                        }
                    });
        }
    }

    public interface LocationCallback {
        void onLocationReceived(LatLng location);
        void onLocationNotFound();
    }

    // Hàm tính giới hạn bán kính dựa trên tọa độ và bán kính (đơn vị là kilômét)
    private RectangularBounds calculateBounds(LatLng center, double radiusInKilometers) {
        double latPerMeter = 1.0 / 111320.0; // 1 độ vĩ tuyến ≈ 111320 mét
        double lonPerMeter = 1.0 / (40075000.0 / 360.0); // 1 độ kinh tuyến ≈ 40075000 mét / 360 độ

        double latDelta = radiusInKilometers * 1000 * latPerMeter;
        double lonDelta = radiusInKilometers * 1000 * lonPerMeter;

        double latMin = center.latitude - latDelta;
        double latMax = center.latitude + latDelta;
        double lonMin = center.longitude - lonDelta;
        double lonMax = center.longitude + lonDelta;

        return RectangularBounds.newInstance(
                new LatLng(latMin, lonMin),
                new LatLng(latMax, lonMax)
        );
    }
}