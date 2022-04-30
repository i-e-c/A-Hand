package com.example.ahand.service;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahand.R;

public class VH extends RecyclerView.ViewHolder {
    ImageView serviceImg;
    TextView serveTitle, serveRegion, priceHr, desc;

    public VH(@NonNull View itemView) {
        super(itemView);
        serviceImg = itemView.findViewById(R.id.serviceImg);
        serveTitle = itemView.findViewById(R.id.serveTitle);
        serveRegion = itemView.findViewById(R.id.serveRegion);
        priceHr = itemView.findViewById(R.id.priceHr);
        desc = itemView.findViewById(R.id.desc);
    }
}
