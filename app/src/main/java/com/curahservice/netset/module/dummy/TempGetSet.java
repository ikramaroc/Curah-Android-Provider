package com.curahservice.netset.module.dummy;

public class TempGetSet {

    private String message;
    private int image;
    private int Tag;

    TempGetSet(String message, int image, int tag) {
        this.message = message;
        this.image = image;
        Tag = tag;
    }

    public String getMessage() {
        return message;
    }

    public int getImage() {
        return image;
    }

    public int getTag() {
        return Tag;
    }
}
