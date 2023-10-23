package com.example.capstone1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;

public class ForgotPassword extends AppCompatActivity {

    Button btn_sendcode;
    ImageView btn_back;
    EditText edt_email;
    TextView txt_view;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        //Ánh xạ ID
        btn_sendcode = findViewById(R.id.btn_sendcode);
        btn_back = findViewById(R.id.btn_back);
        edt_email = findViewById(R.id.edt_email);
        txt_view = findViewById(R.id.txt_view);
        setEvent();
        init();
    }
    void init(){
        String text = "Tôi đã nhớ mật khẩu? <b>Đăng nhập</b>";
        txt_view.setText(Html.fromHtml(text));
    }
    void setEvent() {
        btn_sendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString();
                if(!email.contains("@")) {
                    Toast.makeText(ForgotPassword.this, "Vui lòng nhập đúng định dạng email", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Gửi email thành công.
                                        // Người dùng sẽ nhận được một email chứa liên kết để khôi phục mật khẩu.
                                    } else {
                                        // Xảy ra lỗi khi gửi email khôi phục.
                                        // Hiển thị thông báo hoặc xử lý lỗi ở đây.
                                    }
                                }
                            });
                    auth.fetchSignInMethodsForEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    if (task.isSuccessful()) {
                                        SignInMethodQueryResult result = task.getResult();
                                        List<String> signInMethods = result.getSignInMethods();

                                        if (signInMethods != null && !signInMethods.isEmpty()) {
                                            // Địa chỉ email đã tồn tại trên hệ thống Firebase Authentication.
                                            // signInMethods chứa các phương thức đăng nhập có thể sử dụng với địa chỉ email này.
                                            Intent intent = new Intent(getApplicationContext(), PasswordChanged.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(ForgotPassword.this, "Vui lòng nhập đúng email của bạn", Toast.LENGTH_SHORT).show();
                                            // Địa chỉ email không tồn tại trên hệ thống Firebase Authentication.
                                        }
                                    } else {
                                        // Xảy ra lỗi trong quá trình kiểm tra.
                                        Exception e = task.getException();
                                        // Xử lý lỗi tại đây.
                                    }
                                }
                            });
                }


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

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }


}