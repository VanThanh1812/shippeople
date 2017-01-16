package com.shipfindpeople.app.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.shipfindpeople.app.BaseActivity;
import com.shipfindpeople.app.R;
import com.shipfindpeople.app.ServiceAPI;
import com.shipfindpeople.app.adapter.PackageAdapter;
import com.shipfindpeople.app.model.pojo.Package;
import com.shipfindpeople.app.model.response.CommonResponse;
import com.shipfindpeople.app.storage.AppPreference;
import com.shipfindpeople.app.storage.UserSaved;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PackageActivity extends BaseActivity {
    public UserSaved mUserSaved;
    @BindView(R.id.rv_data_binding)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    View mLoadingLayout;
    private PackageAdapter packageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        mUserSaved = new UserSaved(AppPreference.getInstance(this));
        packageAdapter = new PackageAdapter(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Các gói gia hạn");
        }

        mRecyclerView.setVisibility(View.GONE);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.shiptimnguoi.vn/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServiceAPI service = retrofit.create(ServiceAPI.class);

        service
                .GetPackages(mUserSaved.getId())
                .enqueue(new Callback<CommonResponse<ArrayList<Package>>>() {
                    @Override
                    public void onResponse(Call<CommonResponse<ArrayList<Package>>> call, Response<CommonResponse<ArrayList<Package>>> response) {
                        packageAdapter.setArrayList(response.body().getData());
                        mRecyclerView.setAdapter(packageAdapter);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mLoadingLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<CommonResponse<ArrayList<Package>>> call, Throwable t) {
                        showToast("Đã có lỗi xảy ra");
                    }
                });
        //mLoadingLayout.setVisibility(vi);


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
