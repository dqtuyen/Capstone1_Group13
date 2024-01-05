package com.example.capstone1.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.capstone1.Activity.MainActivity;
import com.example.capstone1.Data.DataCallingForRescue;
import com.example.capstone1.Data.DataUser;
import com.example.capstone1.OnItemClickListener;
import com.example.capstone1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    ArrayList<DataCallingForRescue> dataCallingForRescues;
    Context context;
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public CustomerAdapter(Context context, ArrayList<DataCallingForRescue> dataCallingForRescues) {
        this.context = context;
        this.dataCallingForRescues = dataCallingForRescues;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // gán view
        View view = LayoutInflater.from(context).inflate(R.layout.item_customer, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.ViewHolder holder, int position) {
        String name;

        DataCallingForRescue dataCallingForRescue = dataCallingForRescues.get(position);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                String address = dataCallingForRescue.getAddress();
                listener.onItemClick(position, address);
            }
        });

        holder.txt_name_user.setText(dataCallingForRescue.getName());
        holder.txt_time_user.setText(dataCallingForRescue.getTime());
        holder.txt_phone_user.setText((dataCallingForRescue.getPhone()));
        holder.txt_evaluated.setText(dataCallingForRescue.getDescription());
        holder.txt_address.setText(dataCallingForRescue.getAddress());

        Glide.with(context)
                .load(dataCallingForRescue.getImg())
                .circleCrop() // Áp dụng cắt ảnh thành hình tròn
                .into(holder.img_user);
        //holder.img_user.setImageResource(R.drawable.ic_app);
        holder.img_address.setImageResource(R.drawable.address_marker);


    }

    void getInfoRescue(String iudRescue) {

    }
    @Override
    public int getItemCount() {
        return dataCallingForRescues.size(); // trả item tại vị trí postion
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name_user, txt_phone_user, txt_time_user, txt_evaluated, txt_address;
        ImageView img_user, img_star, img_address;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name_user = itemView.findViewById(R.id.txt_name_user);
            txt_phone_user = itemView.findViewById(R.id.txt_phone_user);
            txt_time_user = itemView.findViewById(R.id.txt_time_user);
            txt_evaluated = itemView.findViewById(R.id.txt_evaluated);
            txt_address = itemView.findViewById(R.id.txt_address);
            img_user = itemView.findViewById(R.id.img_user);
            img_star = itemView.findViewById(R.id.img_star);
            img_address = itemView.findViewById(R.id.img_address);
        }
    }
    public void clearRecyclerView() {
        dataCallingForRescues.clear();
        notifyDataSetChanged();
    }
}
