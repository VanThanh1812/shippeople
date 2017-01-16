package com.shipfindpeople.app.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shipfindpeople.app.R;
import com.shipfindpeople.app.ServiceAPI;
import com.shipfindpeople.app.activity.FacebookAccountKitActivity;
import com.shipfindpeople.app.activity.PackageActivity;
import com.shipfindpeople.app.activity.RechargeActivity;
import com.shipfindpeople.app.model.response.CommonResponse;
import com.shipfindpeople.app.model.requestbody.UpdatePhoneBody;
import com.shipfindpeople.app.storage.AppPreference;
import com.shipfindpeople.app.storage.IntentBundleKey;
import com.shipfindpeople.app.storage.UserSaved;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends AffiliateBaseFragment {
    @BindView(R.id.user_name)
    TextView usTextView;
    @BindView(R.id.email)
    TextView emailTextView;
    @BindView(R.id.coin)
    TextView coinTextView;
    @BindView(R.id.dateExpire)
    TextView dateExpireTextView;
    @BindView(R.id.confirm_phone)
    TextView confirmPhoneTextView;

    UserSaved userSaved;
    private Gson mGson;
    ServiceAPI stackOverflowAPI;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.shiptimnguoi.vn/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        stackOverflowAPI = retrofit.create(ServiceAPI.class);

        userSaved = new UserSaved(AppPreference.getInstance(mContext));
        if(userSaved.getPhone().isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            emailTextView.setVisibility(View.VISIBLE);
            builder.setMessage(R.string.confirm_phone_number_message)
                    .setTitle("Thông báo");
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            emailTextView.setVisibility(View.GONE);
            confirmPhoneTextView.setVisibility(View.GONE);
        }

        usTextView.setText(userSaved.getName());
        emailTextView.setText(userSaved.getPhone());
        coinTextView.setText("Tài khoản: " + userSaved.getCoin());
        dateExpireTextView.setText("Ngày hết hạn: " + userSaved.getDateExpire());
    }

    @OnClick(R.id.logout)
    public void Logout(View v) {
        userSaved.logOut(mContext);
    }

    @OnClick(R.id.recharge)
    public void Recharge(View v) {
        Intent rechareIntent = new Intent(mContext, RechargeActivity.class);
        startActivity(rechareIntent);
    }

    @OnClick(R.id.buy_package)
    public void BuyPackage(View v) {
        Intent packageIntent = new Intent(mContext, PackageActivity.class);
        startActivity(packageIntent);
    }

    @OnClick(R.id.confirm_phone)
    public void ConfirmPhoneNumber(View v) {
        Intent accountKit = new Intent(mContext, FacebookAccountKitActivity.class);
        startActivityForResult(accountKit, 99);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 99) {
            if (data.getBooleanExtra(IntentBundleKey.IS_SUCCESS, false)) {
                final String phoneNum = data.getStringExtra(IntentBundleKey.PHONE_NUMBER).replace("+84", "0");
                UpdatePhoneBody paramBody = new UpdatePhoneBody(userSaved.getId(), phoneNum);
                stackOverflowAPI.UpdatePhone(paramBody).enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        if(response != null && response.body() != null) {
                            Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            userSaved.setPhone(phoneNum);
                            emailTextView.setText(userSaved.getPhone());
                        }else{
                            showToast("Đã có lỗi xảy ra, vui lòng thử lại.");
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        showToast("Đã có lỗi xảy ra, vui lòng thử lại.");
                    }
                });
            } else {
                showToast("Xác thực số điện thoại thất bại");
            }
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_profile;
    }
}
