package com.shipfindpeople.app.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonnd on 10/5/2016.
 */

public class CommonResponse<T> {
    @SerializedName("message") private String Message;
    @SerializedName("error") private boolean Error;
    @SerializedName("data") private T Data;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public boolean isError() {
        return Error;
    }

    public void setError(boolean error) {
        Error = error;
    }
}
