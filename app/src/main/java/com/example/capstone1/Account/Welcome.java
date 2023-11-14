package com.example.capstone1.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.capstone1.Activity.MainActivity;
import com.example.capstone1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Welcome extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Button btn_login;
    Button btn_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        btn_login =findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        checkSavedLoginInfo();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        btn_register.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        }));

        // Kiểm tra xem có token trong lưu trữ dự trữ hay không
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userToken = preferences.getString("userToken", null);

        if (userToken != null) {
            // Sử dụng token để xác thực người dùng
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // Người dùng đã được xác thực mà không cần đăng nhập lại
                goToMainActivity();
            }
        }
    }
    private void checkSavedLoginInfo() {
        SharedPreferences preferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String savedEmail = preferences.getString("email", null);
        String savedPassword = preferences.getString("password", null);

        if (savedEmail != null && savedPassword != null) {
            // Đã lưu thông tin đăng nhập, tự động đăng nhập
            signInWithSavedCredentials(savedEmail, savedPassword);
        }
    }
    private void signInWithSavedCredentials(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Đăng nhập thành công
                            goToMainActivity();
                        } else {
                            // Xử lý lỗi đăng nhập
                        }
                    }
                });
    }
    void goToMainActivity() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Welcome.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
        Intent intent = new Intent(Welcome.this, Intro.class);
        startActivity(intent);
        finish();
    }
}