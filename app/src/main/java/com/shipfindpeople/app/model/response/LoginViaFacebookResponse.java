package com.shipfindpeople.app.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonnd on 10/5/2016.
 */

public class LoginViaFacebookResponse extends CommonResponse {
    @SerializedName("id") private String Id;
    @SerializedName("email") private String Email;
    @SerializedName("gender") private String Gender;
    @SerializedName("name") private String Name;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
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
}
