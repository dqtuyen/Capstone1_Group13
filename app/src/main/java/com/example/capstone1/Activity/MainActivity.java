package com.example.capstone1.Activity;

import static android.content.ContentValues.TAG;
import static com.example.capstone1.FcmNotificationSender.Post_Calling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.capstone1.Adapter.MyPagerAdapter;
import com.example.capstone1.Adapter.ViewPagerAdapter;
import com.example.capstone1.Data.DataCallingForRescue;
import com.example.capstone1.Fragment.AccountFragment;
import com.example.capstone1.Fragment.ChatBotFragment;
import com.example.capstone1.Fragment.HistoryFragment;
import com.example.capstone1.Fragment.HomeFragment;
import com.example.capstone1.Fragment.MapsFragment;
import com.example.capstone1.GenarateCharacter;
import com.example.capstone1.GlobalData;
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
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import APIChatGPT.MessageDataAdapterTest;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private LinearLayout wait_rescue_info;
    private LinearLayout rescue_info;
    List<Fragment> fragmentList;
    GenarateCharacter genarateCharacter;
    ImageButton img_btn_star;
    ImageView img_call, img_mess;
    ImageView img_rescue;
    TextView txt_name_rescue, txt_info_moto, txt_star;

    //Quan trọng
    String Key_name_list = "";
    String myRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        txt_star = findViewById(R.id.txt_star);
        img_btn_star = findViewById(R.id.img_btn_star);
        img_rescue = findViewById(R.id.img_rescue);
        txt_name_rescue = findViewById(R.id.txt_name_rescue);
        txt_info_moto = findViewById(R.id.txt_info_moto);
        img_call = findViewById(R.id.img_call);
        img_mess = findViewById(R.id.img_mess);
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


        getMyUser();

    }
    String CUSlongitude, CUSlatitude, CUSaddress, uidCustomer, idField, CUSname,infoCar, CUSphone, CUSdescription, CUSimg;
    ArrayList<DataCallingForRescue> dataCallingForRescues = new ArrayList<>();
    void getDataRescueInformation(String field) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(user.getUid());

        // Thực hiện truy vấn để lấy thông tin từ collection và document cụ thể
        db.collection("RescueInformation").document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Document tồn tại, lấy trường mảng và gán vào Map
                                List<Object> arrayField = (List<Object>) document.get(field);
                                if (arrayField != null) {
                                    Map<Integer, String> info = new HashMap<>();
                                    for (int i = 0; i < arrayField.size(); i++) {
                                        info.put(i , arrayField.get(i).toString());
                                    }
                                    // Sử dụng Map chứa các giá trị từ trường mảng ở đây
                                    // Ví dụ: Log các giá trị trong Map
                                    for (Map.Entry<Integer, String> entry : info.entrySet()) {
                                        Log.d("MapData", entry.getKey() + ": " + entry.getValue().toString());
                                    }

                                    dataCallingForRescues.add(new DataCallingForRescue(
                                            info.get(0), info.get(1), info.get(2),
                                            info.get(3), info.get(4), info.get(5),
                                            info.get(6), info.get(7), info.get(8), info.get(9)
                                    ));
                                    for (DataCallingForRescue data : dataCallingForRescues) {
                                        // Lấy giá trị từ mỗi phần tử và làm điều gì đó với nó
                                        CUSlongitude = data.getLongitude();
                                        CUSlatitude = data.getLatitude();
                                        CUSphone = data.getPhone();
                                        CUSname = data.getName();
                                        CUSimg = data.getImg();
                                        CUSaddress = data.getAddress();
                                        CUSdescription = data.getDescription();
                                        Log.d("TEST", CUSdescription);
                                    }
                                }
                            } else {
                                // Document không tồn tại
                            }
                        } else {
                            // Đã xảy ra lỗi trong quá trình truy vấn
                            Exception e = task.getException();
                            if (e instanceof FirebaseFirestoreException) {
                                // Xử lý lỗi FirestoreException
                            } else {
                                // Xử lý các loại lỗi khác
                            }
                        }
                    }
                });
    }

    void getMyUser() {
        db.collection("Users").document(user.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            //String myName, myPhone, myEmail, myAvgStar, myLocation, myNumberCar, myTypeCar, myImg, myRole;
//                            myName = doc.getString("name");
//                            myPhone = doc.getString("phone");
//                            myEmail = doc.getString("email");
//                            myAvgStar = doc.getString("avgstar");
//                            myLocation = doc.getString("location");
//                            myNumberCar = doc.getString("numbercar");
//                            myTypeCar = doc.getString("typecar");
//                            myImg = doc.getString("img");
                            myRole = doc.getString("role");

                            GlobalData globalData = GlobalData.getInstance();
                            globalData.setRole(myRole);
                        }
                    }
                });
    }
    void checkClickComfirm() {
        String valueFromActivity2 = null;

        Intent intent = getIntent();
        if (intent != null) {
            valueFromActivity2 = intent.getStringExtra("KEY");
            Key_name_list = intent.getStringExtra("KEY_name_list") + Status.ON;
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

        void updateData5s() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("RescueInformation").document(user.getUid()); // Thay thế "document_id" bằng ID thực của tài liệu

                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            List<Object> myArray = (List<Object>) documentSnapshot.get(Key_name_list);
                            if (myArray != null && myArray.size() >= 7) {

                                String uid_rescue = myArray.get(8).toString(); // Lấy phần tử ở vị trí thứ 7 (chỉ số bắt đầu từ 0)
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(uid_rescue != null && !uid_rescue.isEmpty()) {

                                            getDataRescueInformation(Key_name_list);
                                            Log.d("TEST", Key_name_list);
                                            Toast.makeText(MainActivity.this, "Đã có xe đại vương ơi", Toast.LENGTH_SHORT).show();
                                            getInfoRescue(uid_rescue);
                                            wait_rescue_info.setVisibility(View.GONE);
                                            rescue_info.setVisibility(View.VISIBLE);



                                            handler.removeCallbacksAndMessages(null); // Dừng việc lặp lại
                                            Log.d("TAG", "uid_rescue: " + uid_rescue.toString());
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

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

// Inflate layout custom_dialog_layout.xml vào dialog
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_nofitication_dialog_reveced, null);
        TextView txt_star;
        txt_star = findViewById(R.id.txt_star);
        ImageView dialogImage = dialogView.findViewById(R.id.dialog_image);
        TextView dialog_name = dialogView.findViewById(R.id.dialog_name);
        TextView dialog_information = dialogView.findViewById(R.id.dialog_information);


        txt_star.setText(RESavgstar);
// Set hình ảnh và nội dung cho dialog
        Glide.with(this)
                .load(RESimg)
                .circleCrop() // Áp dụng cắt ảnh thành hình tròn
                .into(dialogImage);

        dialogImage.setImageResource(R.drawable.img_rescue); // Thay đổi hình ảnh
        dialog_name.setText(RESname);
        dialog_information.setText(RESnumbercar + " • " + REStypecar);
// Tạo AlertDialog từ builder và thiết lập layout custom vào dialog
        builder.setView(dialogView);

// Tạo dialog từ builder và gán vào biến dialog
        final AlertDialog dialog = builder.create();

        Button btn_ok = dialogView.findViewById(R.id.btn_ok);
        Button btn_information = dialogView.findViewById(R.id.btn_information);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Processing.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    String RESname, RESphoneNumber, RESnumbercar, REStypecar, RESavgstar, RESimg;
    void getInfoRescue(String iudRescue) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Users").document(iudRescue);

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            RESname = doc.getString("name");
                            RESphoneNumber = doc.getString("phone");
                            RESnumbercar = doc.getString("numbercar");
                            REStypecar = doc.getString("typecar");
                            RESavgstar = doc.getString("avgstar");
                            RESimg = doc.getString("img");

                            Glide.with(MainActivity.this)
                                    .load(RESimg)
                                    .circleCrop() // Áp dụng cắt ảnh thành hình tròn
                                    .into(img_rescue);

                            txt_star.setText(RESavgstar);


                            txt_name_rescue.setText(Html.fromHtml("<b>" + RESname + "</b> đang đến"));
                            txt_info_moto.setText(RESnumbercar + " • " + REStypecar);

                            showCustomDialog();

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
        rescue_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewInformation.class);
                intent.putExtra("RESNAME", RESname);
                intent.putExtra("RESPHONE", RESphoneNumber);
                intent.putExtra("RESNUMBERCAR", RESnumbercar);
                intent.putExtra("RESTYPECAR", REStypecar);
                intent.putExtra("RESIMG", RESimg);
                intent.putExtra("RESAVGSTAR", RESavgstar);

                intent.putExtra("CUSimg", CUSimg);
                intent.putExtra("CUSlatitude", CUSlatitude);
                intent.putExtra("CUSlongitude", CUSlongitude);
                intent.putExtra("CUSdescription", CUSdescription);
                intent.putExtra("CUSaddress", CUSaddress);
                intent.putExtra("CUSname", CUSname);
                startActivity(intent);
            }
        });
        img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Tạo một Intent với ACTION_DIAL và dữ liệu số điện thoại
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + RESphoneNumber));

                // Kiểm tra xem thiết bị có ứng dụng điện thoại không trước khi chuyển hướng
                PackageManager packageManager = getPackageManager();
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent); // Chuyển hướng đến ứng dụng điện thoại
                } else {
                    // Xử lý khi không có ứng dụng điện thoại được tìm thấy
                    Toast.makeText(getApplicationContext(), "Không tìm thấy ứng dụng điện thoại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}