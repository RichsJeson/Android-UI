package com.android.richsjeson.tabhost.domain;
import com.nd.vrstore_refactor.data.bean.PageItem;
import com.nd.vrstore_refactor.data.bean.TabParams;
import com.nd.vrstore_refactor.domain.business.ITabHostBusiness;
import com.nd.vrstore_refactor.presentation.store.ui.fragment.AppTabPagerFragment;
import com.nd.vrstore_refactor.presentation.store.ui.fragment.PanoramicTabPagerFragment;
import com.nd.vrstore_refactor.presentation.store.ui.fragment.PersonalCenterFragment;
import com.nd.vrstore_refactor.presentation.store.ui.fragment.RecommendIndexFragment;
import com.nd.vrstore_refactor.presentation.store.ui.listener.OnTabHosWidgetListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyb
 * TabHost的业务逻辑类
 * @date 2016/5/18 14:05
 */
public class TabHostBusiness{

    private OnTabHosWidgetListener listener;
    private static TabHostBusiness mBusiness;

    public static TabHostBusiness getInstance(){

        if(mBusiness==null){
            mBusiness=new TabHostBusiness();
        }
        return mBusiness;
    }

    @Override
    public void productTabParams() throws TabHostNoInitException {
        if(listener==null) {
            throw new TabHostNoInitException();
        }else{
            //TODO 此处参数要改
            TabParams mParams = new TabParams();
            List<PageItem> list=new ArrayList<PageItem>();
            mParams.setTabBackground("");
            mParams.setTabBackgroundSelect("");
            mParams.setTitleFontColor("vs_store_feedback_count");
            mParams.setTitleFontColorSelect("lightblue_common_text");

            PageItem tjPageItems=new PageItem();
            tjPageItems.setTitle("推荐");
            tjPageItems.setPage(RecommendIndexFragment.class.getName());
            tjPageItems.setImage("vs_btn_main_recommend_normal");
            tjPageItems.setImageSelected("vs_btn_main_recommend_selected");

            PageItem qjPageItems=new PageItem();
            qjPageItems.setTitle("全景");
            qjPageItems.setImage("vs_btn_main_panormaic_normal");
            qjPageItems.setImageSelected("vs_btn_main_panormaic_selected");
            qjPageItems.setPage(PanoramicTabPagerFragment.class.getName());


            PageItem yyPageItems = new PageItem();
            yyPageItems.setTitle("应用");
            yyPageItems.setImage("vs_btn_main_app_normal");
            yyPageItems.setImageSelected("vs_btn_main_app_selected");
            yyPageItems.setPage(AppTabPagerFragment.class.getName());

            PageItem myPageItems = new PageItem();
            myPageItems.setTitle("我的");
            myPageItems.setImage("vs_btn_main_personcenter_normal");
            myPageItems.setImageSelected("vs_btn_main_personcenter_selected");
            myPageItems.setPage(PersonalCenterFragment.class.getName());

            list.add(tjPageItems);
            list.add(qjPageItems);
            list.add(yyPageItems);
            list.add(myPageItems);
            mParams.setItems(list);

            listener.getTabParams(mParams);
        }

    }

    @Override
    public void pushNotifyBadge(int currentTab,int count) {
        listener.getNotifyBadge(currentTab, count);
    }

    @Override
    public void setOnTabHosWidgetListener(OnTabHosWidgetListener listener) {
        this.listener=listener;
    }

    public static class TabHostNoInitException extends Exception {
        public TabHostNoInitException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public TabHostNoInitException() {
            super("TabHost没有初始化或 OnTabHosWidgetListener没有注册监听");
        }
    }
}
