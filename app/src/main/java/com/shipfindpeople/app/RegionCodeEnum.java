package com.shipfindpeople.app;

/**
 * Created by Unstoppable on 8/30/2016.
 */
public enum RegionCodeEnum {
    Hanoi(1),
    Hochiminh(2),;

    private int regionCode;

    RegionCodeEnum(int c){
        regionCode = c;
    }

    public int getRegionCode(){
        return regionCode;
    }
}
