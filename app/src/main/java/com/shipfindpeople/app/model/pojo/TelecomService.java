package com.shipfindpeople.app.model.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonnd on 9/28/2016.
 */

public class TelecomService {
    @SerializedName("Name") private String Name;

    @SerializedName("Value") private String Value;

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
