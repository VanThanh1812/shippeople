package com.shipfindpeople.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.shipfindpeople.app.BaseActivity;
import com.shipfindpeople.app.R;
import com.shipfindpeople.app.ServiceAPI;
import com.shipfindpeople.app.model.pojo.BrandsInfo;
import com.shipfindpeople.app.model.response.RechargeCommonResponse;
import com.shipfindpeople.app.model.requestbody.RechargeRequest;
import com.shipfindpeople.app.model.pojo.TelecomService;
import com.shipfindpeople.app.storage.AppPreference;
import com.shipfindpeople.app.storage.UserSaved;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RechargeActivity extends BaseActivity {
    TextView mSecondAccountCoinTV;
    @BindView(R.id.card_type_spinner)
    Spinner mTelecomServiceSpinner;
    @BindView(R.id.card_code_et)
    TextView mCardCodeET;
    @BindView(R.id.card_serial_et)
    TextView mCardSerialET;
    @BindView(R.id.warning_tv)
    TextView mWarningTV;
    @BindView(R.id.container_layout)
    RelativeLayout mContainerLayout;
    public UserSaved mUserSaved;

    private BrandsInfo mModel;
    private HashMap<String, String> mCardProvider;
    private ServiceAPI service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserSaved = new UserSaved(AppPreference.getInstance(this));
        setContentView(R.layout.activity_rechare);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Nạp tiền");
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.shiptimnguoi.vn/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ServiceAPI.class);

        showProgressDialog();
        service.GetService(mUserSaved.getId()).enqueue(new Callback<BrandsInfo>() {
            @Override
            public void onResponse(Call<BrandsInfo> call, Response<BrandsInfo> response) {
                mModel = response.body();
                mWarningTV.setText(mModel.getNote());
                mCardProvider = new HashMap<>();

                for (TelecomService item : mModel.getBrands()) {
                    mCardProvider.put(item.getName(), item.getValue());
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(RechargeActivity.this,
                        android.R.layout.simple_spinner_item, new ArrayList<>(mCardProvider.keySet()));

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                dataAdapter.insert("Chọn loại thẻ", 0);

                mTelecomServiceSpinner.setAdapter(dataAdapter);
                mTelecomServiceSpinner.setSelection(0);

                mCardProvider.put("Chọn loại thẻ", "");
                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<BrandsInfo> call, Throwable t) {
                dismissProgressDialog();
            }
        });
    }

    @OnClick(R.id.package_link_tv)
    public void Redirect(View v){
        Intent packageIntent = new Intent(this, PackageActivity.class);
        startActivity(packageIntent);
    }

    @OnClick(R.id.submit_card_info_btn)
    public void Recharge(View v) {

        if (mTelecomServiceSpinner.getSelectedItem() != null
                && TextUtils.isEmpty(mCardProvider.get(mTelecomServiceSpinner.getSelectedItem().toString()))) {
            showToast("Vui lòng chọn nhà cung cấp");
            return;
        }

        if (TextUtils.isEmpty(mCardCodeET.getText().toString().trim())) {
            showToast("Vui lòng nhập mã code");
            mCardCodeET.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mCardSerialET.getText().toString().trim())) {
            showToast("Vui lòng nhập mã serial");
            mCardSerialET.requestFocus();
            return;
        }

        showProgressDialog();
        service
                .Recharge(new RechargeRequest(mCardProvider.get(mTelecomServiceSpinner.getSelectedItem().toString()), mCardSerialET.getText().toString().trim(), mCardCodeET.getText().toString().trim(), mUserSaved.getId()))
                .enqueue(new Callback<RechargeCommonResponse>() {
                    @Override
                    public void onResponse(Call<RechargeCommonResponse> call, Response<RechargeCommonResponse> response) {

                        RechargeCommonResponse results = response.body();
                        showToast(results.getMessage());

                        if (!results.isError()) {
                            mCardCodeET.setText("");
                            mCardSerialET.setText("");
                        }
                        dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<RechargeCommonResponse> call, Throwable t) {
                        dismissProgressDialog();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
