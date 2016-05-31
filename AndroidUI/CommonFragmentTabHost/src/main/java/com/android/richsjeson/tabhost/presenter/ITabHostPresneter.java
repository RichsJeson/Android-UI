package com.android.richsjeson.tabhost.presenter;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.richsjeson.tabhost.bean.TabParams;

/**
 * @author zyb
 * @version V1.0
 * TabHost业务处理类
 * @date 2016/5/18 14:24
 */
public interface ITabHostPresneter {


    void getNotifyBadge(int currentTab, int count);



    void getTabParams(TabParams params);

    /**
     * 获取Tab参数集合
     * @return
     */
    void setTabSpec(Bundle bundle);

    Fragment getFragment(int currentTab);

    interface TabHostView{

         void getTabSpc(TabParams tabParams, Bundle bundle);
    }


}
