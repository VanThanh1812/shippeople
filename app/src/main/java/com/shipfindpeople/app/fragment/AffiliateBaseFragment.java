package com.shipfindpeople.app.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class AffiliateBaseFragment extends Fragment {


    protected Context mContext;
    protected Unbinder mUnBinder;


    public AffiliateBaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        injectViews(view);
    }

    /**
     * Every fragment has to inflate a layout in the onCreateView method. We have added this method to
     * avoid duplicate all the inflate code in every fragment. You only have to return the layout to
     * inflate in this method when extends BaseFragment.
     */
    protected abstract int getFragmentLayout();

    /**
     * Replace every field annotated with ButterKnife annotations like @bind with the proper
     * value.
     *
     * @param view to extract each widget bind in the fragment.
     */
    private void injectViews(final View view) {
        this.mUnBinder = ButterKnife.bind(this, view);
    }

    /**
     * Get resources context when fragment attached
     *
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.mUnBinder.unbind();
    }
    /**
     * Show toast message shortcut
     * @param message - toast message
     */
    protected void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Fade out animation
     * @param v - target view
     */
    protected void fadeOut(final View v) {
        if (v.getVisibility() == View.VISIBLE) {
            Animation fadeOutAnim = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out);
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

    /**
     * Fade in animation
     * @param v - target view
     */
    protected void fadeIn(final View v)
    {
        if(v.getVisibility() != View.VISIBLE) {
            Animation fadeInAnim = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);

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
}
