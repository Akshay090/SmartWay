package com.idealtechcontrivance.ashish.examcracker.Model;

public class Features {
    private int id;
    private String title, listing, image;

    public Features() {
    }

    public Features(int id, String title, String listing, String image) {
        this.id = id;
        this.title = title;
        this.listing = listing;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getListing() {
        return listing;
    }

    public void setListing(String listing) {
        this.listing = listing;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
