package com.example.ahand.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahand.Info;
import com.example.ahand.R;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<VH> {
    List<Service> serviceList;
    Context context;
    Info info;

    public ServiceAdapter(List<Service> serviceList, Context context) {
        this.serviceList = serviceList;
        this.context = context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.service_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Service service = serviceList.get(position);
        info = new Info(context);
        holder.serviceImg.setImageBitmap(service.getSrvImage());
        holder.serveTitle.setText(service.getSrvName());
        holder.serveRegion.setText(service.getSrvRegion());
        holder.desc.setText(service.getDescription());
        holder.priceHr.setText("" + (int)service.getSrvHrRate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.message("" + service.getSrvName(), "" + service.getDescription());
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }
}
