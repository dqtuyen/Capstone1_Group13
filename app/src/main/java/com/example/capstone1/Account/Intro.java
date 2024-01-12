package com.example.capstone1.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.capstone1.R;

public class Intro extends AppCompatActivity {
    ImageView img_rescue, img_logo, img_cloud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        img_rescue = findViewById(R.id.img_rescue);
        img_logo = findViewById(R.id.img_logo);
        img_cloud = findViewById(R.id.img_cloud);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setRepeatCount(AnimationSet.INFINITE);
        animationSet.setRepeatMode(AnimationSet.REVERSE);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.1f, 1, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(3000);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0.8f);
        alphaAnimation.setDuration(3000);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        img_cloud.startAnimation(animationSet);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        img_rescue.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish(); // Kết thúc IntroActivity sau khi hiển thị xong
                //Toast.makeText(Intro.this, "finish", Toast.LENGTH_SHORT).show();
            }
        }, 3000);


    }

}