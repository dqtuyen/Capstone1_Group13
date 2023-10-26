package com.example.capstone1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone1.Adapter.AdapterHorizontalScrollView;
import com.example.capstone1.Data.DataTest;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout horizontalLayout = view.findViewById(R.id.layout_horizontal_scroll_view);
        HorizontalScrollView horizontalScrollView = view.findViewById(R.id.horizontal_scroll_view);

        List<DataTest> carServiceList = new ArrayList<>();

        carServiceList.add(new DataTest("UID001", "Auto Care Center", "+1-123-456-7890", "autocare@example.com", "123 Main St, City", "Monday - Friday", "10", "Sedan", "4.8", R.drawable.img_welcom));
        carServiceList.add(new DataTest("UID002", "Speedy Repairs", "+1-987-654-3210", "speedy@example.com", "456 Elm St, Town", "Monday - Saturday", "8", "SUV", "4.5", R.drawable.img_welcom));
        carServiceList.add(new DataTest("UID003", "Pro Auto Shop", "+1-555-777-3333", "proauto@example.com", "789 Oak St, Village", "Monday - Sunday", "12", "Truck", "4.9", R.drawable.img_welcom));
        carServiceList.add(new DataTest("UID004", "City Car Service", "+1-111-222-3333", "citycar@example.com", "321 Pine St, County", "Tuesday - Saturday", "6", "Convertible", "4.7", R.drawable.img_welcom));
        carServiceList.add(new DataTest("UID005", "Green Auto Garage", "+1-888-888-8888", "greenauto@example.com", "654 Birch St, Suburb", "Monday - Saturday", "5", "Hybrid", "4.6", R.drawable.img_welcom));

        for (DataTest data : carServiceList) {
            View item = LayoutInflater.from(getActivity()).inflate(R.layout.item_rescuest, horizontalLayout, false);
            // Thay thế R.id.xxx bằng ID thực sự của các phần tử trong item_rescuest.xml
            ImageView itemImageView = item.findViewById(R.id.imageView);
            TextView txt_name = item.findViewById(R.id.edt_name);
            TextView txt_sdt = item.findViewById(R.id.edt_sdt);
            TextView txt_address = item.findViewById(R.id.edt_address);
            TextView txt_turns = item.findViewById(R.id.edt_turns);

            itemImageView.setImageResource(data.getImg());
            txt_name.setText(data.getName());
            txt_sdt.setText(data.getCall());
            txt_address.setText(data.getAddress());
            txt_turns.setText(data.getTurns());
            horizontalLayout.addView(item);
        }

        return view;
    }

    public void reloadData() {
        Toast.makeText(getActivity(), "Reload Fragment Home", Toast.LENGTH_SHORT).show();
    }
}