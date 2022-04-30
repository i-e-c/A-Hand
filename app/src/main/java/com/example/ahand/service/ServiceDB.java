package com.example.ahand.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.ahand.Info;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ServiceDB extends SQLiteOpenHelper {
    byte[] byteImg;
    ByteArrayOutputStream outputStream;
    Context context;
    Info info;

    public ServiceDB(@Nullable Context context) {
        super(context, "service.db", null, 1);
        this.context = context;
        info = new Info(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table serviceTable(srvId Integer primary key autoincrement, srvImg BLOB, srvName TEXT, srvRegion TEXT, desription TEXT, srvHrRate FLOAT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists serviceTable");
    }

    public void addService(Service service) {
        try {
            SQLiteDatabase srvDB = getWritableDatabase();
            Bitmap img = service.getSrvImage();
            outputStream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byteImg = outputStream.toByteArray();

            ContentValues values = new ContentValues();
            values.put("srvImg", byteImg);
            values.put("srvName", service.getSrvName());
            values.put("srvRegion", service.getSrvRegion());
            values.put("desription", service.getDescription());
            values.put("srvHrRate", service.getSrvHrRate());
            long result = srvDB.insert("serviceTable", null, values);

            if (result != -1) {
                Toast.makeText(context, "Service Added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to be added", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            info = new Info(context);
            info.message("Error occurred", e.getMessage());
        }
    }

    public List<Service> services() {
        try {
            SQLiteDatabase srvDB = getReadableDatabase();
            Cursor cursor = srvDB.rawQuery("select * from serviceTable", null);

            List<Service> serviceList = new ArrayList<>();
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    byte[] byteImg = cursor.getBlob(1);
                    Bitmap imgBtmp = BitmapFactory.decodeByteArray(byteImg, 0, byteImg.length);
                    String name = cursor.getString(2);
                    String region = cursor.getString(3);
                    String description = cursor.getString(4);
                    float rate = cursor.getFloat(5);

                    serviceList.add(new Service(imgBtmp, name, region, description, rate));
                }
                return serviceList;
            }
            info.message("", "There is no services on the list. Please login to add some");
            return null;
        } catch (Exception e) {
            info.message("Error occurred", e.getMessage());
        }
        return null;
    }
}
