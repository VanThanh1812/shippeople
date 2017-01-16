package com.shipfindpeople.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.shipfindpeople.app.adapter.NotificationPagerAdapter;
import com.shipfindpeople.app.R;
import com.shipfindpeople.app.widget.SlidingTabLayout;

import butterknife.BindArray;
import butterknife.BindView;

public class NotificationFragment extends AffiliateBaseFragment {

    public static final String TAG;

    static{
        TAG = NotificationFragment.class.getSimpleName();
    }

    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    SlidingTabLayout tabs;
    @BindArray(R.array.notification_tab_title_arr)
    String mTitles[];
    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        /*Bundle args = new Bundle();
        args.putInt(IntentBundleKey.NOTIFICATION_TAB_POSITION, position);
        fragment.setArguments(args);*/
        return fragment;
    }

    public NotificationFragment() {
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_notification;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NotificationPagerAdapter notificationPagerAdapter
                = new NotificationPagerAdapter(((FragmentActivity) mContext).getSupportFragmentManager());
        notificationPagerAdapter.setTitle(mTitles);

        viewPager.setAdapter(notificationPagerAdapter);
        tabs.setDistributeEvenly(true);
        tabs.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(mContext, android.R.color.white);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(viewPager);
    }
}
