package com.shipfindpeople.app.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonnd on 9/28/2016.
 */

public class RechargeCommonResponse {
    @SerializedName("message") private String Message;
    @SerializedName("error") private boolean Error;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isError() {
        return Error;
    }

    public void setError(boolean error) {
        Error = error;
    }
}
