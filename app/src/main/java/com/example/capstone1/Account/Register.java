package com.example.capstone1.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    EditText edt_name, edt_email, edt_password, edt_confirm_password, edt_phone;
    Button btn_login;
    ImageButton btn_back;
    TextView txt_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Ánh xạ ID
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_phone = findViewById(R.id.edt_phone);
        edt_password = findViewById(R.id.edt_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        btn_login = findViewById(R.id.btn_login);
        txt_view = findViewById(R.id.txt_view);
        btn_back = findViewById(R.id.btn_back01);

        init();
        setEvent();
    }
    void init(){
        String text = "Bạn có sẵn tài khoản để đăng nhập? <b>Đăng nhập</b>";
        txt_view.setText(Html.fromHtml(text));
    }

    void setEvent() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, email, password, re_password, phone;
                name = edt_name.getText().toString();
                email = edt_email.getText().toString();
                password = edt_password.getText().toString();
                re_password = edt_confirm_password.getText().toString();
                phone = edt_phone.getText().toString();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this,"Enter Email",Toast.LENGTH_SHORT);
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this,"Enter Password",Toast.LENGTH_SHORT);
                    return;
                }
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(Register.this,"Enter Name",Toast.LENGTH_SHORT);
                    return;
                }
                if(TextUtils.isEmpty(re_password)){
                    Toast.makeText(Register.this,"Enter Re-Password",Toast.LENGTH_SHORT);
                    return;
                }
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(Register.this,"Nhập số điện thoại",Toast.LENGTH_SHORT);
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Toast.makeText(Register.this, "Create Account success.", Toast.LENGTH_SHORT).show();
                            Adddatabase(email, password, name, phone);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Create Account fail.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void Adddatabase(String email, String password, String name, String phone){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(Register.this, "Authentication succes.", Toast.LENGTH_SHORT).show();
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            Map<String, Object> DataUser = new HashMap<>();
                            DataUser.put("email", email);
                            DataUser.put("name", name);
                            DataUser.put("phone", phone);
                            DataUser.put("numbercar", "");
                            DataUser.put("typecar", "");
                            DataUser.put("location", "");
                            DataUser.put("avgstar", "");
                            DataUser.put("numberofrescue", "0");
                            DataUser.put("role", "customer");
                            DataUser.put("img", "https://firebasestorage.googleapis.com/v0/b/capstone-7a4dc.appspot.com/o/icon_app.png?alt=media&token=821c8add-fb77-4227-872c-fd969e0adc71");

                            db.collection("Users").document(user.getUid())
                                    .set(DataUser)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Map<String, Object> dataLocation = new HashMap<>();
                                            dataLocation.put("latitude", "");
                                            dataLocation.put("longitude", "");
                                            dataLocation.put("address", "");
                                            dataLocation.put("address_name", "");
                                            db.collection("Location").document(user.getUid())
                                                    .set(dataLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                        }
                                                    });
                                            Intent intent = new Intent(getApplicationContext(), Login.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            e.printStackTrace();
                                            //Toast.makeText(Register.this, "Đăng kí thật bại", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            db.collection("RescueInformation")
                                    .document(user.getUid())
                                    .set(new HashMap<>()) // Truyền một Map rỗng
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}