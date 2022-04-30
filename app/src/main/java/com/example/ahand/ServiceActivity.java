package com.example.ahand;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ahand.service.Service;
import com.example.ahand.service.ServiceAdapter;
import com.example.ahand.service.ServiceDB;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ServiceActivity extends AppCompatActivity {
    ImageView serviceImg;
    public static final int REQ_COD = 1001;
    Uri imgPath;
    Bitmap imgBitmap;
    TextInputEditText nameSrv, regionSrv, rateSrv, descr;
    Button btnCancel, btnSend;
    ServiceDB serviceDB;
    AlertDialog.Builder builder;
    Dialog dialog;
    FirebaseUser user;
    FirebaseAuth auth;
    List<Service> serviceList;
    ServiceAdapter serviceAdapter;
    RecyclerView serviceRV;
    Info info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        serviceDB = new ServiceDB(this);
        serviceRV = findViewById(R.id.serviceRV);
        serviceList = serviceDB.services();
        serviceAdapter = new ServiceAdapter(serviceList, this);
        serviceRV.setLayoutManager(new GridLayoutManager(this,2));
        serviceRV.setAdapter(serviceAdapter);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        info = new Info(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        MenuItem addService = menu.findItem(R.id.addService);
        try {
            addService.setVisible(user != null);
        } catch (Exception e) {
            info.message("", e.getMessage());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnuHome) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (item.getItemId() == R.id.addService) {
            newService();
        }
        return super.onOptionsItemSelected(item);
    }

    private void newService() {
        serviceDB = new ServiceDB(this);
        builder = new AlertDialog.Builder(this);
        dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.service_form, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.setContentView(view);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(00, 00, 23, 66)));
        serviceImg = dialog.findViewById(R.id.serviceImg);
        nameSrv = dialog.findViewById(R.id.nameSrv);
        regionSrv = dialog.findViewById(R.id.regionSrv);
        descr = dialog.findViewById(R.id.descr);
        rateSrv = dialog.findViewById(R.id.rateSrv);
        rateSrv.setText(""+ 0);
        btnCancel = dialog.findViewById(R.id.btnCancel);
        btnSend = dialog.findViewById(R.id.btnSend);
        info = new Info(this);

        serviceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void save() {
        String name = nameSrv.getText().toString().trim();
        String region = regionSrv.getText().toString().trim();
        String description = descr.getText().toString().trim();
        Float rate = Float.valueOf(rateSrv.getText().toString().trim());

        if (imgPath == null) {
            Toast.makeText(this, "Please add an image", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            nameSrv.setError("Please add a service name");
            nameSrv.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(region)) {
            regionSrv.setError("Please add a region");
            regionSrv.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(description)) {
            regionSrv.setError("Please add a Description");
            regionSrv.requestFocus();
            return;
        }
        if (rate == 0) {
            rateSrv.setError("Rate cannot be 0");
            rateSrv.requestFocus();
            return;
        }

        try {
            serviceDB.addService(new Service(imgBitmap, name, region, description, rate));
            startActivity(new Intent(ServiceActivity.this, ServiceActivity.class));

        } catch (Exception e) {
            info.message("", e.getMessage());
        }
    }

    private void choosePhoto() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQ_COD);
        } catch (Exception e) {
            info.message("", e.getMessage());
        }
    }

    public void hideDialog(View view){
        dialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_COD && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                imgPath = data.getData();
                imgBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgPath);
                serviceImg.setImageBitmap(imgBitmap);
            } catch (Exception e) {
                info.message("", e.getMessage());
            }
        }
    }
}