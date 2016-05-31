package com.android.richsjeson.tabhost.interf;

import com.android.richsjeson.tabhost.bean.TabParams;

/**
 * @author zyb
 *tabHost回调事件
 * @date 2016/5/18 14:07
 */
public interface OnTabHosWidgetListener {
    /**
     * 获取参数集
     * @param params TabParam页面
     */
    void getTabParams(TabParams params);

    /**
     *
     * @param currentTab  标签的id
     * @param count 发送的条数
     */
    void getNotifyBadge(int currentTab, int count);
}
