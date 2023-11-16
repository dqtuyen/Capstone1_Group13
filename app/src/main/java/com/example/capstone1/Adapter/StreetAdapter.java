package com.example.capstone1.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone1.Fragment.MapsFragment;
import com.example.capstone1.R;
import com.example.capstone1.StreetModel;

import java.util.List;

public class StreetAdapter extends RecyclerView.Adapter<StreetAdapter.ViewHolder> {

    private List<StreetModel> streets;
    private String number;

    private String fullAddress = "";
    public StreetAdapter(String number, List<StreetModel> streets) {
        this.number = number;
        this.streets = streets;
    }
    private MapsFragment fragment;
    public void setMapsFragment(MapsFragment fragment) {
        this.fragment = fragment;
    }
    public void processFragmentData(String data) {
        fragment.processDataFromAdapter(data);
    }
    public StreetAdapter() {
        this.fullAddress = fullAddress;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StreetModel street = streets.get(position);
        if(position == 0) {
            holder.streetNameTextView.setText(number);
        } else {
            holder.streetNameTextView.setText(number.replaceAll("[^0-9/]", "") + " " + street.getName());
        }

        holder.nameAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullAddress = holder.streetNameTextView.getText().toString();
                processFragmentData(fullAddress);
                //Log.d("Fulladdress", "Fulladdress: " + fullAddress);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(streets.size(), 5);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView streetNameTextView;
        public LinearLayout nameAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            streetNameTextView = itemView.findViewById(R.id.streetNameTextView);
            nameAddress = itemView.findViewById(R.id.nameAddress);


        }
    }

}

