package com.example.ahand.service;

import android.graphics.Bitmap;

public class Service {
    Bitmap srvImage;
    String srvName;
    String srvRegion;
    String description;
    float srvHrRate;

    public Service(Bitmap srvImage, String srvName, String srvRegion, String description, float srvHrRate) {
        this.srvImage = srvImage;
        this.srvName = srvName;
        this.srvRegion = srvRegion;
        this.description = description;
        this.srvHrRate = srvHrRate;
    }

//    public Service(Bitmap srvImage, String srvName, String srvRegion, float srvHrRate) {
//        this.srvImage = srvImage;
//        this.srvName = srvName;
//        this.srvRegion = srvRegion;
//        this.srvHrRate = srvHrRate;
//    }

    public Bitmap getSrvImage() {
        return srvImage;
    }

    public void setSrvImage(Bitmap srvImage) {
        this.srvImage = srvImage;
    }

    public String getSrvName() {
        return srvName;
    }

    public void setSrvName(String srvName) {
        this.srvName = srvName;
    }

    public String getSrvRegion() {
        return srvRegion;
    }

    public void setSrvRegion(String srvRegion) {
        this.srvRegion = srvRegion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getSrvHrRate() {
        return srvHrRate;
    }

    public void setSrvHrRate(float srvHrRate) {
        this.srvHrRate = srvHrRate;
    }
}