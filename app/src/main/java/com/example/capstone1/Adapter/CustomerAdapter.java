package com.example.capstone1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone1.Data.DataUser;
import com.example.capstone1.R;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    ArrayList<DataUser> dataUsers;
    Context context;
    public CustomerAdapter(Context context, ArrayList<DataUser> dataUsers) {
        this.context = context;
        this.dataUsers = dataUsers;
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
        DataUser dataUser = dataUsers.get(position);

        holder.txt_name_user.setText(dataUser.getName());
        holder.txt_time_user.setText(dataUser.getTime());
        holder.txt_phone_user.setText((dataUser.getCall()));
        holder.txt_evaluated.setText(dataUser.getDecription());
        holder.txt_address.setText(dataUser.getAddress());
        holder.img_user.setImageResource(R.drawable.ic_app);
        holder.img_address.setImageResource(R.drawable.address_marker);
    }

    @Override
    public int getItemCount() {
        return dataUsers.size(); // trả item tại vị trí postion
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
        dataUsers.clear();
        notifyDataSetChanged();
    }
}
