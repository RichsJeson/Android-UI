package com.android.richsjeson.tabhost.bean;
import java.io.Serializable;

/**
 * @author zyb
 * @date 2016/5/18 13:56
 * 页面属性类
 */
public class PageItem implements Serializable {
    /**
     * 图片常态
     */
    private String image;
    /**
     * 图片选择器
     */
    private String imageSelected;
    /**
     * 页面URL地址
     */
    private String page;
    /**
     * item的title
     */
    private String title;
    /**
     * item的标记
     */
    private String tag;

    public void setImage(String image) {
        this.image = image;
    }

    public void setImageSelected(String imageSelected) {
        this.imageSelected = imageSelected;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getImage() {
        if(this.image==null){
            throw  new NullPointerException("TabItem ICON 默认的图片不能为空");
        }
        return image;
    }

    public String getImageSelected() {
        if(this.imageSelected==null){
            throw  new NullPointerException("TabItem ICON 被选中的图片不能为空");
        }
        return imageSelected;
    }

    public String getPage() {
        if(this.page==null){
            throw  new NullPointerException("TabItem 跳转的连接不能为空（Fragment）");
        }
        return page;
    }

    public String getTitle() {
        if(this.title==null){
            throw  new NullPointerException("TabItem 名称不能为空");
        }
        return title;
    }

    public String getTag() {
        if(this.tag==null){
            throw  new NullPointerException("TabItem 的Item 所对应的Tag不能为空");
        }
        return tag;
    }
}
