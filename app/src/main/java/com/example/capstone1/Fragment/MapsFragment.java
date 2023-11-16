package com.example.capstone1.Fragment;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone1.Adapter.StreetAdapter;
import com.example.capstone1.Data.DataLocation;
import com.example.capstone1.GoogleMapService;
import com.example.capstone1.R;
import com.example.capstone1.StreetModel;
import com.example.capstone1.StreetNameSuggesterDaNang;
import com.google.android.gms.common.api.Status;
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
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
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
            googleMapService = new GoogleMapService(googleMap, fusedLocationProviderClient, getActivity());
            googleMapService.myLocation();
            googleMapService.onMarkerClick();
            mMap = googleMap;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000); // Simulating a delay of 5 seconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Adding markers on the main (UI) thread using Handler
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            googleMapService.addMarkers(locationList, mMap);
                            googleMapService.initializeMap();
                        }
                    });
                }
            }).start();
        }
    };

    private void moveCameraToLocation(LatLng location) {
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        }
    }
    private String receivedData;

    public void processDataFromAdapter(String data) {
        // Xử lý dữ liệu từ Adapter trong Fragment
        if(data != "") {
            googleMapService.searchAddress(getContext(), data + "Da Nang");
            recyclerView.setVisibility(View.GONE);
        }
        receivedData = data;
        Log.d("Fragment", "Received data from Adapter: " + data);

    }
    void setEvent() {

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                googleMapService.searchAddress(getContext(), query + "Da Nang");
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    suggester = new StreetNameSuggesterDaNang();
                    List<String> suggestedStreetsByPrefix = new ArrayList<>();
                    suggestedStreetsByPrefix.add(newText);
                    suggestedStreetsByPrefix.addAll(suggester.suggestStreetByName(newText));

                    List<String> limitedSuggestedStreets = new ArrayList<>();
                    for (int i = 0; i < Math.min(5, suggestedStreetsByPrefix.size()); i++) {
                        limitedSuggestedStreets.add(suggestedStreetsByPrefix.get(i));
                    }
                    // Chuyển đổi danh sách đường sang danh sách StreetModel
                    List<StreetModel> streetModels = convertToStreetModels(suggestedStreetsByPrefix);

                    // Khởi tạo Adapter và RecyclerView
                    adapter = new StreetAdapter(newText, streetModels);
                    adapter.setMapsFragment(MapsFragment.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                }
                return true;
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private List<StreetModel> convertToStreetModels(List<String> streetNames) {
        // Chuyển đổi danh sách tên đường sang danh sách StreetModel
        List<StreetModel> streetModels = new ArrayList<>();
        for (String streetName : streetNames) {
            streetModels.add(new StreetModel(streetName));
        }
        return streetModels;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); // "this" refers to the MapsFragment instance
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        search = view.findViewById(R.id.searchViewHome);
        search.clearFocus();
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyCxTUngn0mwDNeFZYz-WtqavuykNLzhx8Y");
        }
        placesClient = Places.createClient(requireContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        setEvent();
        Data();

        return view;
    }


//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100 && resultCode == RESULT_OK) {
////When success
////Initialize place
//            Place place = Autocomplete.getPlaceFromIntent(data);
//            edt_test.setText(place.getAddress());
//            Log.d("TestAddress", "Address" + place.getAddress());
//            Log.d("TestAddress", "Name" + place.getName());
//            Log.d("TestAddress", place.getLatLng().toString());
//        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
////When error
////Initialize status
//            Status status = Autocomplete.getStatusFromIntent(data);
////Display toast
//            Toast.makeText(getContext(), status.getStatusMessage()
//                    , Toast.LENGTH_SHORT).show();
//        }
//    }


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