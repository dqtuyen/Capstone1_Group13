package com.example.capstone1.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone1.Data.DataTest;
import com.example.capstone1.R;

import java.util.List;

public class AdapterHorizontalScrollView extends RecyclerView.Adapter<AdapterHorizontalScrollView.ViewHolder> {
    private List<DataTest> data;

    public AdapterHorizontalScrollView(List<DataTest> data) {
        this.data = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView txt_name, txt_sdt, txt_address, txt_turns;

        public ImageView img_stars;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            txt_name = itemView.findViewById(R.id.edt_name);
            txt_sdt = itemView.findViewById(R.id.edt_sdt);
            txt_address = itemView.findViewById(R.id.edt_address);
            txt_turns = itemView.findViewById(R.id.edt_turns);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rescuest, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataTest item = data.get(position);

        holder.imageView.setImageResource(item.getImg());
        holder.txt_name.setText(item.getName());
        holder.txt_sdt.setText(item.getCall());
        holder.txt_address.setText(item.getAddress());
        holder.txt_turns.setText(item.getTurns());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}