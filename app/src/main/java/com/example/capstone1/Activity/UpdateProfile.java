package com.example.capstone1.Activity;

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

import com.example.capstone1.Account.Register;
import com.example.capstone1.R;
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
    EditText  edt_email, edt_phone, edt_numbercar, edt_cartype, edt_location;

    EditText edt_name;
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

        Toast.makeText(this, user.getUid().toString(), Toast.LENGTH_SHORT).show();

        init();
        setEvent();
        viewData(user.getUid());
    }

    private void setEvent() {
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Thư - NAME
                String name, email,phone;
                name = edt_name.getText().toString();
                email = edt_email.getText().toString();
                phone = edt_phone.getText().toString();

                if(name.length() < 5){
                    Toast.makeText(UpdateProfile.this, "Tên phải có hơn 5 kí tự", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isFirstCharUppercase(name)){
                    Toast.makeText(UpdateProfile.this, "Các chữ cái đầu của tên phải viết in hoa", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(hasNumber(name)){
                    Toast.makeText(UpdateProfile.this, "Tên không thể chứa số", Toast.LENGTH_SHORT).show();
                    return;
                }


                //email
                if(!email.contains("@gmail.com")){
                    Toast.makeText(UpdateProfile.this, "Vui lòng nhập đúng định dạng Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isValidEmail(email)){
                    Toast.makeText(UpdateProfile.this, "Vui lòng nhập đúng định dạng Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Phone
                if(!isValidPhoneNumber(phone)){
                    Toast.makeText(UpdateProfile.this, "Số điện thoại phải có 10 chữ số", Toast.LENGTH_SHORT).show();
                    return;
                }

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

    //Viết hoa chữ cái
    private boolean isFirstCharUppercase(String name) {
        return !name.isEmpty() && Character.isUpperCase(name.charAt(0));
    }

    //Không được có khoảng cách và dấu
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern) && !email.contains(" ") && !email.contains("â");
    }

    //Tên không được chưa số
    private boolean hasNumber(String name) {
        return name.matches(".*\\d.*");
    }

    //Phone phải đủ 10 số
    private boolean isValidPhoneNumber(String phone) {
        return phone.length() == 10;
    }

    //Nhập đủ biên số xe
    //private boolean isValidNumberCar(String numbercar) {
        // Ví dụ:"43C-12345" hoặc "43C-1234"
      //  return numbercar.matches("[A-Z]{2}-\\d{4,5}");
    //}
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
                .update(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Update thành công", Toast.LENGTH_SHORT).show();
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