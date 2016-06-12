package com.android.richsjeson.ui.tablayout.ripple.listener;


import com.android.richsjeson.ui.tablayout.ripple.bean.Tab;

/**
 * @author zyb
 * @date 2016/6/3 15:21
 */
public interface OnTabSelectedListener {

    /**
     * Called when a tab enters the selected state.
     *
     * @param tab The tab that was selected
     */
    void onTabSelected(Tab tab);

    /**
     * Called when a tab exits the selected state.
     *
     * @param tab The tab that was unselected
     */
    void onTabUnselected(Tab tab);

    /**
     * Called when a tab that is already selected is chosen again by the user. Some applications
     * may use this action to return to the top level of a category.
     *
     * @param tab The tab that was reselected.
     */
    void onTabReselected(Tab tab);
}
