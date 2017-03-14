package com.group.neusoft.moviesurfer.coco;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ttc on 2017/3/13.
 */

public class User extends AppCompatActivity {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    private String name;
    private Bitmap picture;
    private String headerUrl;
}
