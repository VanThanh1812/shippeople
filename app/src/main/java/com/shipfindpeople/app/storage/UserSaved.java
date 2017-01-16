package com.shipfindpeople.app.storage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.shipfindpeople.app.activity.LoginActivity;

/**
 * Created by sonnd on 9/26/2016.
 */

public class UserSaved{
    private AppPreference AppPreference;

    private static final String IS_LOGIN = "isLogged";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_PARTNER_ID = "partnerId";
    private static final String KEY_SHOP_ID = "shopId";
    private static final String KEY_DINERS_ID = "dinersId";
    private static final String KEY_TYPE = "type";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_USERNAME = "userName";
    private static final String KEY_ORDER = "order";
    private static final String KEY_COIN = "coin";
    private static final String KEY_DATE_EXPIRE = "dateExpire";

    public UserSaved() {
    }

    public UserSaved(AppPreference AppPreference) {
        this.AppPreference = AppPreference;
    }

    public int getId() {
        return AppPreference.getInt(KEY_ID, 0);
    }

    public void setId(int id) {
        AppPreference.setInt(KEY_ID, id);
    }

    public String getName() {
        return AppPreference.getString(KEY_NAME, "");
    }

    public void setName(String name) {
        AppPreference.setString(KEY_NAME, name);
    }

    public String getGender() {
        return AppPreference.getString(KEY_GENDER, "");
    }

    public void setGender(String gender) {
        AppPreference.setString(KEY_GENDER, gender);
    }

    public String getEmail() {
        return AppPreference.getString(KEY_EMAIL, "");
    }

    public void setEmail(String email) {
        AppPreference.setString(KEY_EMAIL, email);
    }

    public String getPhone() {
        return AppPreference.getString(KEY_PHONE, "");
    }

    public void setPhone(String phone) {
        AppPreference.setString(KEY_PHONE, phone);
    }

    public int getType() {
        return AppPreference.getInt(KEY_TYPE, 0);
    }

    public void setType(int type) {
        AppPreference.setInt(KEY_TYPE, type);
    }

    public String getAvatar() {
        return AppPreference.getString(KEY_AVATAR, "");
    }

    public void setAvatar(String avatar) {
        AppPreference.setString(KEY_AVATAR, avatar);
    }

    public String getUserName() {
        return AppPreference.getString(KEY_USERNAME, "");
    }

    public void setUserName(String userName) {
        AppPreference.setString(KEY_USERNAME, userName);
    }

    public String getOrder() {
        return AppPreference.getString(KEY_ORDER, "");
    }

    public void setOrder(String order) {
        AppPreference.setString(KEY_ORDER, order);
    }

    public String getCoin() {
        return AppPreference.getString(KEY_COIN, "");
    }

    public void setCoin(String order) {
        AppPreference.setString(KEY_COIN, order);
    }

    public String getDateExpire() {
        return AppPreference.getString(KEY_DATE_EXPIRE, "");
    }

    public void setDateExpire(String order) {
        AppPreference.setString(KEY_DATE_EXPIRE, order);
    }

    private void clearProfile() {
        this.AppPreference.remove(KEY_ID);
        this.AppPreference.remove(KEY_NAME);
        this.AppPreference.remove(KEY_PHONE);
        this.AppPreference.remove(KEY_EMAIL);
        this.AppPreference.remove(KEY_PARTNER_ID);
        this.AppPreference.remove(KEY_SHOP_ID);
        this.AppPreference.remove(KEY_DINERS_ID);
        this.AppPreference.remove(KEY_AVATAR);
        this.AppPreference.remove(KEY_USERNAME);
        this.AppPreference.remove(KEY_ORDER);
        this.AppPreference.remove(KEY_COIN);
        this.AppPreference.remove(KEY_DATE_EXPIRE);
    }

    @Deprecated
    public void logOut(Context ctx){
        clearProfile();
        Intent intentLogin = new Intent(ctx, LoginActivity.class);

        intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        ctx.startActivity(intentLogin);
        ((Activity)ctx).finish();
    }
}
