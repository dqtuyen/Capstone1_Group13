package com.example.capstone1;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

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
        // Tên tài nguyên hình ảnh hoặc URL hình ảnh cần hiển thị
        String imageUrl = "https://scontent.fdad3-5.fna.fbcdn.net/v/t39.30808-6/369010065_1746686709081571_4477404930095956354_n.jpg?_nc_cat=106&ccb=1-7&_nc_sid=5f2048&_nc_ohc=4b-YSe1e7K8AX9yMnod&_nc_ht=scontent.fdad3-5.fna&cb_e2o_trans=t&oh=00_AfBj4wv-u-t91U7i1rUDZKmlre4RwEjsm_fWd4fYOymuIg&oe=653D48A9";

        Glide.with(this)
                .load(imageUrl)
                .circleCrop() // Áp dụng cắt ảnh thành hình tròn
                .into(imageButtonView);
        logout();
        return view;
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
        btn_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Welcome.class);
                startActivity(intent);
            }
        });
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