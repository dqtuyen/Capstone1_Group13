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
import com.google.android.material.textfield.TextInputLayout;
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

                //name
                if(!isFirstCharUppercase(name)){
                    Toast.makeText(Register.this, "Các chữ cái đầu của tên phải viết in hoa", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(hasNumber(name)){
                    Toast.makeText(Register.this, "Tên không thể chứa số", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(name.length() < 5){
                    Toast.makeText(Register.this, "Tên phải có hơn 5 kí tự", Toast.LENGTH_SHORT).show();
                    return;
                }


                //Email
                if(!email.contains("@gmail.com")){
                    Toast.makeText(Register.this, "Vui lòng nhập đúng định dạng Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isValidEmail(email)){
                    Toast.makeText(Register.this, "Vui lòng nhập đúng định dạng Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Phone
                if(!isValidPhoneNumber(phone)){
                    Toast.makeText(Register.this, "Số điện thoại phải có 10 chữ số", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Password
                if(password.length() < 5){
                    Toast.makeText(Register.this, "Mật khẩu phải có hơn 5 kí tự", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!hasLetterAndNumber(password)){
                    Toast.makeText(Register.this, "Mật khẩu phải có cả chữ và số", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Re-password
                if(!password.equals(re_password)){
                    Toast.makeText(Register.this, "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
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

    private boolean isFirstCharUppercase(String name) {
        return !name.isEmpty() && Character.isUpperCase(name.charAt(0));
    }
    private boolean hasNumber(String name) {
        return name.matches(".*\\d.*");
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern) && !email.contains(" ") && !email.contains("â");
    }
    private boolean isValidPhoneNumber(String phone) {
        return phone.length() == 10;
    }
    private boolean hasLetterAndNumber(String password) {
        return password.matches("^(?=.*[a-zA-Z])(?=.*\\d).+$");
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
                            DataUser.put("role", "customer");

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
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}