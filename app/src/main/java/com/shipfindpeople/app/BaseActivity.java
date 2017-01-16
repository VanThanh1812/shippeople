package com.shipfindpeople.app;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected View mRootView;
    private Toolbar mCommonToolbar;
    private ProgressDialog mProgressDialog;
    private ProgressBar mProgressBar;
    private ViewStub mViewStub;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewStub = new ViewStub(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbindDrawables(this.mRootView);

    }

    public void setProgressBar(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    public void setToolbar(Toolbar toolbar, String title) {
        this.mCommonToolbar = toolbar;
        setSupportActionBar(this.mCommonToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(title);
        }
    }

    public void setToolbar(Toolbar toolbar) {
        setToolbar(toolbar, "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        } else {
            winParams.flags &= -WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        }
        win.setAttributes(winParams);
    }


    protected Toolbar getBaseActionBar() {
        return this.mCommonToolbar;
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void pushFragment(Fragment fragment, FragmentManager fragmentManager, @IdRes int layoutId){
        Fragment oldFragment;
        fragmentManager.executePendingTransactions();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(fragmentManager.getFragments() == null || fragmentManager.getFragments().size() == 0) {
            fragmentTransaction
                    .add(layoutId, fragment)
                    .addToBackStack(null);
        }else{
            fragmentTransaction
                    .replace(layoutId, fragment)
                    .addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    protected void fadeOut(final View v) {
        if (v.getVisibility() == View.VISIBLE) {
            Animation fadeOutAnim = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
            fadeOutAnim.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.GONE);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }
            });
            v.startAnimation(fadeOutAnim);
        }
    }

    protected void showProgressDialog() {
        mProgressDialog = ProgressDialog.show(this, "",
                "Đang tải...", true);
    }

    protected void dismissProgressDialog() {
        if(mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    protected void showProgressBar() {
        this.mProgressBar.setVisibility(View.VISIBLE);
    }

    protected void hideProgressBar() {
        this.mProgressBar.setVisibility(View.GONE);
    }

    protected void fadeIn(final View v) {
        if (v.getVisibility() != View.VISIBLE) {
            Animation fadeInAnim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);

            fadeInAnim.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.VISIBLE);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }
            });
            v.startAnimation(fadeInAnim);
        }
    }

    public void unbindDrawables(View view) {
        if (view != null) {
            int i;
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
                if (Build.VERSION.SDK_INT >= 16) {
                    View parent = (View) view.getParent();
                    if (parent == null || !(parent instanceof SwipeRefreshLayout)) {
                        view.setBackground(null);
                    }
                }
            }
            if ((view instanceof ImageView) && ((ImageView) view).getDrawable() != null) {
                ((ImageView) view).setImageDrawable(null);
                ((ImageView) view).setImageBitmap(null);
            }
            if ((view instanceof RecyclerView) && ((RecyclerView) view).getAdapter() != null) {
                int itemCount = ((RecyclerView) view).getAdapter().getItemCount();
                for (i = 0; i < itemCount; i++) {
                    RecyclerView.ViewHolder holder = ((RecyclerView) view).findViewHolderForAdapterPosition(i);
                    if (holder != null) {
                        unbindDrawables(holder.itemView);
                    }
                }
            }
            if (view instanceof ViewGroup) {
                for (i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
                if (!(view instanceof AdapterView)) {
                    ((ViewGroup) view).removeAllViews();
                }
            }
        }
    }

}
