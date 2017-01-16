package com.shipfindpeople.app.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonnd on 10/24/2016.
 */

public class LoginResponse extends CommonResponse {
    @SerializedName("Id")
    private int Id;
    @SerializedName("Email")
    private String Email;
    @SerializedName("Gender")
    private String Gender;
    @SerializedName("Name")
    private String Name;
    @SerializedName("UserName")
    private String UserName;
    @SerializedName("Phone")
    private String Phone;
    @SerializedName("IsExpired")
    private String IsExpired;
    @SerializedName("MessageExpired")
    private String MessageExpired;
    @SerializedName("UrlAvatar")
    private String UrlAvatar;
    @SerializedName("DateUse")
    private String DateUse;
    @SerializedName("Coin")
    private String Coin;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getIsExpired() {
        return IsExpired;
    }

    public void setIsExpired(String isExpired) {
        IsExpired = isExpired;
    }

    public String getMessageExpired() {
        return MessageExpired;
    }

    public void setMessageExpired(String messageExpired) {
        MessageExpired = messageExpired;
    }

    public String getUrlAvatar() {
        return UrlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        UrlAvatar = urlAvatar;
    }

    public String getDateUse() {
        return DateUse;
    }

    public void setDateUse(String dateUse) {
        DateUse = dateUse;
    }

    public String getCoin() {
        return Coin;
    }

    public void setCoin(String coin) {
        Coin = coin;
    }
}
