package com.example.capstone1.Fragment;

import static android.content.ContentValues.TAG;

import static com.example.capstone1.FcmNotificationSender.Post_Calling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.capstone1.Activity.CallForRescue;
import com.example.capstone1.Activity.RescueCallingList;
import com.example.capstone1.Activity.Rescue_Evaluate;
import com.example.capstone1.Activity.Test_Process;
import com.example.capstone1.Data.DataUser;
import com.example.capstone1.GoogleMapService;
import com.example.capstone1.OnAddressReceivedListener;
import com.example.capstone1.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback {

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
    // TODO: Rename and change types and number of parameter
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
    private FusedLocationProviderClient fusedLocationProviderClient;
    GoogleMapService googleMapService;
    LinearLayout horizontalLayout;
    ImageButton img_ring, img_chatbotgpt, img_test;
    ImageView img_avt;
    TextView txt_name;
    private AlertDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        //Ánh xạ ID
        // Khởi tạo SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        horizontalLayout = view.findViewById(R.id.layout_horizontal_scroll_view);
        HorizontalScrollView horizontalScrollView = view.findViewById(R.id.horizontal_scroll_view);
        img_ring = view.findViewById(R.id.img_ring);
        img_chatbotgpt = view.findViewById(R.id.img_chatbotgpt);
        img_test = view.findViewById(R.id.img_test);
        txt_name = view.findViewById(R.id.txt_name);
        img_avt = view.findViewById(R.id.img_avt);

        Bundle receivedBundle = getArguments();
        if (receivedBundle != null) {
            String receivedData = receivedBundle.getString("key"); // Thay "key" bằng tên key của dữ liệu
            String name = receivedBundle.getString("myName");
            Log.d("TEST",receivedData + name);
        }

        viewData_cuuhonhieunhat();
        setEvent();
        getMyUser();


        return view;
    }

    private void checkListCallingRescue() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Thực hiện lấy danh sách các documents từ collection "collectionCallingForRescue" trong Firestore
        CollectionReference collectionRef = db.collection("CallingForRescue");

        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        // Collection không rỗng, có documents
                        // Thực hiện các thao tác bạn cần ở đây với querySnapshot
                        if(role.equals("rescue")) {
                            showCustomDialog();
                        }
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            // Lặp qua các document và thực hiện xử lý nếu cần
                            // Ví dụ: String documentId = document.getId();

                        }
                    } else {
                        // Collection rỗng, không có documents
                        // Xử lý logic khi collection rỗng ở đây

                    }
                } else {
                    // Lỗi khi thực hiện lấy dữ liệu từ Firestore
                }
            }
        });
    }
    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

// Inflate layout custom_dialog_layout.xml vào dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_notification_dialog_layout, null);

// Tìm các view trong layout custom để thay đổi nội dung
        ImageView dialogImage = dialogView.findViewById(R.id.dialog_image);
        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);

// Set hình ảnh và nội dung cho dialog
        dialogImage.setImageResource(R.drawable.img_rescue); // Thay đổi hình ảnh
        dialogTitle.setText("Thông báo cứu hộ");
        dialogMessage.setText("Có người đang gọi cứu hộ...");

// Tạo AlertDialog từ builder và thiết lập layout custom vào dialog
        builder.setView(dialogView);

// Tạo dialog từ builder và gán vào biến dialog
        final AlertDialog dialog = builder.create();

// Xử lý sự kiện cho nút trong dialogView
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnView = dialogView.findViewById(R.id.btnView);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi người dùng nhấn Hủy bỏ
                dialog.dismiss();
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi người dùng nhấn Xem ngay
                Intent intent = new Intent(getActivity(), RescueCallingList.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public void onMapReady(@NonNull GoogleMap googleMap) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Tạo service của Google Map và thực hiện các chức năng
                googleMapService = new GoogleMapService(googleMap, fusedLocationProviderClient, getActivity());
                googleMapService.myLocation();
                googleMapService.onMarkerClick();
                googleMapService.initializeMap();
                googleMapService.setOnAddressReceivedListener(new OnAddressReceivedListener() {
                    @Override
                    public void onAddressReceived(String address, String addressName, double latitude, double longitude) {
                        // Xử lý địa chỉ và thông tin vị trí ở đây
                        Log.d("Address", "Received Address: " + address);
                        Log.d("Address", "Received Address Name: " + addressName);
                        Log.d("Location", "Received Latitude: " + latitude);
                        Log.d("Location", "Received Longitude: " + longitude);
                    }
                });
            }
        }, 4000); // 5 giây
    }

    void viewData_cuuhonhieunhat() {

        List<DataUser> carServiceList = new ArrayList<>();

//         carServiceList.add(new DataUser("UID001", "Auto Care Center", "+1-123-456-7890", "autocare@example.com", "123 Main St, City", "Monday - Friday", "10", "Sedan", "4.8", R.drawable.img_welcom));
//        carServiceList.add(new DataUser("UID002", "Speedy Repairs", "+1-987-654-3210", "speedy@example.com", "456 Elm St, Town", "Monday - Saturday", "8", "SUV", "4.5", R.drawable.img_welcom));
//        carServiceList.add(new DataUser("UID003", "Pro Auto Shop", "+1-555-777-3333", "proauto@example.com", "789 Oak St, Village", "Monday - Sunday", "12", "Truck", "4.9", R.drawable.img_welcom));
//        carServiceList.add(new DataUser("UID004", "City Car Service", "+1-111-222-3333", "citycar@example.com", "321 Pine St, County", "Tuesday - Saturday", "6", "Convertible", "4.7", R.drawable.img_welcom));
//        carServiceList.add(new DataUser("UID005", "Green Auto Garage", "+1-888-888-8888", "greenauto@example.com", "654 Birch St, Suburb", "Monday - Saturday", "5", "Hybrid", "4.6", R.drawable.img_welcom));

        for (DataUser data : carServiceList) {
            View item = LayoutInflater.from(getActivity()).inflate(R.layout.item_rescuest, horizontalLayout, false);
            // Thay thế R.id.xxx bằng ID thực sự của các phần tử trong item_rescuest.xml
            ImageView itemImageView = item.findViewById(R.id.imageView);
            TextView txt_name = item.findViewById(R.id.edt_name);
            TextView txt_sdt = item.findViewById(R.id.edt_sdt);
            TextView txt_address = item.findViewById(R.id.edt_address);
            TextView txt_turns = item.findViewById(R.id.edt_turns);

            itemImageView.setImageResource(R.drawable.ic_app);
            txt_name.setText(data.getName());
            txt_sdt.setText(data.getCall());
            txt_address.setText(data.getAddress());
            txt_turns.setText(data.getDecription());
            horizontalLayout.addView(item);
        }
    }
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String name, phone, typecar, numbercar, img, role;
    void getMyUser() {
        db.collection("Users").document(user.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            name = doc.getString("name");
                            txt_name.setText(name);
                            phone = doc.getString("phone");
                            typecar = doc.getString("typecar");
                            numbercar = doc.getString("numbercar");
                            img = doc.getString("img");
                            role = doc.getString("role");

                            Log.d("TEST",role);

                            Glide.with(HomeFragment.this)
                                    .load(img)
                                    .circleCrop() // Áp dụng cắt ảnh thành hình tròn
                                    .into(img_avt);

                            checkListCallingRescue();
                        }
                    }
                });
    }

    void setEvent() {

        img_ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CallForRescue.class);
                intent.putExtra("NAME", name);
                intent.putExtra("PHONE", phone);
                intent.putExtra("TYPECAR", typecar);
                intent.putExtra("NUMBERCAR", numbercar);
                startActivity(intent);
            }
        });
        img_chatbotgpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FCMUtil fcmUtil = new FCMUtil();
//                fcmUtil.subscribeToTopic(getActivity(),"rescue");

                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                    return;
                                }

                                // Get new FCM registration token
                                String token = task.getResult();

                                // Log and toast

                                Log.d(TAG, token);
                            }
                        });
                Toast.makeText(getActivity(), "Vui lòng chờ", Toast.LENGTH_SHORT).show();
                // Thông tin cần thiết

                String fcmServerKey = "AAAA-aEDMr4:APA91bFkulQb-yKqZHCdfMMvTnAWHu6eHSaFsPTkTiM4CN4nux4zGjFOpEnk_NXESGI3i98JmZX0AJj7tqyFsxmhhOU5AP4v0fHmxVNNA6olETuUvwhpCg6ip_0NT3kXa-eWUFeC0rP_";
                String receiverToken = "eImI4cXhSR-bWnQP84GKNe:APA91bHvlmJxjpwJggnIDvEAIlB3KA8bT6OBUDDjdoUFxEWzl-CS3vAQZWRhC5XE7Ca7WTUOGz6g8ltGfB0foaSIXRQOc4_FKkOGmVWbfMBUrZP0b3xwGmU9Sy6bJa6FhEUxiqhVL20R";
                String notificationTitle = "Xin chào";
                String notificationBody = "This is a test notification";

                // Gửi thông báo
                Post_Calling(fcmServerKey, receiverToken,notificationBody, notificationTitle);
            }
        });

        img_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Test_Process.class);
                startActivity(intent);
            }
        });
    }

}