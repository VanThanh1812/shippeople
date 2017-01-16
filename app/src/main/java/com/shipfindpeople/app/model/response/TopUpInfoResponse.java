package com.shipfindpeople.app.model.response;

import com.google.gson.annotations.SerializedName;
import com.shipfindpeople.app.model.pojo.AccountInfo;
import com.shipfindpeople.app.model.pojo.TelecomService;

import java.util.ArrayList;

/**
 * Created by sonnd on 9/27/2016.
 */

public class TopUpInfoResponse {
    @SerializedName("message")
    private String Message;
    @SerializedName("note")
    private String Note;
    @SerializedName("brands")
    private ArrayList<TelecomService> Services;
    @SerializedName("account")
    private com.shipfindpeople.app.model.pojo.AccountInfo AccountInfo;
    @SerializedName("error")
    private boolean Error;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<TelecomService> getServices() {
        return Services;
    }

    public void setServices(ArrayList<TelecomService> services) {
        Services = services;
    }

    public AccountInfo getAccountInfo() {
        return AccountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        AccountInfo = accountInfo;
    }

    public boolean isError() {
        return Error;
    }

    public void setError(boolean error) {
        Error = error;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
