package com.example.capstone1.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.capstone1.R;

public class Processing extends AppCompatActivity {
    String name, phone, typecar, numbercar, img;
    TextView txt_name, txt_phoneNumber, txt_numcar, txt_typecar;
    Button btn_done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);

        //Ánh xạ ID
        txt_name = findViewById(R.id.txt_name);
        txt_phoneNumber = findViewById(R.id.txt_phoneNumber);
        txt_numcar = findViewById(R.id.txt_numcar);
        txt_typecar = findViewById(R.id.txt_typecar);
        btn_done = findViewById(R.id.btn_done);
        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra("NAME");
            phone = intent.getStringExtra("PHONE");
            typecar= intent.getStringExtra("TYPECAR");
            numbercar = intent.getStringExtra("NUMBERCAR");
            img = intent.getStringExtra("IMG");
        } else {
            // Xử lý trường hợp intent là null
        }
        txt_name.setText(name);
        txt_phoneNumber.setText(phone);
        txt_numcar.setText(typecar);
        txt_typecar.setText(numbercar);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Processing.this, Rescue_Evaluate.class);
                intent.putExtra("IMG", img);
                //intent.putExtra("KEY", 0);
                startActivity(intent);
                finish();
            }
        });

    }
}