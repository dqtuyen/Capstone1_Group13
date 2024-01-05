package com.example.capstone1.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone1.GlobalData;
import com.example.capstone1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CallForRescue extends AppCompatActivity {

    ImageButton btn_back;
    Button btn_call_for_myself, btn_help_friend, btn_edit_info;
    TextView txt_name, txt_phone, txt_numcar, txt_typecar;
    String name, phone, typecar, numbercar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_for_rescue);

        btn_back = findViewById(R.id.btn_back01);
        btn_call_for_myself = findViewById(R.id.btn_call_for_myself);
        btn_help_friend = findViewById(R.id.btn_help_friend);
        btn_edit_info = findViewById(R.id.btn_edit_info);
        txt_name = findViewById(R.id.txt_name);
        txt_phone = findViewById(R.id.txt_phone);
        txt_numcar = findViewById(R.id.txt_numcar);
        txt_typecar = findViewById(R.id.txt_typecar);
        setEvent();

        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra("NAME");
            phone = intent.getStringExtra("PHONE");
            typecar= intent.getStringExtra("TYPECAR");
            numbercar = intent.getStringExtra("NUMBERCAR");

            txt_name.setText(name);
            txt_phone.setText(phone);
            txt_typecar.setText(typecar);
            txt_numcar.setText(numbercar);
        } else {
            // Xử lý trường hợp intent là null
        }

    }
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    private void setEvent() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_call_for_myself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConfirmLocation.class);
                intent.putExtra("NAME", name);
                intent.putExtra("PHONE", phone);
                intent.putExtra("TYPECAR", typecar);
                intent.putExtra("NUMBERCAR", numbercar);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        btn_help_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), HelpFriends.class);
                startActivity(intent);
                finish();
            }
        });

        btn_edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), UpdateProfile.class);
                startActivity(intent);
                finish();
            }
        });


    }
}