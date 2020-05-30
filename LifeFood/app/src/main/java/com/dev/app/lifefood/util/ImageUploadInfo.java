package com.dev.app.lifefood.util;

public class ImageUploadInfo {

    public String imageName;

    public String imageURL;

    public String storeCategory;

    public String user_price;

    public String user_discount;

    public String default_quantity;

    public int selected_count;

    public int getSelected_count() {
        return selected_count;
    }

    public void setSelected_count(int selected_count) {
        this.selected_count = selected_count;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setStoreCategory(String storeCategory) {
        this.storeCategory = storeCategory;
    }

    public void setUser_price(String user_price) {
        this.user_price = user_price;
    }

    public void setUser_discount(String user_discount) {
        this.user_discount = user_discount;
    }

    public void setDefault_quantity(String default_quantity) {
        this.default_quantity = default_quantity;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getStoreCategory() {
        return storeCategory;
    }

    public String getUser_price() {
        return user_price;
    }

    public String getUser_discount() {
        return user_discount;
    }

    public String getDefault_quantity() {
        return default_quantity;
    }
}
