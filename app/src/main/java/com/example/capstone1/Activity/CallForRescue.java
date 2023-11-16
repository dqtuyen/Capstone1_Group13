package com.example.capstone1.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.capstone1.R;

public class CallForRescue extends AppCompatActivity {

    ImageButton btn_back;
    Button btn_call_for_myself, btn_help_friend, btn_edit_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_for_rescue);

        btn_back = findViewById(R.id.btn_back);
        btn_call_for_myself = findViewById(R.id.btn_call_for_myself);
        btn_help_friend = findViewById(R.id.btn_help_friend);
        btn_edit_info = findViewById(R.id.btn_edit_info);
        setEvent();
    }

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