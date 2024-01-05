package com.example.capstone1.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.capstone1.R;

public class Done_Evaluated extends AppCompatActivity {

    Button btn_done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_evaluated);
        btn_done = findViewById(R.id.btn_done);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Done_Evaluated.this, MainActivity.class);
                intent.putExtra("KEY", 0);
                startActivity(intent);
                finish();
            }
        });

    }
}