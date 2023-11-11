package com.example.capstone1.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.capstone1.Adapter.ViewPagerAdapter;
import com.example.capstone1.Fragment.HomeFragment;
import com.example.capstone1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    //BottomNavigationView
    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Button btn_find;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.view_pager);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        btn_find = findViewById(R.id.btn_find);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);

        checkedInformationReal(user.getUid());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_tab_1).setChecked(true);
                        loadVisible();
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_tab_2).setChecked(true);
                        loadVisible();
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_tab_3).setChecked(true);
                        loadVisible();
                        break;
                    case 3:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_tab_4).setChecked(true);
                        loadVisible();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_tab_1) {
                    mViewPager.setCurrentItem(0);
                    HomeFragment homeFragment = (HomeFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 0);
                    homeFragment.reloadData();
                    loadVisible();
                    return true;
                } else if (item.getItemId() == R.id.menu_tab_2) {
                    mViewPager.setCurrentItem(1);
                    loadVisible();
                    return true;
                } else if (item.getItemId() == R.id.menu_tab_3) {
                    mViewPager.setCurrentItem(2);
                    loadVisible();
                    return true;
                } else if (item.getItemId() == R.id.menu_tab_4) {
                    mViewPager.setCurrentItem(3);
                    loadVisible();
                    return true;
                }
                return false;
            }
        });


        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomNavigationView.getMenu().findItem(R.id.menu_tab_2).setChecked(true);
                mViewPager.setCurrentItem(1);

            }
        });


    }
    void loadVisible() {
        if(mViewPager.getCurrentItem() != 0) {
            btn_find.setVisibility(View.GONE);
        } else {
            btn_find.setVisibility(View.VISIBLE);
        }
    }


    public void checkedInformationReal(String uid) {
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

                    String phone = doc.getString("phone");
                    String numbercar = doc.getString("numbercar");

                    if(phone == "" || numbercar == "") {
                        Intent intent2 = new Intent(getApplicationContext(), UpdateProfile.class);
                        startActivity(intent2);
                    }

                } else {
                    // Xử lý lỗi
                }
            }
        });
    }


}