package com.example.capstone1.Fragment;

import static com.example.capstone1.FcmNotificationSender.Post_Calling;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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

import com.example.capstone1.Activity.MainActivity;
import com.example.capstone1.Adapter.StreetAdapter;
import com.example.capstone1.Data.DataLocation;
import com.example.capstone1.GenarateCharacter;
import com.example.capstone1.GoogleMapService;
import com.example.capstone1.R;
import com.example.capstone1.StreetModel;
import com.example.capstone1.StreetNameSuggesterDaNang;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    Button btn_confirm;
    private GoogleMap googleMap;
    String name, phone, numbercar;
    public void receiveDataFromActivity(String name, String phone, String numbercar) {
        // Sử dụng dữ liệu được nhận ở đây, ví dụ:
        Log.d("MapsFragment", "Address Line: " + name);
        Log.d("MapsFragment", "Latitude: " + phone);
        Log.d("MapsFragment", "Longitude: " + numbercar);
        this.name = name;
        this.phone = phone;
        this.numbercar = numbercar;
    }
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

    private void sendDataToActivity() {

    }
    private void moveCameraToLocation(LatLng location) {
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        }
    }
    private String receivedData;

    public void processDataFromAdapter(String data) {
        // Xử lý dữ liệu từ Adapter trong Fragment
        if(data != "") {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

            try {
                // Sử dụng Geocoder để tìm kiếm địa chỉ
                List<Address> addressList = geocoder.getFromLocationName(data + "Da Nang", 1);

                if (addressList != null && addressList.size() > 0) {
                    // Lấy địa chỉ đầu tiên từ danh sách
                    Address address = addressList.get(0);

                    // Lấy thông tin chi tiết về địa chỉ
                    String locality = address.getLocality(); // Thành phố
                    String country = address.getCountryName(); // Quốc gia
                    addressLine = address.getAddressLine(0); // Địa chỉ đầy đủ
                    latitude = address.getLatitude(); // Vĩ độ
                    longitude = address.getLongitude(); // Kinh độ

                    txt_address.setText("Địa chỉ: "+ addressLine);

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
                    mMap.clear();
                    mMap.addMarker(markerOptions);

                    // Đưa camera đến vị trí của marker và phóng to mức zoom mong muốn
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 14));

                    // Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                } else {
                    Log.e("AddressInfo", "Address not found");
                    // Hiển thị thông báo lỗi (tùy chọn)
                    // Toast.makeText(context, "Address not found", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            recyclerView.setVisibility(View.GONE);
        }
        receivedData = data;
        Log.d("Fragment", "Received data from Adapter: " + data);

    }

    String addressLine;
    double latitude;
    double longitude;
    ArrayList<String> dataArray = new ArrayList<>();
    void setEvent() {

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                try {
                    // Sử dụng Geocoder để tìm kiếm địa chỉ
                    List<Address> addressList = geocoder.getFromLocationName(query + "Da Nang", 1);

                    if (addressList != null && addressList.size() > 0) {
                        // Lấy địa chỉ đầu tiên từ danh sách
                        Address address = addressList.get(0);

                        // Lấy thông tin chi tiết về địa chỉ
                        String locality = address.getLocality(); // Thành phố
                        String country = address.getCountryName(); // Quốc gia
                        addressLine = address.getAddressLine(0); // Địa chỉ đầy đủ
                        latitude = address.getLatitude(); // Vĩ độ
                        longitude = address.getLongitude(); // Kinh độ



                        // Hiển thị thông tin địa chỉ
                        String result = "Locality: " + locality + "\nCountry: " + country +
                                "\nAddress: " + addressLine + "\nLatitude: " + latitude +
                                "\nLongitude: " + longitude;

                        Log.d("AddressInfo", result);

                        txt_address.setText("Địa chỉ: "+ addressLine);


                        // Hiển thị thông tin địa chỉ (tùy chọn)
                        // Tạo LatLng từ thông tin địa chỉ
                        LatLng markerLocation = new LatLng(latitude, longitude);

                        // Tạo MarkerOptions
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(markerLocation)
                                .title(locality) // Sử dụng thành phố làm tiêu đề marker (có thể thay đổi)
                                .snippet(addressLine); // Sử dụng địa chỉ đầy đủ làm mô tả marker (có thể thay đổi)

                        // Hiển thị marker trên bản đồ
                        mMap.clear();
                        mMap.addMarker(markerOptions);

                        // Đưa camera đến vị trí của marker và phóng to mức zoom mong muốn
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 14));

                        // Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("AddressInfo", "Address not found");
                        // Hiển thị thông báo lỗi (tùy chọn)
                        // Toast.makeText(context, "Address not found", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //googleMapService.searchAddress(getContext(), query + "Da Nang");
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
    TextView txt_address;
    EditText edt_name, edt_phone, edt_numbercar;
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


        googleMap = mMap;
        placesClient = Places.createClient(requireContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        setEvent();

        txt_address = view.findViewById(R.id.txt_address);
        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_numbercar = view.findViewById(R.id.edt_numbercar);
        btn_confirm = view.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                doneRescue();
                sendNotification();

                intent.putExtra("KEY_name_list", idField);
                intent.putExtra("KEY", "1");// Đặt key và giá trị cần truyền

                startActivity(intent);
                if(getActivity() != null) {
                    // Kết thúc (hoặc đóng) Activity chứa Fragment
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        return view;
    }
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    GenarateCharacter genarateCharacter;
    String idField;
    void doneRescue() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        String formattedDateTime = dateFormat.format(currentDate);
        String document = user.getUid();
        idField = formattedDateTime;

        // Tạo một ArrayList chứa thông tin
        dataArray.add(edt_name.getText().toString());
        dataArray.add(edt_phone.getText().toString());
        dataArray.add(formattedDateTime); // datetime
        dataArray.add(edt_numbercar.getText().toString());
        dataArray.add(addressLine);
        dataArray.add(String.valueOf(latitude));
        dataArray.add(String.valueOf(longitude));
        dataArray.add(user.getUid());

        //setCallingRescue(nameList);
        // Ghi ArrayList này vào Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("CallingForRescue").document(user.getUid())
                .set(Collections.singletonMap(idField, dataArray))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Thành công", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    void sendNotification() {
        String fcmServerKey = "AAAA-aEDMr4:APA91bFkulQb-yKqZHCdfMMvTnAWHu6eHSaFsPTkTiM4CN4nux4zGjFOpEnk_NXESGI3i98JmZX0AJj7tqyFsxmhhOU5AP4v0fHmxVNNA6olETuUvwhpCg6ip_0NT3kXa-eWUFeC0rP_";
        String receiverToken = "eImI4cXhSR-bWnQP84GKNe:APA91bHvlmJxjpwJggnIDvEAIlB3KA8bT6OBUDDjdoUFxEWzl-CS3vAQZWRhC5XE7Ca7WTUOGz6g8ltGfB0foaSIXRQOc4_FKkOGmVWbfMBUrZP0b3xwGmU9Sy6bJa6FhEUxiqhVL20R";
        String notificationTitle = idField +"_"+ user.getUid();
        String notificationBody = "Bạn ơi có người cần bạn giúp đỡ";

        // Gửi thông báo
        Post_Calling(fcmServerKey, receiverToken,notificationBody, notificationTitle);
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

}