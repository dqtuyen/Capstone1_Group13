package com.example.capstone1.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.capstone1.Adapter.CustomerAdapter;
import com.example.capstone1.Data.DataCallingForRescue;
import com.example.capstone1.Data.DataUser;
import com.example.capstone1.GenarateCharacter;
import com.example.capstone1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RescueCallingList extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView img_back;
    ArrayList<DataUser> userList = new ArrayList<>();
    ArrayList<DataCallingForRescue> dataCallingForRescues = new ArrayList<>();
    Map<Integer, String> info = new HashMap<>();
    CustomerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_calling_list);

        recyclerView = findViewById(R.id.recyclerView);
        img_back = findViewById(R.id.img_back);



//        // Adding 5 users to the ArrayList
//        userList.add(new DataUser("UID1", "Alice", "1234567890", "123 Main St", "Description 1", "9:00 AM", "ABC123", "Sedan", "img1.jpg"));
//        userList.add(new DataUser("UID2", "Bob", "9876543210", "456 Elm St", "Description 2", "10:30 AM", "DEF456", "SUV", "img2.jpg"));
//        userList.add(new DataUser("UID3", "Charlie", "1112223333", "789 Oak St", "Description 3", "12:00 PM", "GHI789", "Truck", "img3.jpg"));
//        userList.add(new DataUser("UID4", "David", "4445556666", "321 Pine St", "Description 4", "2:00 PM", "JKL012", "Convertible", "img4.jpg"));
//        userList.add(new DataUser("UID5", "Eva", "7778889999", "654 Cedar St", "Description 5", "4:30 PM", "MNO345", "Hatchback", "img5.jpg"));


        getAllDocumentsAndGetDataInfo();
        adapter = new CustomerAdapter(getApplicationContext(),dataCallingForRescues);
        adapter.setOnItemClickListener((position, address) -> {
            Log.d("RecyclerView", "Clicked position: " + position + address);
            // Xử lý logic khi bấm vào item ở vị trí position ở đây

            DataCallingForRescue firstElement = dataCallingForRescues.get(0);
            String uid_customer = firstElement.getUidCustomer();
            String latitude = firstElement.getLatitude();
            String longitude =  firstElement.getLongitude();
            String name =  firstElement.getName();
            String description = firstElement.getDescription();
            String id_field = listIdField.get(position);
            Log.e("LOG", id_field);
            Intent intent = new Intent(this, Receive.class);
            intent.putExtra("NAME", name);
            intent.putExtra("UID_CUSTOMER", uid_customer);
            intent.putExtra("ID_FIELD", id_field);
            intent.putExtra("LATITUDE", latitude);
            intent.putExtra("LONGITUDE", longitude);
            intent.putExtra("ADDRESS", address);
            intent.putExtra("DESCRIPTION", description);
            startActivity(intent);
        });



        setEvent();
    }
    ArrayList<String> listRescueCalling = new ArrayList<>();
    ArrayList<String> listIdField = new ArrayList<>();
    private void getAllDocumentsAndGetDataInfo() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("CallingForRescue")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documentList = task.getResult().getDocuments();

                        for (DocumentSnapshot document : documentList) {
                            Log.d("Firestore", "Document ID: " + document.getId());
                            listRescueCalling.add(document.getId());
                            getDataInfoPosition(document.getId());
                        }

                        // Khi đã lấy được danh sách document, gọi getDataInfoPosition
                        // ở đây để xử lý với dữ liệu từ Firestore
                         // Gọi phương thức xử lý dữ liệu ở đây
                    } else {
                        Log.w("Firestore", "Lỗi khi lấy documents.", task.getException());
                    }
                });
    }
    GenarateCharacter genarateCharacter = new GenarateCharacter();
    String img;
    private void getDataInfoPosition(String uidCalling) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(uidCalling);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                String img = doc.getString("img");

                // Sau khi lấy ảnh thành công, tiến hành các thao tác khác với dữ liệu
                db.collection("CallingForRescue").document(uidCalling)
                        .get()
                        .addOnCompleteListener(callForRescueTask -> {
                            if (callForRescueTask.isSuccessful()) {
                                DocumentSnapshot document = callForRescueTask.getResult();
                                if (document.exists()) {
                                    List<Object> yourArray = (List<Object>) document.getData().entrySet().iterator().next().getValue();
                                    Log.d("LOG", document.getData().keySet().toString());
                                    listIdField.add(genarateCharacter.removeBrackets(document.getData().keySet().toString()));
                                    if (yourArray != null) {
                                        int i = 0;
                                        Map<Integer, String> info = new HashMap<>();
                                        Log.d("Firestore", "aaaa" + yourArray.toString());
                                        for (Object item : yourArray) {
                                            Log.d("Firestore", "Item: " + item.toString());
                                            info.put(i++, item.toString());
                                        }

                                        for (Map.Entry<Integer, String> entry : info.entrySet()) {
                                            Integer key = entry.getKey();
                                            String value = entry.getValue();
                                            Log.d("Firestore", "Key: " + key + ", Value: " + value);
                                        }

                                        dataCallingForRescues.add(new DataCallingForRescue(
                                                info.get(0), info.get(1), info.get(2),
                                                info.get(3), info.get(4), info.get(5),
                                                info.get(6), img, info.get(7), ""
                                        ));

                                        recyclerView.setAdapter(adapter);
                                    } else {
                                        Log.d("Firestore", "Mảng không tồn tại hoặc rỗng.");
                                    }
                                } else {
                                    Log.d("Firestore", "Document không tồn tại.");
                                }
                            } else {
                                Log.w("Firestore", "Error getting document.", callForRescueTask.getException());
                            }
                        });
            } else {
                Log.w("Firestore", "Error getting document.", task.getException());
            }
        });
    }

    void setEvent() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void onBackPressed() {
        finish();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        IntentFilter intentFilter = new IntentFilter("com.example.capstone1.MyFirebaseMessagingService");
//        registerReceiver(messageReceiver, intentFilter);
//    }
}