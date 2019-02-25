package com.curahservice.netset.module.myProfile_tabs;

import android.content.Context;

public class Image_Model {

    private String id,video;
    private int image,thumb;
    private boolean isImage = false;
    private Context context;

    public Image_Model(Context context) {
        this.context = context;
    }

    Image_Model(String id, int image, int thumb, String video) {
        this.id = id;
        this.image = image;
        this.thumb = thumb;
        this.video = video;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getThumb() {
        return thumb;
    }

    public void setThumb(int thumb) {
        this.thumb = thumb;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
