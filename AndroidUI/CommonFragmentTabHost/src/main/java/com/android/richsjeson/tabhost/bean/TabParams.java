package com.android.richsjeson.tabhost.bean;
import java.io.Serializable;
import java.util.List;

/**
 * @author zyb
 * Tab实体类
 * @date 2016/5/18 13:59
 */
public class TabParams implements Serializable {
    /**
     * 页面属性集合
     */
    private List<PageItem> items;
    /**
     * tab的背景
     */
    private String tabBackground;
    /**
     * tab的背景选择器
     */
    private String tabBackgroundSelect;
    /**
     * 标题的字体颜色常态
     */
    private String titleFontColor;
    /**
     * 标题的字体选择器
     */
    private String titleFontColorSelect;

    public List<PageItem> getItems() {
        if(this.items==null){
            throw  new NullPointerException("TabItem 的列表不能为空");
        }
        return items;
    }

    public void setItems(List<PageItem> items) {
        this.items = items;
    }

    public String getTabBackground() {
        return tabBackground;
    }

    public void setTabBackground(String tabBackground) {
        this.tabBackground = tabBackground;
    }

    public String getTabBackgroundSelect() {
        return tabBackgroundSelect;
    }

    public void setTabBackgroundSelect(String tabBackgroundSelect) {
        this.tabBackgroundSelect = tabBackgroundSelect;
    }

    public String getTitleFontColor() {
        if(this.titleFontColor==null){
            throw  new NullPointerException("TabItem 的标题的默认未选中的颜色未定义");
        }
        return titleFontColor;
    }

    public void setTitleFontColor(String titleFontColor) {
        this.titleFontColor = titleFontColor;
    }

    public String getTitleFontColorSelect() {
        if(this.titleFontColorSelect==null){
            throw  new NullPointerException("TabItem 的标题被选中的颜色未定义");
        }
        return titleFontColorSelect;
    }

    public void setTitleFontColorSelect(String titleFontColorSelect) {
        this.titleFontColorSelect = titleFontColorSelect;
    }
}
