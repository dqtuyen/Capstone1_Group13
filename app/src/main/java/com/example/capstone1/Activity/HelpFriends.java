package com.example.capstone1.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HelpFriends extends AppCompatActivity {

    EditText edt_name2, edt_phone2, edt_numbercar2, edt_location2;
    FirebaseUser myFriends = FirebaseAuth.getInstance().getCurrentUser();
    Button btn_Confirm_information;
    ImageView btn_back01;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_friends);

        edt_name2 = findViewById(R.id.edt_name2);
        edt_phone2 = findViewById(R.id.edt_phone2);
        edt_numbercar2 = findViewById(R.id.edt_numbercar2);
        edt_location2 = findViewById(R.id.edt_location2);
        btn_Confirm_information = findViewById(R.id.btn_Confirm_information);
        btn_back01 = findViewById(R.id.btn_back01);

        // Xử lý sự kiện khi nút "Confirm Information" được nhấn
        btn_Confirm_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });

        // Xử lý sự kiện khi nút "Back" được nhấn
        btn_back01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    void setData() {
        if (myFriends != null) {
            String uid = myFriends.getUid();

            Map<String, Object> data = new HashMap<>();
            data.put("name", edt_name2.getText().toString());
            data.put("phone", edt_phone2.getText().toString());
            data.put("numbercar", edt_numbercar2.getText().toString());
            data.put("location", edt_location2.getText().toString());

            // Thực hiện ghi dữ liệu vào Firestore
            db.collection("MyFriends").document(uid)
                    .set(data)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to update data", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//    void doneRescueFriend() {
//        Date currentDate = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
//        String formattedDateTime = dateFormat.format(currentDate);
//        String document = user.getUid();
//        String nameList = idField + Status.ON;
//
//        // Tạo một ArrayList chứa thông tin
//        ArrayList<String> dataArray = new ArrayList<>();
//
//        //setCallingRescue(nameList);
//        // Ghi ArrayList này vào Firebase
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("RescueInformation").document(user.getUid())
//                .update(Collections.singletonMap(nameList, dataArray))
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(Receive.this, "Thành công", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        e.printStackTrace();
//                        Toast.makeText(getApplicationContext(), "Thất bại", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}