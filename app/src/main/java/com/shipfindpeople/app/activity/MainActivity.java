package com.shipfindpeople.app.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.shipfindpeople.app.BaseActivity;
import com.shipfindpeople.app.fragment.HomeFragment;
import com.shipfindpeople.app.fragment.ProfileFragment;
import com.shipfindpeople.app.R;
import com.shipfindpeople.app.storage.AppPreference;
import com.shipfindpeople.app.storage.UserSaved;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;

    private Fragment mCurrentFragment;
    private UserSaved userSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userSaved = new UserSaved(AppPreference.getInstance(this));

        if (!userSaved.getPhone().isEmpty()) {
            mBottomBar.setDefaultTab(R.id.tab_home);
        } else {
            mBottomBar.setDefaultTab(R.id.tab_profile);
        }

        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_home:
                        if (!userSaved.getPhone().isEmpty()) {
                            mCurrentFragment = HomeFragment.newInstance();
                        } else {
                            showToast(getString(R.string.confirm_phone_number_message));
                            mBottomBar.getTabAtPosition(1).setSelected(true);
                        }
                        break;

                    case R.id.tab_profile:
                        mCurrentFragment = ProfileFragment.newInstance();
                        break;
                }

                setMainContent(mCurrentFragment);
            }
        });
    }

    public void setMainContent(Fragment fm) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, fm).commit();
    }
}
