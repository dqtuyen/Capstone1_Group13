package com.example.capstone1.Activity;

import static com.example.capstone1.MyFirebaseMessagingService.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Receive extends AppCompatActivity implements OnMapReadyCallback {

    TextView txt_name, txt_phone, txt_numcar, txt_typecar, txt_address;
    Button btn_receive, btn_cancel;
    ImageButton btn_back;
    String longitude, latitude, address, uidCustomer, idField, name, description;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private GoogleMapService googleMapService;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    List<DataLocation> locationList = new ArrayList<>();

    DataCallingForRescue dataCallingForRescue;
    Map<Integer, String> info = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        txt_address = findViewById(R.id.txt_address);
        txt_name = findViewById(R.id.txt_name);
        txt_phone = findViewById(R.id.txt_phone);
        txt_numcar = findViewById(R.id.txt_numcar);
        txt_typecar = findViewById(R.id.txt_typecar);
        btn_back = findViewById(R.id.btn_back);
        btn_receive = findViewById(R.id.btn_receive);
        btn_cancel = findViewById(R.id.btn_cancel);

        Intent intent = getIntent();
        if (intent != null) {
            uidCustomer = intent.getStringExtra("UID_CUSTOMER");
            address = intent.getStringExtra("ADDRESS");
            latitude = intent.getStringExtra("LATITUDE");
            longitude = intent.getStringExtra("LONGITUDE");
            idField = intent.getStringExtra("ID_FIELD");
            name = intent.getStringExtra("NAME");
            description = intent.getStringExtra("DESCRIPTION");
            Log.d("LOG",latitude + longitude);
        } else {

        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        // Liên kết với sự kiện OnMapReadyCallback
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        GenarateCharacter genarateCharacter = new GenarateCharacter();
        String new_address = genarateCharacter.trimmedCustom(address);
        locationList.add(new DataLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), new_address, name+" đang ở đây!"));
        setEvent();
        viewData();
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMapService = new GoogleMapService(googleMap, fusedLocationProviderClient, this);
        googleMapService.myLocation();

        mMap = googleMap;
        googleMapService.addMarkerAndShowInformation(locationList, mMap);
        LatLng currentLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));


    }
    String img, numbercar, typecar;
    void viewData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lấy dữ liệu của user
        DocumentReference docRef = db.collection("Users").document(uidCustomer);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Lấy dữ liệu của user
                    DocumentSnapshot doc = task.getResult();

                    // Hiển thị dữ liệu của user
                    String name = doc.getString("name");
                    String phone = doc.getString("phone");
                    numbercar = doc.getString("numbercar");
                    typecar = doc.getString("typecar");
                    img = doc.getString("img");

                    txt_address.setText(address);
                    txt_name.setText(name);
                    txt_numcar.setText(numbercar);
                    txt_phone.setText(phone);
                    txt_typecar.setText(typecar);
                } else {
                    // Xử lý lỗi
                }
            }
        });
    }

    void setEvent() {
        btn_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataInfo(uidCustomer);
                Intent intent = new Intent(Receive.this, Test_Process.class);
                intent.putExtra("NAME", name);
                intent.putExtra("UID_CUSTOMER", uidCustomer);
                intent.putExtra("ID_FIELD", idField);
                intent.putExtra("LATITUDE", latitude);
                intent.putExtra("LONGITUDE", longitude);
                intent.putExtra("ADDRESS", address);
                intent.putExtra("IMG", img);
                intent.putExtra("INFOCAR", numbercar + " • " + typecar);
                intent.putExtra("DESCRIPTION", description);
                startActivity(intent);
                finish();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    void deleteDocument() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("CallingForRescue").document(uidCustomer);

        documentReference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error deleting document", e);
                    }
                });
    }
    void doneRescue() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        String formattedDateTime = dateFormat.format(currentDate);
        String document = user.getUid();
        String nameList = idField + Status.ON;

        // Tạo một ArrayList chứa thông tin
        ArrayList<String> dataArray = new ArrayList<>();
        dataArray.add(dataCallingForRescue.getName()); //name
        dataArray.add(dataCallingForRescue.getPhone()); //phone
        dataArray.add(dataCallingForRescue.getTime()); // datetime
        dataArray.add(dataCallingForRescue.getDescription()); // description
        dataArray.add(dataCallingForRescue.getAddress()); // address
        dataArray.add(dataCallingForRescue.getLatitude()); // latitude
        dataArray.add(dataCallingForRescue.getLongitude()); // longitude
        dataArray.add(dataCallingForRescue.getImg()); // myuid
        dataArray.add(dataCallingForRescue.getUidCustomer()); // myuid
        dataArray.add(dataCallingForRescue.getUidRescue()); // rescueuid

        //setCallingRescue(nameList);
        // Ghi ArrayList này vào Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("RescueInformation").document(user.getUid())
                .update(Collections.singletonMap(nameList, dataArray))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Receive.this, "Thành công", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getDataInfo(String uidCalling) {
        // Thực hiện xử lý với dữ liệu từ Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("CallingForRescue").document(uidCalling)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Xử lý dữ liệu của document sau khi nó đã được lấy về
                            List<Object> yourArray = (List<Object>) document.getData().entrySet().iterator().next().getValue();
                            Log.d("LOG", document.getData().keySet().toString());

                            if (yourArray != null) {
                                int i = 0;
                                for (Object item : yourArray) {

                                    // Xử lý từng phần tử trong mảng
                                    Log.d("Firestore", "Item: " + item.toString());

                                    info.put(i++, item.toString());
                                    // Đây là nơi bạn có thể thực hiện logic xử lý với từng phần tử trong mảng
                                }

                                for (Map.Entry<Integer, String> entry : info.entrySet()) {
                                    Integer key = entry.getKey();
                                    String value = entry.getValue();
                                    Log.d("Firestore", "Key: " + key + ", Value: " + value);
                                }

                                dataCallingForRescue = new DataCallingForRescue(info.get(0),info.get(1),info.get(2),info.get(3), info.get(4), info.get(5), info.get(6),img, info.get(7), user.getUid());
                                Log.d("LOG", dataCallingForRescue.toString());
                                doneRescue();
                                deleteDocument();
                            } else {
                                Log.d("Firestore", "Mảng không tồn tại hoặc rỗng.");
                            }
                        } else {
                            Log.d("Firestore", "Document không tồn tại.");
                        }
                    } else {
                        Log.w("Firestore", "Error getting document.", task.getException());
                    }
                });
    }


}