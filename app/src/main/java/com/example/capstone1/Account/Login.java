package com.example.capstone1.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone1.Activity.MainActivity;
import com.example.capstone1.Activity.Role;
import com.example.capstone1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    FirebaseUser user;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db;
    EditText edt_email_login, edt_password;
    Button btn_login;
    ImageView btn_back;
    TextView txt_forgotpassword, txt_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Ánh xạ ID
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        edt_email_login = findViewById(R.id.edt_email_login);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);
        btn_back = findViewById(R.id.btn_back01);
        txt_forgotpassword = findViewById(R.id.txt_forgotpassword);
        txt_view = findViewById(R.id.txt_view);
        init();
        setEvent();
    }

    void init(){
        String text = "Bạn chưa có tài khoản? <b>Đăng ký ngay</b>";
        txt_view.setText(Html.fromHtml(text));
    }
    void setEvent() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = edt_email_login.getText().toString();
                password = edt_password.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this,"Enter Email",Toast.LENGTH_SHORT);
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this,"Enter Password",Toast.LENGTH_SHORT);
                    return;
                }

                //email
                if(!email.contains("@gmail.com")){
                    Toast.makeText(Login.this, "Vui lòng nhập đúng định dạng Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isValidEmail(email)){
                    Toast.makeText(Login.this, "Vui lòng nhập đúng định dạng Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Password
                if(password.length() < 5){
                    Toast.makeText(Login.this, "Mật khẩu phải có hơn 5 kí tự", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!hasLetterAndNumber(password)){
                    Toast.makeText(Login.this, "Mật khẩu phải có cả chữ và số", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            saveLoginInfo(email, password);
                            goToMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });

        txt_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Welcome.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Lưu thông tin đăng nhập sau khi đăng nhập thành công
    private void saveLoginInfo(String email, String password) {
        SharedPreferences preferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    //Email không được có khoảng trống và dấu
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern) && !email.contains(" ") && !email.contains("â");
    }

    private boolean hasLetterAndNumber(String password) {
        return password.matches("^(?=.*[a-zA-Z])(?=.*\\d).+$");
    }
    void goToMainActivity() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
        Intent intent = new Intent(getApplicationContext(), Intro.class);
        startActivity(intent);
        finish();



    }
}
