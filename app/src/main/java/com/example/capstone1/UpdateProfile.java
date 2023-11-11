package com.example.capstone1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {

    ImageView img_back;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    EditText edt_name, edt_email, edt_phone, edt_numbercar, edt_cartype, edt_location;
    Button btn_update;
    TextView txt_init;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        btn_update = findViewById(R.id.btn_update);
        img_back = findViewById(R.id.img_back);
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_phone = findViewById(R.id.edt_phone);
        edt_numbercar = findViewById(R.id.edt_numbercar);
        edt_cartype = findViewById(R.id.edt_typecar);
        edt_location = findViewById(R.id.edt_address);
        txt_init = findViewById(R.id.txt_init);


        init();
        setEvent();
        viewData(user.getUid());
    }

    private void setEvent() {
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    void init(){
        String text = "<b>Để sau</b>";
        txt_init.setText(Html.fromHtml(text));
    }
    void setData() {
        String uid = user.getUid().toString();

        Map<String, Object> data = new HashMap<>();
        data.put("email", edt_email.getText().toString());
        data.put("name", edt_name.getText().toString());
        data.put("phone", edt_phone.getText().toString());
        data.put("numbercar", edt_numbercar.getText().toString());
        data.put("typecar", edt_cartype.getText().toString());
        data.put("location", edt_location.getText().toString());


        db.collection("Users").document(uid)
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

    }

    public void viewData(String uid) {
        // Tạo đối tượng Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lấy dữ liệu của user
        DocumentReference docRef = db.collection("Users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Lấy dữ liệu của user
                    DocumentSnapshot doc = task.getResult();

                    // Hiển thị dữ liệu của user
                    String name = doc.getString("name");
                    String phone = doc.getString("phone");
                    String email = doc.getString("email");
                    String location = doc.getString("location");
                    String numbercar = doc.getString("numbercar");
                    String typecar = doc.getString("typecar");
                    String role = doc.getString("role");

                    edt_name.setText(name);
                    edt_phone.setText(phone);
                    edt_email.setText(email);
                    edt_numbercar.setText(numbercar);
                    edt_location.setText(location);
                    edt_cartype.setText(typecar);


                } else {
                    // Xử lý lỗi
                }
            }
        });
    }

}