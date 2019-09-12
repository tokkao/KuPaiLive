package com.benben.kupaizhibo.bean;

/**
 * Created by: wanghk 2019-07-10.
 * Describe: 举报列表bean
 */
public class ReportListBean {


    /**
     * id : 1
     * classify_name : 低俗色情1
     */

    private int id;
    private String classify_name;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassify_name() {
        return classify_name;
    }

    public void setClassify_name(String classify_name) {
        this.classify_name = classify_name;
    }
}
