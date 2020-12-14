package com.example.said.villefuteeips2017;

/**
 * Created by idriss on 1/17/18.
 */

public class Item {
    private int id;
    private int storeId;
    private String locality;
    private String articleName;
    private String articlCategory;
    private double price;
    private String articleDescription;
    private byte[] imageImageData;

    public Item(int id, int storeId, String articleName, String articlCategory, double price, String articleDescription, byte[] imageImageData, String locality) {
        this.id = id;
        this.storeId = storeId;
        this.articleName = articleName;
        this.articlCategory = articlCategory;
        this.price = price;
        this.articleDescription = articleDescription;
        this.imageImageData = imageImageData;
        this.locality= locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLocality() {
        return locality;
    }

    public Item() {
    }

    public int getId() {
        return id;
    }

    public int getStoreId() {
        return storeId;
    }

    public String getArticleName() {
        return articleName;
    }

    public String getArticlCategory() {
        return articlCategory;
    }

    public double getPrice() {
        return price;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public byte[] getImageImageData() {
        return imageImageData;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public void setArticlCategory(String articlCategory) {
        this.articlCategory = articlCategory;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    public void setImageImageData(byte[] imageImageData) {
        this.imageImageData = imageImageData;
    }
}
