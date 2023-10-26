package com.example.capstone1;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.capstone1.Data.DataTest;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
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
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
    ImageButton imageButtonView;
    Button btn_logout, btn_information, btn_history, btn_update_role;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    List<DataTest> carServiceList = new ArrayList<>();
    TextView txt_call, txt_email, txt_address, txt_numcar, txt_type_moto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        imageButtonView = view.findViewById(R.id.imageButtonView);
        btn_logout = view.findViewById(R.id.btn_logout);
        btn_information = view.findViewById(R.id.btn_information);
        btn_history = view.findViewById(R.id.btn_history);
        btn_update_role = view.findViewById(R.id.btn_update_role);

        txt_call = view.findViewById(R.id.txt_call);
        txt_email = view.findViewById(R.id.txt_gmail);
        txt_address = view.findViewById(R.id.txt_address);
        txt_numcar = view.findViewById(R.id.txt_numcar);
        txt_type_moto = view.findViewById(R.id.txt_typecar);
        // Tên tài nguyên hình ảnh hoặc URL hình ảnh cần hiển thị
        String imageUrl = "https://img.freepik.com/free-vector/businessman-character-avatar-isolated_24877-60111.jpg?w=740&t=st=1698340797~exp=1698341397~hmac=7fb261f6da08e5edd433994a215dff773601a0649d258b694b10ff1e6d0dbed0";

        Glide.with(this)
                .load(imageUrl)
                .circleCrop() // Áp dụng cắt ảnh thành hình tròn
                .into(imageButtonView);
        data();
        setData();
        logout();
        setEvent();
        return view;
    }
    void setEvent() {
        btn_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateProfile.class);
                startActivity(intent);
            }
        });
        btn_update_role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateRole.class);
                startActivity(intent);
            }
        });
    }
    void setData() {

        txt_call.setText(carServiceList.get(1).getCall());
        txt_email.setText(carServiceList.get(1).getEmail());
        txt_address.setText(carServiceList.get(1).getAddress());
        txt_numcar.setText(carServiceList.get(1).getNum_car());
        txt_type_moto.setText(carServiceList.get(1).getType_car());
    }
    void logout() {
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                clearLoginInfo();
                Intent intent = new Intent(getContext(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });



    }

    private void data() {
        carServiceList.add(new DataTest("UID001", "Auto Care Center", "+1-123-456-7890", "autocare@example.com", "123 Main St, City", "Monday - Friday", "10", "Sedan", "4.8", R.drawable.back));
        carServiceList.add(new DataTest("UID002", "Speedy Repairs", "+1-987-654-3210", "speedy@example.com", "456 Elm St, Town", "Monday - Saturday", "8", "SUV", "4.5", R.drawable.back));
        carServiceList.add(new DataTest("UID003", "Pro Auto Shop", "+1-555-777-3333", "proauto@example.com", "789 Oak St, Village", "Monday - Sunday", "12", "Truck", "4.9", R.drawable.back));
        carServiceList.add(new DataTest("UID004", "City Car Service", "+1-111-222-3333", "citycar@example.com", "321 Pine St, County", "Tuesday - Saturday", "6", "Convertible", "4.7", R.drawable.back));
        carServiceList.add(new DataTest("UID005", "Green Auto Garage", "+1-888-888-8888", "greenauto@example.com", "654 Birch St, Suburb", "Monday - Saturday", "5", "Hybrid", "4.6", R.drawable.back));

    }
    private void clearLoginInfo() {
        Context context = requireActivity();
        SharedPreferences preferences = context.getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("email");
        editor.remove("password");
        editor.apply();
    }
}