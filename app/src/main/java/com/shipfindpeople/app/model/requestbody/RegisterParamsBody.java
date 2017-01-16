package com.shipfindpeople.app.model.requestbody;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonnd on 10/20/2016.
 */

public class RegisterParamsBody {
    @SerializedName("username") String UserName;
    @SerializedName("password") String Password;
    @SerializedName("name") String Name;
    @SerializedName("email") String Email;
    @SerializedName("phone") String Phone;
    @SerializedName("source") String Source;

    public RegisterParamsBody() {
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }
}
