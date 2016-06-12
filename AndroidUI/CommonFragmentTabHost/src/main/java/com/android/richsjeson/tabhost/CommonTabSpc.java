package com.android.richsjeson.tabhost;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.richsjeson.tabhost.bean.PageItem;

/**
 * @author zyb
 * Tab标签类,属性标签
 * @date 2016/5/18 14:59
 */
public class CommonTabSpc {

    private Context mContext;

    private String mTitleFontColor;

    private String mTitleFontColorSelected;

    private String mTabBackground;

    private String mTabBackgroundSelect;

    private PageItem mPageItem;

    private View mView;

    private String mTag;

    private ImageView ivBadge;

    private ImageView ivCommonTabSpec;
    private TextView tvCommonTabSpec;




    public CommonTabSpc(Context context,String titleFontColor, String titleFontColorSelected, String tabBackground, String tabBackgroundSelect, PageItem item) {

        this.mContext=context;
        this.mTitleFontColor=titleFontColor;
        this.mTitleFontColorSelected=titleFontColorSelected;
        this.mTabBackground=tabBackground;
        this.mTabBackgroundSelect=tabBackgroundSelect;
        this.mPageItem=item;
    }

    public CommonTabSpc(Context context,PageItem item){
        this.mContext=context;
        this.mPageItem=item;
    }

    @Deprecated
    public void setTag(int tagId){

        mTag="tab_"+tagId;
    }

    public void setTag(String tagId){

        mTag=tagId;
    }

    public void setBadge(int count){
        Log.i(this.getClass().getName(), "count:=" + count);
        if(count>0) {
            ivBadge.setVisibility(View.VISIBLE);
        }else{
            ivBadge.setVisibility(View.GONE);
        }
    }


    public void setIndicator(CommonTabWidget tabWidget){

        mView= LayoutInflater.from(mContext).inflate(R.layout.common_tabspec, tabWidget,false);
        ivCommonTabSpec= (ImageView) mView.findViewById(R.id.iv_vs_common_tabspec);
        tvCommonTabSpec= (TextView) mView.findViewById(R.id.tv_vs_common_tabspec);
        ivBadge=(ImageView)mView.findViewById(R.id.iv_vs_common_badge);
        ivBadge.setVisibility(View.GONE);
        if(mPageItem.getTitle() != null && !mPageItem.getTitle().equals("")) {
            tvCommonTabSpec.setText(mPageItem.getTitle());
        }
        if(mTitleFontColor != null && !mTitleFontColor.equals("")  &&  getResourceColorId(mContext, mTitleFontColor) !=0){
            tvCommonTabSpec.setTextColor(mContext.getResources().getColor(getResourceColorId(mContext, mTitleFontColor)));
        }

        if(mTabBackground != null && !mTabBackground.equals("") &&  getResourceDrawableId(mContext, mTabBackground) !=0){
            mView.setBackgroundResource(getResourceDrawableId(mContext, mTabBackground));
        }

        if(mPageItem.getImage() != null && !mPageItem.getImage().equals("") &&  getResourceDrawableId(mContext, mPageItem.getImage()) !=0){
            ivCommonTabSpec.setImageResource(getResourceDrawableId(mContext, mPageItem.getImage()));
        }
    }

    public void setCurrentItem(){
        if(mTabBackground != null && !mTabBackground.equals("") &&  getResourceDrawableId(mContext, mTabBackgroundSelect) !=0){
            mView.setBackgroundResource(getResourceDrawableId(mContext, mTabBackgroundSelect));
        }
    }

    public void setCurrentFontColor(){
        if(mTitleFontColorSelected != null && !mTitleFontColorSelected.equals("")  &&  getResourceColorId(mContext, mTitleFontColorSelected) !=0){
            tvCommonTabSpec.setTextColor(mContext.getResources().getColor(getResourceColorId(mContext, mTitleFontColorSelected)));
        }
    }
    public void setCurrentImage(){
        if(mPageItem.getImageSelected() != null && !mPageItem.getImageSelected().equals("") &&  getResourceDrawableId(mContext, mPageItem.getImageSelected()) !=0){
            ivCommonTabSpec.setImageResource(getResourceDrawableId(mContext, mPageItem.getImageSelected()));
        }
    }

    public void setFontColor(){
        if(mTitleFontColor != null && !mTitleFontColor.equals("")  &&  getResourceColorId(mContext, mTitleFontColor) !=0){
            tvCommonTabSpec.setTextColor(mContext.getResources().getColor(getResourceColorId(mContext, mTitleFontColor)));
        }
    }

    public void setImage(){
        if(mPageItem.getImage() != null && !mPageItem.getImage().equals("") &&  getResourceDrawableId(mContext, mPageItem.getImage()) !=0){
            ivCommonTabSpec.setImageResource(getResourceDrawableId(mContext, mPageItem.getImage()));
        }
    }

    public void setItem(){
        if(mTabBackground != null && !mTabBackground.equals("") &&  getResourceDrawableId(mContext, mTabBackground) !=0){
            mView.setBackgroundResource(getResourceDrawableId(mContext, mTabBackground));
        }
    }

    public static int getResourceColorId(Context context,String name){
        return getResourceId(context,"color/"+name);
    }

    public static int getResourceDrawableId(Context context,String name){
        return getResourceId(context,"drawable/"+name);
    }

    /**
     * 执行操作，根据resource获取资源数据信息</p>
     * @param context
     * @param name
     * @return
     */
    public static int getResourceId(Context context,String name){
        Resources resources = context.getResources();
        int indentify = resources.getIdentifier(context.getPackageName()+":"+name, null, null);
        if(indentify>0){
            return indentify;
        }
        return 0;
    }

    public String getTag() {
        return this.mTag;
    }

    public View getIndicator() {
        return mView;
    }
}
