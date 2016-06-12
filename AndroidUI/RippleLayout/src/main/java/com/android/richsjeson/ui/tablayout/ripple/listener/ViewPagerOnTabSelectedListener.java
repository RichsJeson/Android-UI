package com.android.richsjeson.ui.tablayout.ripple.listener;

import android.support.v4.view.ViewPager;

import com.nd.vrstore_refactor.presentation.common.widget.tablayout.bean.Tab;

/**
 * @author zyb
 * @version V1.0
 * @CoypRight:nd.com (c) 2016 All Rights Reserved
 * @Title: ${FILE_NAME}
 * @Package com.nd.library.tablayout.listener
 * @date 2016/6/3 11:10
 */
public class ViewPagerOnTabSelectedListener implements OnTabSelectedListener {

    private final ViewPager mViewPager;

    public ViewPagerOnTabSelectedListener(ViewPager viewPager) {
        this.mViewPager = viewPager;
    }

    public void onTabSelected(Tab tab) {
        this.mViewPager.setCurrentItem(tab.getPosition());
    }

    public void onTabUnselected(Tab tab) {
    }

    public void onTabReselected(Tab tab) {
    }
}
