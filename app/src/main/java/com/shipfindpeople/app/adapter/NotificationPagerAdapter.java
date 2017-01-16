package com.shipfindpeople.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shipfindpeople.app.fragment.NotificationTabFragment;

/**
 * Created by sonnd on 10/11/2016.
 */

public class NotificationPagerAdapter extends FragmentStatePagerAdapter {
    private String mTitle[];
    public NotificationPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setTitle(String[] titles){
        mTitle = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return NotificationTabFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return mTitle[position];
    }
}
