package com.shipfindpeople.app.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sonnd on 10/24/2016.
 */

public class BrandsInfo {
    @SerializedName("error") private boolean error;
    @SerializedName("note") private String note;
    @SerializedName("brands") private ArrayList<TelecomService> brands;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<TelecomService> getBrands() {
        return brands;
    }

    public void setBrands(ArrayList<TelecomService> brands) {
        this.brands = brands;
    }
}
