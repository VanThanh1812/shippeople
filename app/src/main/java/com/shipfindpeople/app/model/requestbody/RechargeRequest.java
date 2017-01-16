package com.shipfindpeople.app.model.requestbody;

/**
 * Created by sonnd on 9/29/2016.
 */

public class RechargeRequest {
    private String vendor;
    private String serial;
    private String code;
    private int accountId;

    public RechargeRequest(String vendor, String serial, String code, int accountId) {
        this.vendor = vendor;
        this.serial = serial;
        this.code = code;
        this.accountId = accountId;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
