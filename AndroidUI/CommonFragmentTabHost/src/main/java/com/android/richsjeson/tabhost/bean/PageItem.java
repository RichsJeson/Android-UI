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

    public String getImage() {
        return image;
    }

    public String getImageSelected() {
        return imageSelected;
    }

    public String getPage() {
        return page;
    }

    public String getTitle() {
        return title;
    }
}
