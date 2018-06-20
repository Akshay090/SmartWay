package com.idealtechcontrivance.ashish.smartway.Model;

public class Banner {
    private String id, image;

    public Banner() {
    }

    public Banner(String id, String image) {
        this.id = id;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
