package com.example.capstone1.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.capstone1.Adapter.CustomerAdapter;
import com.example.capstone1.Data.DataCallingForRescue;
import com.example.capstone1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DocumentFailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocumentFailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DocumentFailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DocumentFailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DocumentFailFragment newInstance(String param1, String param2) {
        DocumentFailFragment fragment = new DocumentFailFragment();
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
    RecyclerView recyclerView;
    ArrayList<DataCallingForRescue> dataCallingForRescues = new ArrayList<>();

    CustomerAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_document_fail, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        laytruong();

        adapter = new CustomerAdapter(getContext(),dataCallingForRescues);
        return view;
    }

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //Đầu tiên lấy danh sách trường trong user.uid
    void laytruong() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("RescueInformation").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> data = documentSnapshot.getData();
                    if (data != null) {
                        ArrayList<String> danhSachTenTruong = new ArrayList<>(data.keySet());
                        for (String tenTruong : danhSachTenTruong) {
                            Log.d("TAG", "Tên trường: " + tenTruong);
                            //Sau đó ddauw trường vào phương thức lấy data của người gọi cứu hộ
                            if(tenTruong.contains("_CANCEL")){
                                getDataCustomer(tenTruong);
                            }
                        }
                    }
                } else {
                    Log.d("TAG", "Không có dữ liệu cho document này");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "Lỗi: " + e.getMessage());
            }
        });
    }
    void getDataCustomer(String field) {
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
                                    recyclerView.setAdapter(adapter);
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
}