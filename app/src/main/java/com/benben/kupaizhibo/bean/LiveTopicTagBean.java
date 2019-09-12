package com.benben.kupaizhibo.bean;

/**
 * 直播话题标签
 */
public class LiveTopicTagBean extends FilterableBean{
    /**
     * id : 1
     * title : 眼部
     */

    private String id;
    private String title;

    private boolean isSelect = false;

    public LiveTopicTagBean() {
    }

    public LiveTopicTagBean(String id, String title, boolean isSelect) {
        this.id = id;
        this.title = title;
        this.isSelect = isSelect;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getFilterString() {
        return title;
    }
}
