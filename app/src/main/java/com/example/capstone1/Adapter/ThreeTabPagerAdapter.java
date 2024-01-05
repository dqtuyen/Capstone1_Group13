package com.example.capstone1.Adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.capstone1.Fragment.AccountFragment;
import com.example.capstone1.Fragment.DocumentCompletedFragment;
import com.example.capstone1.Fragment.DocumentFailFragment;
import com.example.capstone1.Fragment.DocumentPresentFragment;

import java.util.ArrayList;
import java.util.List;

public class ThreeTabPagerAdapter extends FragmentPagerAdapter {
    final String[] tabTitles = {"Hiện tại", "Hoàn thành"};
    private final List<Fragment> mFragments = new ArrayList<>();

    public ThreeTabPagerAdapter(FragmentManager fm) {
        super(fm);

        // Thêm các fragment vào adapter
        mFragments.add(new DocumentPresentFragment());
        mFragments.add(new DocumentCompletedFragment());
        //mFragments.add(new DocumentFailFragment());
    }

    @Override
    public Fragment getItem(int position) {
        // Trả về fragment ở vị trí chỉ định
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        // Trả về số lượng fragment
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Trả về tiêu đề cho tab ở vị trí chỉ định
        return tabTitles[position];
    }
}