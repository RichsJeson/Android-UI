package com.android.richsjeson.tabhost.presenter.impl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.richsjeson.tabhost.CommonFragmentTabHost;
import com.android.richsjeson.tabhost.bean.TabParams;
import com.android.richsjeson.tabhost.presenter.ITabHostPresneter;

/**
 * @author zyb
 * TabHost业务层
 * @date 2016/5/18 14:26
 */
public class TabHostPresenter implements ITabHostPresneter {

    private TabParams mParams;

    private CommonFragmentTabHost mTab;

    private TabHostView mView;

    private static TabHostPresenter mPrensenter;


    public static TabHostPresenter getInstance(){

        if(mPrensenter==null){
            mPrensenter=new TabHostPresenter();
        }
        return mPrensenter;
    }

    public void setView(TabHostView view){
        this.mView=view;
    }

    public CommonFragmentTabHost getTab() {
        return mTab;
    }

    public void setTab(CommonFragmentTabHost mTab) {
        this.mTab = mTab;
    }

    @Override
    public void getNotifyBadge(int currentTab, int count) {
        Log.i(this.getClass().getName(), "currentTab:=" + currentTab + ",count:=" + count);
        if(mTab != null) {
            mTab.setBadge(currentTab, count);
        }
    }

    @Override
    public void getTabParams(TabParams params) {
        this.mParams= params;
    }

    @Override
    public void setTabSpec(Bundle bundle) {
        mView.getTabSpc(mParams,bundle);
    }

    @Override
    public Fragment getFragment(int currentTab) {
        if(mTab != null){

            if(mTab.getmTabs().get(currentTab)!=null){
                CommonFragmentTabHost.TabInfo tabInfo=mTab.getmTabs().get(currentTab);
                return  tabInfo.getFragment();
            }
        }
        return null;
    }
}
