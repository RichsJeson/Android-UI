package com.android.richsjeson.tabhost;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.android.richsjeson.tabhost.bean.PageItem;
import com.android.richsjeson.tabhost.bean.TabParams;
import com.android.richsjeson.tabhost.interf.OnTabSelectedListener;
import com.android.richsjeson.tabhost.presenter.impl.TabHostPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richsjeson on 16-1-12.
 * 底部的TabHost
 */
public class CommonFragmentTabHost extends TabHost
        implements TabHost.OnTabChangeListener,TabHostPresenter.TabHostView {

    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    private final ArrayList<CommonTabSpc> mTabSpecs=new ArrayList<CommonTabSpc>();
    private FrameLayout mTabContent;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private int mContainerId;
    private OnTabChangeListener mOnTabChangeListener;
    private TabInfo mLastTab;
    private boolean mAttached;
    private CommonTabWidget mCommonTabWidget;
    private OnKeyListener mTabKeyListener;
    private View mCurrentView = null;
    private OnTabSelectedListener mOnTabSelectedListener;

    private  int mCurrentTab=-1;


    public void getTabSpc(TabParams tabParams,Bundle bundle) {
        if(tabParams != null){
            //读取数据
            String titleFontColor=tabParams.getTitleFontColor();

            String titleFontColorSelected=tabParams.getTitleFontColorSelect();

            String tabBackground=tabParams.getTabBackground();

            String tabBackgroundSelect=tabParams.getTabBackgroundSelect();

            List<PageItem> list=tabParams.getItems();

            if(list != null){
                try {
                    for (int i=0;i< list.size();i++) {
                        PageItem item =list.get(i);
                        CommonTabSpc tabSpc = new CommonTabSpc(mContext, titleFontColor, titleFontColorSelected, tabBackground, tabBackgroundSelect, item);
                        tabSpc.setTag(item.getTag());
                        tabSpc.setIndicator(mCommonTabWidget);
                        addTab(tabSpc, Class.forName(item.getPage()), bundle);
                    }
                }catch (NoClassDefFoundError e){
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    public static final class TabInfo {
        private final String tag;

        public Class<?> getTabClass() {
            return clss;
        }

        private final Class<?> clss;
        private final Bundle args;
        private Fragment fragment;

        TabInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }

        public Fragment getFragment() {
            return fragment;
        }

    }


    static class SavedState extends BaseSavedState {
        String curTab;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            curTab = in.readString();
        }


        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(curTab);
        }


        public String toString() {
            return "FragmentTabHost.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " curTab=" + curTab + "}";
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public CommonFragmentTabHost(Context context) {
        super(context, null);
        initFragmentTabHost(context, null);
    }

    public CommonFragmentTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFragmentTabHost(context, attrs);
    }

    private void initFragmentTabHost(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                new int[] { android.R.attr.inflatedId }, 0, 0);
        mContainerId = a.getResourceId(0, 0);
        a.recycle();
        TabHostPresenter.getInstance().setView(this);
        setOnTabChangedListener(this);
    }

    private void invokeOnTabChangeListener() {
        if (mOnTabChangeListener != null) {
            mOnTabChangeListener.onTabChanged(getCurrentTabTag());
        }
    }

    public void ensureHierarchy(Context context) {
        if (findViewById(R.id.common_tabwidget) == null) {
            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.VERTICAL);
            addView(ll, new LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.FILL_PARENT));

            CommonTabWidget tw = new CommonTabWidget(context);
            tw.setId(R.id.common_tabwidget);
            tw.setOrientation(TabWidget.HORIZONTAL);
            tw.setDividerDrawable(null);
            tw.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            ll.addView(tw, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 0));

            FrameLayout fl = new FrameLayout(context);
            fl.setId(R.id.common_tabcontent);
            ll.addView(fl);
            mTabContent = fl = new FrameLayout(context);
            mTabContent.setId(mContainerId);
            ll.addView(fl);
        }
    }


    public void setOnTabSelectedListener(OnTabSelectedListener listener){
        this.mOnTabSelectedListener=listener;
    }

    public void setup() {
        throw new IllegalStateException(
                "Must call setup() that takes a Context and FragmentManager");
    }

    public void setup(Context context, FragmentManager manager) {
        ensureHierarchy(context);
        setup(context);
        mContext = context;
        mFragmentManager = manager;
    }

    public void createTabHost(Context context, FragmentManager manager, int containerId,Bundle bundle) {
        ensureHierarchy(context);  // Ensure views required by super.setup()
        setup(context);
        mContext = context;
        mFragmentManager = manager;
        mContainerId = containerId;
        if (getId() == View.NO_ID) {
            setId(R.id.common_tabhost);
        }
        //执行addTab的操作
        TabHostPresenter.getInstance().setTab(this);
        TabHostPresenter.getInstance().setTabSpec(bundle);

    }


    public void setBadge(int currentTab, int count) {
        Log.i(this.getClass().getName(), "currentTab:=" + currentTab);
        CommonTabSpc tabSpc=mTabSpecs.get(currentTab);
        Log.i(this.getClass().getName(), "tabSpc:=" + tabSpc);
        if(tabSpc != null){
            tabSpc.setBadge(count);
        }
    }

    /**
     * 隐藏
     * @param currentTab   当前选中的Tab
     * @param count        推送的条数
     * @param isCheck   是否需要检查currentTab被选中
     */
    public void setBadge(int currentTab,int count,boolean isCheck) {
        Log.i(this.getClass().getName(), "currentTab:=" + currentTab);
        if(isCheck){
            if (this.mCurrentTab!=currentTab) {
                setBadge(currentTab,count);
            }
        }else{
            setBadge(currentTab, count);
        }
    }

    /**
     * 清理
     * @param currentTab
     */
    public void clearBadge(int currentTab){
        setBadge(currentTab, 0);
    }

    public void setOnTabChangedListener(OnTabChangeListener l) {
        mOnTabChangeListener = l;
    }

    public void addTab(CommonTabSpc tabSpec, Class<?> clss, Bundle args) {
        String tag = tabSpec.getTag();
        TabInfo info = new TabInfo(tag, clss, args);

        if (mAttached) {
            info.fragment = mFragmentManager.findFragmentByTag(tag);
            if (info.fragment != null && !info.fragment.isDetached()) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.hide(info.fragment);
                ft.commit();
            }
        }

        mTabs.add(info);
        mCommonTabWidget.addView(tabSpec.getIndicator());
        mTabSpecs.add(tabSpec);
        if(mCurrentTab==-1){
            setCurrentTab(0);
        }

    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        String currentTab = getCurrentTabTag();

        // Go through all tabs and make sure their fragments match
        // the correct state.
        FragmentTransaction ft = null;
        for (int i=0; i<mTabs.size(); i++) {
            TabInfo tab = mTabs.get(i);
            tab.fragment = mFragmentManager.findFragmentByTag(tab.tag);
            if (tab.fragment != null && !tab.fragment.isDetached()) {
                if (tab.tag.equals(currentTab)) {
                    // The fragment for this tab is already there and
                    // active, and it is what we really want to have
                    // as the current tab.  Nothing to do.
                    mLastTab = tab;
                } else {
                    // This fragment was restored in the active state,
                    // but is not the current tab.  Deactivate it.
                    if (ft == null) {
                        ft = mFragmentManager.beginTransaction();
                    }
                    ft.hide(tab.fragment);
                }
            }
        }

        // We are now ready to go.  Make sure we are switched to the
        // correct tab.
        mAttached = true;
        ft = doTabChanged(currentTab, ft);
        if (ft != null) {
            ft.commit();
            mFragmentManager.executePendingTransactions();
        }
    }

    public String getCurrentTabTag() {
        if (mCurrentTab >= 0 && mCurrentTab < mTabSpecs.size()) {
            return mTabSpecs.get(mCurrentTab).getTag();
        }
        return null;
    }



    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttached = false;
    }


    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.curTab = getCurrentTabTag();
        return ss;
    }


    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());
        setCurrentTabByTag(ss.curTab);
    }


    public void onTabChanged(String tabId) {
        if (mAttached) {
            FragmentTransaction ft = doTabChanged(tabId, null);
            if (ft != null) {
                ft.commit();
            }
        }
    }

    public FragmentTransaction doTabChanged(String tabId, FragmentTransaction ft) {
        TabInfo newTab = null;
        for (int i=0; i<mTabs.size(); i++) {
            TabInfo tab = mTabs.get(i);
            CommonTabSpc tabSpc=mTabSpecs.get(i);
            if (tab.tag.equals(tabId)) {
                newTab = tab;
                tabSpc.setCurrentFontColor();
                tabSpc.setCurrentImage();
                tabSpc.setCurrentItem();
            }else{
                tabSpc.setFontColor();
                tabSpc.setImage();
                tabSpc.setItem();
            }
        }
        if (newTab == null) {
            android.util.Log.i(this.getClass().getName(),"No tab known for tag " + tabId);
        }
        if (mLastTab != newTab) {
            if (ft == null) {
                ft = mFragmentManager.beginTransaction();
            }
            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
                    ft.hide(mLastTab.fragment);
                }
            }
            if (newTab != null) {

                if (newTab.fragment == null) {
                    newTab.fragment = Fragment.instantiate(mContext,
                            newTab.clss.getName(), newTab.args);
                    ft.add(mContainerId, newTab.fragment, newTab.tag);
                } else {
                    ft.show(newTab.fragment);
                }
            }

            mLastTab = newTab;
        }
        return ft;
    }

    public CommonTabWidget getCommonTabWidget() {
        return mCommonTabWidget;
    }



    void setup(Context context){
        mCommonTabWidget = (CommonTabWidget) findViewById(R.id.common_tabwidget);
        if (mCommonTabWidget == null) {
            throw new RuntimeException(
                    "Your TabHost must have a TabWidget whose id attribute is 'android.R.id.tabs'");
        }
        mTabContent = (FrameLayout) findViewById(R.id.common_tabcontent);
        if (mTabContent == null) {
            throw new RuntimeException(
                    "Your TabHost must have a FrameLayout whose id attribute is "
                            + "'android.R.id.tabcontent'");
        }

        mCommonTabWidget.setTabSelectionListener(new CommonTabWidget.OnTabSelectionChanged() {
            public void onTabSelectionChanged(int tabIndex, boolean clicked) {
                setCurrentTab(tabIndex);
                if (clicked) {
                    mTabContent.requestFocus(View.FOCUS_FORWARD);
                    //取出进行操作
                }
            }
        });

        mTabKeyListener = new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                    case KeyEvent.KEYCODE_DPAD_UP:
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                    case KeyEvent.KEYCODE_ENTER:
                        return false;

                }
                mTabContent.requestFocus(View.FOCUS_FORWARD);
                return mTabContent.dispatchKeyEvent(event);
            }

        };


    }
    public void setCurrentTab(int index) {

        if(mOnTabSelectedListener != null){
            mOnTabSelectedListener.onPageSelected(index);
        }

        if (index < 0 || index >= mTabs.size()) {
            return;
        }

        if (index == mCurrentTab) {
            return;
        }

        mCurrentTab = index;
        final CommonTabSpc spec = mTabSpecs.get(index);
        mCommonTabWidget.focusCurrentTab(mCurrentTab);
        mCurrentView = spec.getIndicator();

        if (mCurrentView.getParent() == null) {
            mTabContent
                    .addView(
                            mCurrentView,
                            new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT));
        }

        if (!mCommonTabWidget.hasFocus()) {
            mCurrentView.requestFocus();
        }

        invokeOnTabChangeListener();
    }

    public Fragment getCurrentFragment(int index){
        TabInfo tabInfo=mTabs.get(index);
        if(tabInfo!=null){
            return mTabs.get(index).getFragment();
        }else{
            return null;
        }
    }

    @Override
    public int getCurrentTab() {
        return mCurrentTab;
    }
}


