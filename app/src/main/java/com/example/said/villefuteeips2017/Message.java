package com.example.said.villefuteeips2017;

/**
 * Created by idriss on 1/27/18.
 */

public class Message {
    private int id;
    private String name;
    private String phone;
    private String replyEmail;
    private String sender;
    private String reciver;
    private int storeId;
    private String message;

    public Message(int id, String name, String phone, String replyEmail, String sender, String reciver, int storeId, String message) {
        this.id=id;
        this.name = name;
        this.phone = phone;
        this.replyEmail = replyEmail;
        this.sender = sender;
        this.reciver = reciver;
        this.storeId = storeId;
        this.message = message;
    }

    public Message() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReplyEmail() {
        return replyEmail;
    }

    public void setReplyEmail(String replyEmail) {
        this.replyEmail = replyEmail;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
