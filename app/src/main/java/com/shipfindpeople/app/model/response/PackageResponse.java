package com.shipfindpeople.app.model.response;

import com.google.gson.annotations.SerializedName;
import com.shipfindpeople.app.model.pojo.Package;

import java.util.ArrayList;

/**
 * Created by sonnd on 9/28/2016.
 */

public class PackageResponse {
    @SerializedName("error")
    private boolean Error;
    @SerializedName("mesage")
    private String Message;
    @SerializedName("list")
    private ArrayList<Package> Packages;

    public boolean isError() {
        return Error;
    }

    public void setError(boolean error) {
        Error = error;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<Package> getPackages() {
        return Packages;
    }

    public void setPackages(ArrayList<Package> packages) {
        Packages = packages;
    }
}
