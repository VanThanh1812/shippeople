package com.shipfindpeople.app.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shipfindpeople.app.R;
import com.shipfindpeople.app.model.pojo.Notification;
import com.shipfindpeople.app.storage.AppPreference;
import com.shipfindpeople.app.storage.UserSaved;

import java.util.ArrayList;

/**
 * Created by sonnd on 10/12/2016.
 */
public class NotificationAdapter extends BaseRecyclerAdapter<Notification> {


    private Context mContext;
    private ArrayList<Notification> mModel;
    private Gson mGson;
    private UserSaved mUser;

    public NotificationAdapter(Context mContext) {
        this.mContext = mContext;
        setUseDefaultClickListener(true);
        mGson = new Gson();
        mUser = new UserSaved(AppPreference.getInstance(mContext));
    }

    private class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mNotiAvatar, mIvPinOrder, mIvRemovePinned;
        private View mBtnPin;
        private View call;
        private TextView mContent, mCreator, mDateCreated;

        NotificationViewHolder(View itemView) {
            super(itemView);
            mContent = (TextView) itemView.findViewById(R.id.noti_content_tv);
            mCreator = (TextView) itemView.findViewById(R.id.noti_creator_name_tv);
            mDateCreated = (TextView) itemView.findViewById(R.id.noti_date_created_tv);
            mIvPinOrder = (ImageView) itemView.findViewById(R.id.iv_not_pin);
            mIvRemovePinned = (ImageView) itemView.findViewById(R.id.iv_pinned);
            mBtnPin = itemView.findViewById(R.id.btn_pin_order);
            call = itemView.findViewById(R.id.call);
            itemView.findViewById(R.id.fb_inbox).setOnClickListener(this);
            itemView.findViewById(R.id.fb_comment).setOnClickListener(this);
            call.setOnClickListener(this);
            mBtnPin.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            final Notification orderItem = mModel.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.fb_inbox:
                    pinOrder(orderItem, getAdapterPosition(), false);
                    if (!orderItem.getCreatorId().isEmpty()) {
                        String facebookUrl = "https://www.facebook.com/" + orderItem.getCreatorId();

                        String facebookUrlScheme = "fb://profile/" + orderItem.getCreatorId();

                        try {
                            int versionCode = mContext.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;

                            if (versionCode >= 3002850) {
                                Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                                mContext.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                            } else {
                                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrlScheme)));
                            }
                        } catch (Exception e) {
                            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
                        }
                    }
                    break;
                case R.id.fb_comment:
                    pinOrder(orderItem, getAdapterPosition(), false);
                    String facebookCmtUrl = "https://www.facebook.com/" + orderItem.getPostId();
                    String facebookCmtUrlScheme = "fb://page/" + orderItem.getPostId();

                    try {
                        int versionCode = mContext.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
                        Intent cmtMessage;
                        if (versionCode >= 3002850) {
                            Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookCmtUrl);
                            cmtMessage = new Intent(Intent.ACTION_VIEW, uri);
                        } else {

                            cmtMessage = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookCmtUrlScheme));
                        }

                        mContext.startActivity(cmtMessage);

                    } catch (PackageManager.NameNotFoundException | ActivityNotFoundException e) {
                        e.printStackTrace();
                        String groupId = orderItem.getPostId().split("_")[0];
                        String feedId = orderItem.getPostId().split("_")[1];

                        mContext.startActivity(new Intent(Intent.ACTION_VIEW
                                , Uri.parse("https://m.facebook.com/groups/" + groupId + "?view=permalink&id=" + feedId + "")));
                    }

                    break;
                case R.id.btn_pin_order:
                    pinOrder(orderItem, getAdapterPosition(), true);
                    break;
                case R.id.call:
                    pinOrder(orderItem, getAdapterPosition(), false);
                    openCallDialog2(mContext, orderItem.getPhone());
                    break;
            }
        }
    }

    public void pinOrder(final Notification orderItem, final int position, boolean removable) {
        if (orderItem.isPinned()) {
            if(removable) {
                if (!mUser.getOrder().isEmpty()) {
                    ArrayList<Notification> orders = mGson.fromJson(mUser.getOrder(), new TypeToken<ArrayList<Notification>>() {
                    }.getType());
                    for (Notification order : orders) {
                        if (order.getPostId().equals(orderItem.getPostId())) {
                            orders.remove(order);
                            break;
                        }
                    }
                    mUser.setOrder(mGson.toJson(orders));
                    mModel.remove(orderItem);
                    notifyItemRemoved(position);
                }
            }
        } else {
            orderItem.setPinned(true);
            ArrayList<Notification> orders = new ArrayList<>();
            if (!mUser.getOrder().isEmpty()) {
                orders = mGson.fromJson(mUser.getOrder(), new TypeToken<ArrayList<Notification>>() {}.getType());
            }
            orders.add(orderItem);
            if(orders.size() > 20){
                orders.remove(20);
            }
            mUser.setOrder(mGson.toJson(orders));
            notifyItemChanged(position);

            Intent callback = new Intent("com.shipfindpeople.app.NOTIFICATION");
            callback.putExtra("notification", orderItem);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(callback);
        }
    }

    @Override
    public void bindMyViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NotificationViewHolder) {

            NotificationViewHolder convertVH = (NotificationViewHolder) holder;
            final Notification item = mModel.get(position);
            convertVH.call.setVisibility(View.GONE);
            convertVH.mIvRemovePinned.setVisibility(View.GONE);
            convertVH.mIvPinOrder.setVisibility(View.GONE);

            if (!item.getPhone().isEmpty()) {
                convertVH.call.setVisibility(View.VISIBLE);
            }

            if (item.isPinned()) {

                convertVH.mIvRemovePinned.setVisibility(View.VISIBLE);
            } else {

                convertVH.mIvPinOrder.setVisibility(View.VISIBLE);
            }

            convertVH.mContent.setText(item.getContent());
            convertVH.mCreator.setText(item.getCreatorName());
            convertVH.mDateCreated.setText(item.getDateCreated());
        }
    }

    @Override
    public int getTotal() {
        return mModel.size();
    }

    @Override
    public ArrayList<Notification> getArrayList() {
        return mModel;
    }

    @Override
    public void setArrayList(ArrayList<Notification> data) {
        mModel = data;
    }

    @Override
    public RecyclerView.ViewHolder getCustomViewHolder(ViewGroup parent) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.notificaiton_item_layout, parent, false);

        return new NotificationViewHolder(itemView);
    }

    public void openCallDialog2(final Context ctx, String phoneStr) {
        String[] phones = phoneStr.split(",");
        if (phones.length > 1) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ctx, R.style.NonActivityDialog));
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.popup_call_from_fb_feed, null);

            builder.setView(dialogView);

            final AlertDialog dialog = builder.create();
            // Set the dialog negative button
            LinearLayout container = (LinearLayout) dialogView.findViewById(R.id.container_call_popup);

            for (final String phone : phones) {
                View callItem = inflater.inflate(R.layout.phone_number_item_layout, container, false);
                ((TextView) callItem.findViewById(R.id.tv_phone_number)).setText(phone);
                callItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callPhone(ctx, phone);
                    }
                });
                container.addView(callItem);
            }

            dialogView.findViewById(R.id.submit_change_shipping_cost).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } else {
            callPhone(ctx, phones.length > 0 ? phones[0] : "");
        }
    }

    public void callPhone(Context context, String numberPhone) {

        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + numberPhone));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
