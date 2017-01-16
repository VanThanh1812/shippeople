package com.shipfindpeople.app;

import com.shipfindpeople.app.model.pojo.BrandsInfo;
import com.shipfindpeople.app.model.pojo.Package;
import com.shipfindpeople.app.model.requestbody.BuyPackageRequest;
import com.shipfindpeople.app.model.requestbody.LoginParamsBody;
import com.shipfindpeople.app.model.requestbody.RechargeRequest;
import com.shipfindpeople.app.model.requestbody.UpdatePhoneBody;
import com.shipfindpeople.app.model.response.CommonResponse;
import com.shipfindpeople.app.model.response.LoginResponse;
import com.shipfindpeople.app.model.response.RechargeCommonResponse;
import com.shipfindpeople.app.model.response.TopUpInfoResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sonnd on 10/24/2016.
 */

public interface ServiceAPI {
    @GET("Customer/GetCharging")
    Call<TopUpInfoResponse> GetAccountInfo(@Query("id")  int accountId);

    @POST("Customer/Charging")
    Call<RechargeCommonResponse> Recharge(@Body RechargeRequest body);

    @GET("Customer/GetPackage")
    Call<CommonResponse<ArrayList<Package>>> GetPackages(@Query("accountId")int accountId);

    @POST("Customer/BuyPackage")
    Call<RechargeCommonResponse> Extend(@Body BuyPackageRequest body);

    @GET("Customer/GetCharging")
    Call<BrandsInfo> GetService(@Query("id") int id);

    @POST("Customer/Signin")
    Call<CommonResponse<LoginResponse>> Login(@Body LoginParamsBody paramsBody);

    @GET("Customer/AccessApp")
    Call<CommonResponse<LoginResponse>> Accept(@Query("id") int id);

    @POST("Customer/UpdatePhone")
    Call<CommonResponse> UpdatePhone(@Body UpdatePhoneBody body);
}
