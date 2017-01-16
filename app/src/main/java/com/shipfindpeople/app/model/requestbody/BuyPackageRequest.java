package com.shipfindpeople.app.model.requestbody;

/**
 * Created by sonnd on 9/29/2016.
 */

public class BuyPackageRequest {

    private int accountId;
    private int packageId;

    public BuyPackageRequest(int accountId, int packageId) {
        this.accountId = accountId;
        this.packageId = packageId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }
}
