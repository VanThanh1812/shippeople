package com.shipfindpeople.app.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shipfindpeople.app.R;
import com.shipfindpeople.app.ServiceAPI;
import com.shipfindpeople.app.model.requestbody.BuyPackageRequest;
import com.shipfindpeople.app.model.pojo.Package;
import com.shipfindpeople.app.model.response.RechargeCommonResponse;
import com.shipfindpeople.app.storage.AppPreference;
import com.shipfindpeople.app.storage.UserSaved;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sonnd on 9/28/2016.
 */

public class PackageAdapter extends BaseRecyclerAdapter<Package> {

    private Context mContext;
    private ArrayList<Package> mModel;
    private ServiceAPI stackOverflowAPI;
    private UserSaved userSaved;
    private ProgressDialog mLoadingDialog;

    public PackageAdapter(Context mContext) {
        this.mContext = mContext;
        setUseDefaultClickListener(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.shiptimnguoi.vn/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        stackOverflowAPI = retrofit.create(ServiceAPI.class);
        userSaved = new UserSaved(AppPreference.getInstance(mContext));
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mPackageNameTV;
        private TextView mCostTV;
        private TextView mDeadlineTV;
        private TextView mStateTV;
        public MyViewHolder(View itemView) {
            super(itemView);
            mPackageNameTV = (TextView) itemView.findViewById(R.id.pkg_name_tv);
            mCostTV = (TextView) itemView.findViewById(R.id.pkg_cost_tv);
            mDeadlineTV = (TextView) itemView.findViewById(R.id.pkg_deadline_tv);
            mStateTV = (TextView) itemView.findViewById(R.id.pkg_state_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final Package aPackage = mModel.get(getAdapterPosition());
            new AlertDialog.Builder(mContext)
                    .setTitle("Thông báo")
                    .setMessage("Bạn có chắc muốn mua gói gia hạn: "+ aPackage.getName())
                    .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mLoadingDialog.show();
                            stackOverflowAPI.Extend(new BuyPackageRequest(userSaved.getId(), aPackage.getId()))
                            .enqueue(new Callback<RechargeCommonResponse>() {
                                @Override
                                public void onResponse(Call<RechargeCommonResponse> call, Response<RechargeCommonResponse> response) {
                                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    mLoadingDialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<RechargeCommonResponse> call, Throwable t) {
                                    mLoadingDialog.dismiss();
                                }
                            });
                        }

                    })
                    .setNegativeButton("Bỏ qua", null)
                    .show();
        }
    }


    @Override
    public void bindMyViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PackageAdapter.MyViewHolder) {
            MyViewHolder convertVh = (MyViewHolder) holder;

            Package pkg = mModel.get(position);
            convertVh.mPackageNameTV.setText(pkg.getName());
            convertVh.mCostTV.setText(pkg.getMoneyString());
            convertVh.mDeadlineTV.setText(pkg.getDayString());
            convertVh.mStateTV.setText(pkg.getStatus());

        }
    }

    @Override
    public int getTotal() {
        return mModel.size();
    }

    @Override
    public ArrayList getArrayList() {
        return mModel;
    }

    @Override
    public void setArrayList(ArrayList data) {
        mModel = data;
    }

    @Override
    public RecyclerView.ViewHolder getCustomViewHolder(ViewGroup parent) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.package_layout_item, parent, false);

        return new PackageAdapter.MyViewHolder(itemView);
    }
}
