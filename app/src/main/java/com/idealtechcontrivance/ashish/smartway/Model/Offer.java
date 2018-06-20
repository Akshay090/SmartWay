package com.idealtechcontrivance.ashish.smartway.Model;

public class Offer {
    private String shopName, shopImage, offerName, offerDiscount, gotDiscount;

    public Offer() {
    }

    public Offer(String shopName, String offerDiscount, String shopImage, String offerName, String gotDiscount) {
        this.shopName = shopName;
        this.offerDiscount = offerDiscount;
        this.shopImage = shopImage;
        this.offerName = offerName;
        this.gotDiscount = gotDiscount;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOfferDiscount() {
        return offerDiscount;
    }

    public void setOfferDiscount(String offerDiscount) {
        this.offerDiscount = offerDiscount;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getGotDiscount() {
        return gotDiscount;
    }

    public void setGotDiscount(String gotDiscount) {
        this.gotDiscount = gotDiscount;
    }
}
