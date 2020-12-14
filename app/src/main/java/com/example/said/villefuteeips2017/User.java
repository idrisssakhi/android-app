package com.example.said.villefuteeips2017;

/**
 * Created by isakhi on 25/12/17.
 */

public class User {
    private String userName;
    private String mailAdress;
    private String password;
    private int age;
    private String Gender;
    private String fonction;

    public User(String userName, String mailAdress, String password, int age, String gender, String fonction) {
        this.userName = userName;
        this.mailAdress = mailAdress;
        this.password = password;
        this.age = age;
        Gender = gender;
        this.fonction = fonction;
    }

    public User(String mailAdress) {
        this.mailAdress = mailAdress;
    }

    public String getUserName() {
        return userName;
    }

    public String getMailAdress() {
        return mailAdress;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return Gender;
    }

    public String getFonction() {
        return fonction;
    }
}
