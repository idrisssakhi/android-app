package com.example.said.villefuteeips2017;

import android.media.Image;
import android.provider.ContactsContract;

/**
 * Created by idriss on 1/11/18.
 */

public class Store {
    private int id;
    private String mailAdress;
    private String storeName;
    private String StorePhone;
    private String StoreAddress;
    private int StoreCodePostal;
    private String StoreLocality;
    private Double StoreLongitude;
    private Double StoreLatitude;
    private String StoreDescription;
    private String StoreCategory;
    private String StoreOpening;
    private String StoreClosing;
    private byte[] StoreImageData;


    public Store(int id, String mailAdress, String storeName, String storePhone, String storeAddress,
                 int storeCodePostal, String storeLocality, Double storeLongitude, Double storeLatitude,
                 String storeDescription, String storeCategory, String storeOpening, String storeClosing,
                 byte[] storeImageData) {
        this.id = id;
        this.mailAdress = mailAdress;
        this.storeName = storeName;
        StorePhone = storePhone;
        StoreAddress = storeAddress;
        StoreCodePostal = storeCodePostal;
        StoreLocality = storeLocality;
        StoreLongitude = storeLongitude;
        StoreLatitude = storeLatitude;
        StoreDescription = storeDescription;
        StoreCategory = storeCategory;
        StoreOpening = storeOpening;
        StoreClosing = storeClosing;
        StoreImageData = storeImageData;
    }

    public Store() {
    }

    public Store(int id, String storeName, String storePhone, String storeLocality, byte[] img, String storeDescription) {
        this.id = id;
        this.storeName = storeName;
        StorePhone = storePhone;
        StoreLocality = storeLocality;
        this.StoreImageData=img;
        this.StoreDescription=storeDescription;
    }

    public int getId() {
        return id;
    }

    public String getMailAdress() {
        return mailAdress;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStorePhone() {
        return StorePhone;
    }

    public String getStoreAddress() {
        return StoreAddress;
    }

    public int getStoreCodePostal() {
        return StoreCodePostal;
    }

    public String getStoreLocality() {
        return StoreLocality;
    }

    public Double getStoreLongitude() {
        return StoreLongitude;
    }

    public Double getStoreLatitude() {
        return StoreLatitude;
    }

    public String getStoreDescription() {
        return StoreDescription;
    }

    public String getStoreCategory() {
        return StoreCategory;
    }

    public String getStoreOpening() {
        return StoreOpening;
    }

    public String getStoreClosing() {
        return StoreClosing;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMailAdress(String mailAdress) {
        this.mailAdress = mailAdress;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setStorePhone(String storePhone) {
        StorePhone = storePhone;
    }

    public void setStoreAddress(String storeAddress) {
        StoreAddress = storeAddress;
    }

    public void setStoreCodePostal(int storeCodePostal) {
        StoreCodePostal = storeCodePostal;
    }

    public void setStoreLocality(String storeLocality) {
        StoreLocality = storeLocality;
    }

    public void setStoreLongitude(Double storeLongitude) {
        StoreLongitude = storeLongitude;
    }

    public void setStoreLatitude(Double storeLatitude) {
        StoreLatitude = storeLatitude;
    }

    public void setStoreDescription(String storeDescription) {
        StoreDescription = storeDescription;
    }

    public byte[] getStoreImageData() {
        return StoreImageData;
    }

    public void setStoreImageData(byte[] storeImageData) {
        StoreImageData = storeImageData;
    }

    public void setStoreCategory(String storeCategory) {
        StoreCategory = storeCategory;
    }

    public void setStoreOpening(String storeOpening) {
        StoreOpening = storeOpening;
    }

    public void setStoreClosing(String storeClosing) {
        StoreClosing = storeClosing;
    }
}
