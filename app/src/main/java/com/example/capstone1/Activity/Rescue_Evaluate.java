package com.example.capstone1.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.capstone1.R;

public class Rescue_Evaluate extends AppCompatActivity {
    ImageButton img_btn_star1, img_btn_star2, img_btn_star3, img_btn_star4, img_btn_star5, imageButtonView, btn_back;
    Button btn_send_evaluate, btn_cancel;
    EditText edt_vietdanhgia;
    TextView txt_name, txt_infoRescue;
    private int starCount = 0;
    String img, name, rescueinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_evaluate);

        img_btn_star1 = findViewById(R.id.img_btn_star1);
        img_btn_star2 = findViewById(R.id.img_btn_star2);
        img_btn_star3 = findViewById(R.id.img_btn_star3);
        img_btn_star4 = findViewById(R.id.img_btn_star4);
        img_btn_star5 = findViewById(R.id.img_btn_star5);
        btn_back = findViewById(R.id.btn_back);
        imageButtonView = findViewById(R.id.imageButtonView);
        btn_send_evaluate = findViewById(R.id.btn_send_evaluate);
        btn_cancel = findViewById(R.id.btn_cancel);
        edt_vietdanhgia = findViewById(R.id.edt_vietdanhgia);
        txt_name= findViewById(R.id.txt_name);
        txt_infoRescue = findViewById(R.id.txt_infoRescue);
        Intent intent = getIntent();
        if (intent != null) {
            img = intent.getStringExtra("RESimg");
            rescueinfo = intent.getStringExtra("RESinfoCar");
            name = intent.getStringExtra("RESname");
        } else {
            // Xử lý trường hợp intent là null
        }
        txt_name.setText(name);
        txt_infoRescue.setText(rescueinfo);
        setImage();
        setEventClickStar();
        setEvent();
    }
    void setImage() {

        Glide.with(this)
                .load(img)
                .circleCrop() // Áp dụng cắt ảnh thành hình tròn
                .into(imageButtonView);
    }
    void setEventClickStar() {
        img_btn_star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_btn_star1.setImageResource(R.drawable.one_star_orange);

                img_btn_star2.setImageResource(R.drawable.one_star_gray);
                img_btn_star3.setImageResource(R.drawable.one_star_gray);
                img_btn_star4.setImageResource(R.drawable.one_star_gray);
                img_btn_star5.setImageResource(R.drawable.one_star_gray);
                starCount = 1;
            }
        });
        img_btn_star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_btn_star1.setImageResource(R.drawable.one_star_orange);
                img_btn_star2.setImageResource(R.drawable.one_star_orange);

                img_btn_star3.setImageResource(R.drawable.one_star_gray);
                img_btn_star4.setImageResource(R.drawable.one_star_gray);
                img_btn_star5.setImageResource(R.drawable.one_star_gray);
                starCount = 2;
            }
        });
        img_btn_star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_btn_star1.setImageResource(R.drawable.one_star_orange);
                img_btn_star2.setImageResource(R.drawable.one_star_orange);
                img_btn_star3.setImageResource(R.drawable.one_star_orange);

                img_btn_star4.setImageResource(R.drawable.one_star_gray);
                img_btn_star5.setImageResource(R.drawable.one_star_gray);
                starCount = 3;
            }
        });
        img_btn_star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_btn_star1.setImageResource(R.drawable.one_star_orange);
                img_btn_star2.setImageResource(R.drawable.one_star_orange);
                img_btn_star3.setImageResource(R.drawable.one_star_orange);
                img_btn_star4.setImageResource(R.drawable.one_star_orange);

                img_btn_star5.setImageResource(R.drawable.one_star_gray);
                starCount = 4;
            }
        });
        img_btn_star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_btn_star1.setImageResource(R.drawable.one_star_orange);
                img_btn_star2.setImageResource(R.drawable.one_star_orange);
                img_btn_star3.setImageResource(R.drawable.one_star_orange);
                img_btn_star4.setImageResource(R.drawable.one_star_orange);
                img_btn_star5.setImageResource(R.drawable.one_star_orange);

                starCount = 5;
            }
        });
    }
    void setEvent() {
        btn_send_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(starCount == 0) {
                    Toast.makeText(Rescue_Evaluate.this, "Vui lòng chọn số sao trước khi đánh giá", Toast.LENGTH_SHORT).show();
                } else   if (TextUtils.isEmpty(edt_vietdanhgia.getText().toString().trim())) {
                    Toast.makeText(Rescue_Evaluate.this, "Vui lòng viết đánh giá", Toast.LENGTH_SHORT).show();
                } else {
                    setData();

                    Intent intent = new Intent(Rescue_Evaluate.this, Done_Evaluated.class);
                    startActivity(intent);
                    finish();

                }

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Rescue_Evaluate.this, MainActivity.class);
                intent.putExtra("KEY", 0);
                startActivity(intent);
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Rescue_Evaluate.this, MainActivity.class);
                intent.putExtra("KEY", 0);
                startActivity(intent);
                finish();
            }
        });
    }
    private void setData() {
        // Viết trường đẩy lên database ở đây
        Toast.makeText(this, starCount + edt_vietdanhgia.getText().toString(), Toast.LENGTH_SHORT).show();

    }
}