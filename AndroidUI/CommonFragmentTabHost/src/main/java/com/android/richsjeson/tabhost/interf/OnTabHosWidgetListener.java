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


    /**
     * 接收推送的操作
     * @param currentTab  当前Tab
     * @param count       下推的数量
     * @param isChecked    是否执行检查，判断当前Tab是否是已选中的tag
     */
    void pushNotifyBadge(int currentTab, int count,boolean isChecked);

    /**
     * 移除tab的badge 红点
     * @param currentTab
     */
    void removeNotifyBadge(int currentTab);
}
