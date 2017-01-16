package com.shipfindpeople.app.model.requestbody;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonnd on 11/2/2016.
 */

public class UpdatePhoneBody {
    @SerializedName("accountId") private int AccountId;
    @SerializedName("phone") private String PhoneNumber;

    public UpdatePhoneBody(int accountId, String phoneNumber) {
        AccountId = accountId;
        PhoneNumber = phoneNumber;
    }

    public int getAccountId() {
        return AccountId;
    }

    public void setAccountId(int accountId) {
        AccountId = accountId;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
