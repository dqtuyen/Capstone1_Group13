package com.example.capstone1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;



import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.LocationBias;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private PlacesClient placesClient;
    private Marker lastClickedMarker;
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

    private static final int REQUEST_CODE = 200;
    LatLng center;
    RectangularBounds bounds;
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

        }

    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        test = view.findViewById(R.id.test);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); // "this" refers to the MapsFragment instance
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyCxTUngn0mwDNeFZYz-WtqavuykNLzhx8Y");
        }
        placesClient = Places.createClient(requireContext());

        /////////
        getLastLocation(new LocationCallback() {
            @Override
            public void onLocationReceived(LatLng location) {
                center = new LatLng(location.latitude, location.longitude);
                bounds = calculateBounds(center, 3);
                // Xử lý vị trí đã nhận được ở đây
                Toast.makeText(requireContext(), "Latitude: " + location.latitude + ", Longitude: " + location.longitude, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLocationNotFound() {
                // Xử lý khi không tìm thấy vị trí
                Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show();
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchNearbyCarRepairShops("Tiệm sửa xe");
//                getCurrentPlace("Tiệm sửa xe");
//                getCurrentPlace("sửa xe");


            }
        });



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
                Toast.makeText(requireContext(), "Quyền truy cập được cấp", Toast.LENGTH_SHORT).show();
                // Quyền truy cập vị trí đã được cấp
            } else {
                // Quyền truy cập vị trí bị từ chối
                Toast.makeText(requireContext(), "Quyền truy cập bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void searchNearbyCarRepairShops(String query) {
        Places.initialize(requireContext(), "AIzaSyCxTUngn0mwDNeFZYz-WtqavuykNLzhx8Y"); // Thay thế YOUR_API_KEY bằng khóa API của bạn

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setQuery(query)
                .build();

        Places.createClient(requireContext()).findAutocompletePredictions(request)
                .addOnSuccessListener((FindAutocompletePredictionsResponse response) -> {
                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                        String placeId = prediction.getPlaceId();
                        String fullText = prediction.getFullText(null).toString();

                        FetchPlaceRequest placeRequest = FetchPlaceRequest.builder(placeId, Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME))
                                .build();

                        Places.createClient(requireContext()).fetchPlace(placeRequest)
                                .addOnSuccessListener((FetchPlaceResponse placeResponse) -> {
                                    Place place = placeResponse.getPlace();

                                    // Lấy tọa độ của địa điểm
                                    LatLng location = place.getLatLng();

                                    // Tạo một Marker trên bản đồ
                                    MarkerOptions markerOptions = new MarkerOptions()
                                            .position(location)
                                            .title(fullText);

                                    // Thêm Marker lên bản đồ
                                    mMap.addMarker(markerOptions);
                                })
                                .addOnFailureListener((Exception e) -> {
                                    Toast.makeText(requireContext(), "Lỗi khi lấy chi tiết vị trí: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener((Exception e) -> {
                    String errorMessage = "Lỗi khi tìm kiếm địa điểm: " + e.getMessage();
                    Log.e("MyTag", errorMessage);
                });
    }



    // Trong phương thức getCurrentPlace, chúng ta sẽ tạo một đối tượng RectangularBounds để giới hạn tìm kiếm trong bán kính 10KM.
    private void getCurrentPlace(String query) {
                // Tạo yêu cầu tìm kiếm
                FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                        .setQuery(query)
                        .setLocationRestriction(bounds)
                        .setTypeFilter(TypeFilter.ESTABLISHMENT)
                        .build();

                // Gửi yêu cầu tìm kiếm
                Task<FindAutocompletePredictionsResponse> task = placesClient.findAutocompletePredictions(request);

                // Xử lý kết quả trả về
                task.addOnSuccessListener((response) -> {
                    // Lấy danh sách các địa điểm
                    predictions = response.getAutocompletePredictions();

                    // Hiển thị danh sách các địa điểm
                    for (AutocompletePrediction prediction : predictions) {
                        Log.i(TAG, prediction.getFullText(null).toString());
                    }
                    showMarker();

                    task.addOnFailureListener((exception) -> {
                        // Xử lý lỗi
                    });
                });

    }

    private void showMarker() {
        if (mMap == null) {
            // Kiểm tra xem có đối tượng GoogleMap đã sẵn sàng hay chưa
            return;
        }

        // Xóa tất cả các marker cũ trên bản đồ (nếu có)
        //mMap.clear();

        for (AutocompletePrediction prediction : predictions) {
            // Lấy thông tin địa điểm từ prediction
            String placeId = prediction.getPlaceId();

            // Sử dụng Places API để lấy chi tiết địa điểm
            FetchPlaceRequest placeRequest = FetchPlaceRequest.builder(placeId, Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME)).build();
            placesClient.fetchPlace(placeRequest).addOnSuccessListener((fetchPlaceResponse) -> {
                Place place = fetchPlaceResponse.getPlace();

                // Lấy tọa độ và tên của địa điểm
                LatLng location = place.getLatLng();
                String name = place.getName();

                // Tạo marker trên bản đồ
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(location)
                        .title(name)
                        .snippet(prediction.getFullText(null).toString());

                // Thêm marker lên bản đồ
                mMap.addMarker(markerOptions);
            }).addOnFailureListener((exception) -> {
                // Xử lý lỗi khi lấy thông tin chi tiết địa điểm
            });
        }
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