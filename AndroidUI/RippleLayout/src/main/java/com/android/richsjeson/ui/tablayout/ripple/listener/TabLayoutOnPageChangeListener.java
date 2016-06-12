package com.android.richsjeson.ui.tablayout.ripple.listener;

import android.support.v4.view.ViewPager;

import com.android.richsjeson.ui.tablayout.ripple.RippleTabLayout;

import java.lang.ref.WeakReference;

/**
 * @author zyb
 * @date 2016/6/3 11:11
 */
public class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {

    private final WeakReference<RippleTabLayout> mTabLayoutRef;
    private int mPreviousScrollState;
    private int mScrollState;

    public TabLayoutOnPageChangeListener(RippleTabLayout rippleTabLayout) {
        this.mTabLayoutRef = new WeakReference(rippleTabLayout);
    }

    public void onPageScrollStateChanged(int state) {
        this.mPreviousScrollState = this.mScrollState;
        this.mScrollState = state;
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        RippleTabLayout rippleTabLayout = (RippleTabLayout) this.mTabLayoutRef.get();
        if (rippleTabLayout != null) {
            rippleTabLayout.setScrollPosition(position, positionOffset, true);
        }

    }

    public void onPageSelected(int position) {
        RippleTabLayout rippleTabLayout = (RippleTabLayout) this.mTabLayoutRef.get();
        if (rippleTabLayout != null) {
            rippleTabLayout.selectTab(rippleTabLayout.getTabAt(position),mScrollState == ViewPager.SCROLL_STATE_IDLE);
        }

    }
}
