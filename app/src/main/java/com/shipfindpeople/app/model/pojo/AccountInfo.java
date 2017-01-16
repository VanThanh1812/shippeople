package com.shipfindpeople.app.model.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonnd on 9/27/2016.
 */

public class AccountInfo {
    @SerializedName("Total")
    private String Total;
    @SerializedName("First")
    private String First;
    @SerializedName("Second")
    private String Second;
    @SerializedName("Deposit")
    private String Deposit;

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getFirst() {
        return First;
    }

    public void setFirst(String first) {
        First = first;
    }

    public String getSecond() {
        return Second;
    }

    public void setSecond(String second) {
        Second = second;
    }

    public String getDeposit() {
        return Deposit;
    }

    public void setDeposit(String deposit) {
        Deposit = deposit;
    }
}
