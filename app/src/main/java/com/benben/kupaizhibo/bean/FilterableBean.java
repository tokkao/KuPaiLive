package com.benben.kupaizhibo.bean;

/**
 * Author:zhn
 * Time:2019/5/9 11:26
 *
 * AutoSpinnerAdapter 使用的实体类继承这个抽象类，用于获取控件显示内容文字
 *
 * BottomMenuAdapter 使用的实体类继承这个抽象类，用于获取控件显示内容文字
 */
public abstract class FilterableBean {
    public abstract String getFilterString();
}
