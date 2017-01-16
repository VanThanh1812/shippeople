package com.shipfindpeople.app.model.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonnd on 9/28/2016.
 */

public class Package {
    @SerializedName("Id")
    private int Id;
    @SerializedName("Name")
    private String Name;
    @SerializedName("Day")
    private String DayString;
    @SerializedName("DayNumber")
    private int Day;
    @SerializedName("Status")
    private String Status;
    @SerializedName("Money")
    private String MoneyString;
    @SerializedName("MoneyNumber")
    private double Money;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDayString() {
        return DayString;
    }

    public void setDayString(String dayString) {
        DayString = dayString;
    }

    public int getDay() {
        return Day;
    }

    public void setDay(int day) {
        Day = day;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMoneyString() {
        return MoneyString;
    }

    public void setMoneyString(String moneyString) {
        MoneyString = moneyString;
    }

    public double getMoney() {
        return Money;
    }

    public void setMoney(double money) {
        Money = money;
    }
}
