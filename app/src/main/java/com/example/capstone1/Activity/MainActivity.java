package com.example.capstone1.Activity;

import static android.content.ContentValues.TAG;
import static com.example.capstone1.FcmNotificationSender.Post_Calling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone1.Adapter.MyPagerAdapter;
import com.example.capstone1.Adapter.ViewPagerAdapter;
import com.example.capstone1.Fragment.AccountFragment;
import com.example.capstone1.Fragment.ChatBotFragment;
import com.example.capstone1.Fragment.HistoryFragment;
import com.example.capstone1.Fragment.HomeFragment;
import com.example.capstone1.Fragment.MapsFragment;
import com.example.capstone1.GenarateCharacter;
import com.example.capstone1.GoogleMapService;
import com.example.capstone1.OnAddressReceivedListener;
import com.example.capstone1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import APIChatGPT.MessageDataAdapterTest;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getStringExtra("title");
            String body = intent.getStringExtra("body");

            // Xử lý dữ liệu nhận được từ service ở đây
            // Ví dụ: Hiển thị thông báo trong MainActivity
            Toast.makeText(MainActivity.this, title + " -> " + body, Toast.LENGTH_SHORT).show();
            Log.e("LOG",title + " -> " + body );
        }
    };


    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d("LOG", "Đã bấm vào thông báo");

        if (intent.getBooleanExtra("notification_clicked", false)) {
            String title = intent.getStringExtra("title");
            String key_user = intent.getStringExtra("key_user");
            Log.d("LOG", "Notification Clicked - Title: " + title + ", Key_User: " + key_user);


            String key = genarateCharacter.getKey(key_user);
            String uid_custommer = genarateCharacter.getUID(key_user);
            Log.d("LOG", "Notification Clicked - Title: " + title + ", Key: " + key);
            updateSixthElement(key);
            getInfoCustommer(key, uid_custommer);
        }
    }
    void getInfoCustommer(String key_user, String uid_custommer) {
        db.collection("RescueInformation").document(uid_custommer)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Kiểm tra xem trường arrayFieldName có tồn tại không
                            if (documentSnapshot.contains(key_user)) {
                                List<Object> dataArray = (List<Object>) documentSnapshot.get(key_user);

                                String dateTime = dataArray.get(0).toString();
                                String distance = dataArray.get(1).toString();
                                String evaluateid = dataArray.get(2).toString();
                                String latitude = dataArray.get(3).toString();
                                String longitude = dataArray.get(4).toString();
                                String uid_cus = uid_custommer;
                                String uid_res = dataArray.get(6).toString();

                                Log.d("LOG", "Info: " +  dateTime + distance +
                                        evaluateid + latitude + longitude + uid_cus + uid_res);
                            } else {
                                // Trường arrayFieldName không tồn tại trong tài liệu
                                System.out.println("Trường arrayFieldName không tồn tại.");
                            }
                        } else {
                            // Tài liệu không tồn tại
                            System.out.println("Tài liệu không tồn tại.");
                        }
                    }
                });
    }

    void updateSixthElement(String key_user) {
        db.collection("RescueInformation").document("nWNVygakezUI0b7A0yknmrDX9Sg1")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            List<String> dataArray = (List<String>) documentSnapshot.get(key_user);

                            if (dataArray != null && dataArray.size() >= 7) {
                                dataArray.set(6, user.getUid()); // Cập nhật giá trị tại vị trí thứ 6

                                // Cập nhật lại toàn bộ mảng vào Firestore
                                db.collection("RescueInformation").document("nWNVygakezUI0b7A0yknmrDX9Sg1")
                                        .update(key_user, dataArray)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("LOG", "Thành công");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                e.printStackTrace();
                                                Log.d("LOG", "Thất bại");
                                            }
                                        });
                            } else {
                                Log.d("LOG", "Mảng không đủ phần tử");
                            }
                        } else {
                            Log.d("LOG", "Tài liệu không tồn tại");
                        }
                    }
                });
    }
    //BottomNavigationView
    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private LinearLayout wait_rescue_info;
    private LinearLayout rescue_info;
    List<Fragment> fragmentList;
    GenarateCharacter genarateCharacter;

    ImageView img_star, img_rescue;
    TextView txt_name_rescue, txt_info_moto;

    //Quan trọng
    String Key_name_list = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        img_star = findViewById(R.id.img_star);
        img_rescue = findViewById(R.id.img_rescue);
        txt_name_rescue = findViewById(R.id.txt_name_rescue);
        txt_info_moto = findViewById(R.id.txt_info_moto);

        mViewPager = findViewById(R.id.view_pager);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        wait_rescue_info = findViewById(R.id.wait_rescue_info);
        rescue_info = findViewById(R.id.rescue_info);
        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new HistoryFragment());
        fragmentList.add(new ChatBotFragment());
        fragmentList.add(new AccountFragment());
        genarateCharacter = new GenarateCharacter();
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(pagerAdapter);
// Xác định số lượng trang được giữ lại (ví dụ: 2)
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(0);
        checkedInformationReal(user.getUid());
        checkClickComfirm();

        setEvent();
        MessageDataAdapterTest messageDataAdapterTest = new MessageDataAdapterTest();
        messageDataAdapterTest.sendMessageToOpenAI("Bác hồ tên thật là gì?");

        IntentFilter intentFilter = new IntentFilter("com.example.capstone1.MyFirebaseMessagingService");
        registerReceiver(messageReceiver, intentFilter);
    }

    void sendNotification() {
        String fcmServerKey = "AAAA-aEDMr4:APA91bFkulQb-yKqZHCdfMMvTnAWHu6eHSaFsPTkTiM4CN4nux4zGjFOpEnk_NXESGI3i98JmZX0AJj7tqyFsxmhhOU5AP4v0fHmxVNNA6olETuUvwhpCg6ip_0NT3kXa-eWUFeC0rP_";
        String receiverToken = "eImI4cXhSR-bWnQP84GKNe:APA91bHvlmJxjpwJggnIDvEAIlB3KA8bT6OBUDDjdoUFxEWzl-CS3vAQZWRhC5XE7Ca7WTUOGz6g8ltGfB0foaSIXRQOc4_FKkOGmVWbfMBUrZP0b3xwGmU9Sy6bJa6FhEUxiqhVL20R";
        String notificationTitle = Key_name_list + user.getUid();
        String notificationBody = "Bạn ơi có người cần bạn giúp đỡ";

        // Gửi thông báo
        Post_Calling(fcmServerKey, receiverToken,notificationBody, notificationTitle);
    }

    void checkClickComfirm() {
        String valueFromActivity2 = null;

        Intent intent = getIntent();
        if (intent != null) {
            valueFromActivity2 = intent.getStringExtra("KEY");
            Key_name_list = intent.getStringExtra("KEY_name_list");
            if(valueFromActivity2 != null && valueFromActivity2.equals("1")) {
                wait_rescue_info.setVisibility(View.VISIBLE);
                updateData5s();
                Log.d("LOG", "Key is 1");
                Log.d("LOG", Key_name_list);
            } else {
                wait_rescue_info.setVisibility(View.GONE);
                Log.d("LOG", "Key is not 1");
            }
            Log.d("LOG", "Key: " + valueFromActivity2);
        } else {
            Log.d("LOG", "Intent is null");
        }
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    void getRescueInfo(){
        Map<Object, String> data = new HashMap<>();
        data.put("","");

    }
//    void updateData5s() {
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                DocumentReference docRef = db.collection("RescueInformation").document("nWNVygakezUI0b7A0yknmrDX9Sg1");
//
//                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot doc = task.getResult();
//                            String rescueuid = doc.getString("rescueuid");
//
//                            if (rescueuid != null && !rescueuid.isEmpty()) {
//                                Toast.makeText(MainActivity.this, "Đã có xe đại vương ơi", Toast.LENGTH_SHORT).show();
//                                wait_rescue_info.setVisibility(View.GONE);
//                                rescue_info.setVisibility(View.VISIBLE);
//                                handler.removeCallbacksAndMessages(null); // Dừng việc lặp lại
//                            } else {
//                                Toast.makeText(MainActivity.this, "Đang tìm kiếm xe", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            // Xử lý lỗi
//                        }
//                    }
//                });
//
//                // Lặp lại sau 5 giây nếu điều kiện vẫn chưa được thỏa mãn
//                if (!handler.hasMessages(0)) {
//                    handler.postDelayed(this, 5000); // 5000 milliseconds = 5 seconds
//                }
//            }
//        }, 5000); // Delay ban đầu, 5000 milliseconds = 5 seconds
//    }

        void updateData5s() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("RescueInformation").document("nWNVygakezUI0b7A0yknmrDX9Sg1"); // Thay thế "document_id" bằng ID thực của tài liệu

                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            List<Object> myArray = (List<Object>) documentSnapshot.get(Key_name_list);
                            if (myArray != null && myArray.size() >= 7) {

                                String itemAtIndex7 = myArray.get(6).toString(); // Lấy phần tử ở vị trí thứ 7 (chỉ số bắt đầu từ 0)
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(itemAtIndex7 != null && !itemAtIndex7.isEmpty()) {
                                            Toast.makeText(MainActivity.this, "Đã có xe đại vương ơi", Toast.LENGTH_SHORT).show();
                                            getInfoRescue(itemAtIndex7);
                                            wait_rescue_info.setVisibility(View.GONE);
                                            rescue_info.setVisibility(View.VISIBLE);

                                            handler.removeCallbacksAndMessages(null); // Dừng việc lặp lại
                                            Log.d("TAG", "Item at index 7: " + itemAtIndex7.toString());
                                        } else {
                                            Toast.makeText(MainActivity.this, "Đang tìm kiếm xe", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, 3000);


                            } else {
                                Log.d("TAG", "myArray is null or empty");
                            }
                        } else {
                            Log.d("TAG", "Document does not exist");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "Error getting document", e);
                    }
                });
                Log.e("TAG", "đang chạy");
                // Lặp lại sau 5 giây nếu điều kiện vẫn chưa được thỏa mãn
                if (!handler.hasMessages(0)) {
                    handler.postDelayed(this, 5000); // 5000 milliseconds = 5 seconds
                }
            }
        }, 5000); // Delay ban đầu, 5000 milliseconds = 5 seconds
    }
    void getInfoRescue(String iudRescue) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Users").document(iudRescue);

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            String name = doc.getString("name");
                            String numbercar = doc.getString("numbercar");
                            String typecar = doc.getString("typecar");
                            String avgstar = doc.getString("avgstar");
                            String img = doc.getString("img");

                            img_star.setImageResource(R.drawable.one_star_orange);
                            img_rescue.setImageResource(R.drawable.back);
                            txt_name_rescue.setText(name);
                            txt_info_moto.setText(numbercar + " • " + typecar);
                        } else {
                            // Xử lý lỗi
                        }
                    }
                });

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
    protected void onDestroy() {
        super.onDestroy();
    }
    void setEvent() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_tab_1).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_tab_2).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_tab_3).setChecked(true);
                        break;
                    case 3:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_tab_4).setChecked(true);
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
                    return true;
                } else if (item.getItemId() == R.id.menu_tab_2) {
                    mViewPager.setCurrentItem(1);
                    return true;
                } else if (item.getItemId() == R.id.menu_tab_3) {
                    mViewPager.setCurrentItem(2);
                    return true;
                } else if (item.getItemId() == R.id.menu_tab_4) {
                    mViewPager.setCurrentItem(3);
                    return true;
                }
                return false;
            }
        });
        wait_rescue_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomNavigationView.getMenu().findItem(R.id.menu_tab_2).setChecked(true);
                mViewPager.setCurrentItem(1);

            }
        });
    }
}