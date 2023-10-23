package com.example.capstone1;

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
    EditText edt_name, edt_email, edt_password, edt_confirm_password;
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
        edt_password = findViewById(R.id.edt_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        btn_login = findViewById(R.id.btn_login);
        txt_view = findViewById(R.id.txt_view);
        btn_back = findViewById(R.id.btn_back);
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
                String name, email, password, re_password;
                name = edt_name.getText().toString();
                email = edt_email.getText().toString();
                password = edt_password.getText().toString();
                re_password = edt_confirm_password.getText().toString();

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

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Toast.makeText(Register.this, "Create Account success.", Toast.LENGTH_SHORT).show();
                            Adddatabase(email, password, name);
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

    void Adddatabase(String email, String password, String name){

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


                            db.collection("Users").document(user.getUid())
                                    .set(DataUser)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
//                                            Map<String, Object> DataSaved = new HashMap<>();
//                                            DataSaved.put("Post_Saved", Arrays.asList(""));
//                                            DataSaved.put("Post_Posted", Arrays.asList(""));
//                                            db.collection("Saved").document(user.getUid())
//                                                    .set(DataSaved).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//
//                                                        }
//                                                    });
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
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}