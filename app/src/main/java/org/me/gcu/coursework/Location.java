package org.me.gcu.coursework;

import android.net.Uri;
import android.widget.RatingBar;

public class Location {
    private String name;
    private String address;
    private float rating;
    private int imageResId;
//    private Uri imageUri;// Resource ID for image

    public Location(String name, String address, float rating, int imageResId) {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.imageResId = imageResId;
    }

//    public Location(String name, String address, float rating, Uri imageResId) {
//        this.name = name;
//        this.address = address;
//        this.rating = rating;
//        this.imageUri = imageResId;
//    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public float getRating() {
        return rating;
    }

    public int getImageResId() {
        return imageResId;
    }
}